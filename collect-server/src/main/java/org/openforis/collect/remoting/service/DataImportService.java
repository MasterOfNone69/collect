package org.openforis.collect.remoting.service;

import java.io.File;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openforis.collect.concurrency.CollectJobManager;
import org.openforis.collect.io.data.DataImportSummary;
import org.openforis.collect.io.data.DataRestoreJob;
import org.openforis.collect.io.data.DataRestoreSummaryJob;
import org.openforis.collect.io.data.RecordProvider;
import org.openforis.collect.io.data.TransactionalDataRestoreJob;
import org.openforis.collect.io.data.XMLParsingRecordProvider;
import org.openforis.collect.io.data.proxy.DataRestoreJobProxy;
import org.openforis.collect.io.data.proxy.DataRestoreSummaryJobProxy;
import org.openforis.collect.io.exception.DataImportExeption;
import org.openforis.collect.manager.RecordSessionManager;
import org.openforis.collect.manager.SurveyManager;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.remoting.service.dataimport.DataImportSummaryProxy;
import org.openforis.collect.web.session.SessionState;
import org.openforis.concurrency.proxy.JobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

/**
 * 
 * @author S. Ricci
 *
 */
public class DataImportService {
	
	private static final Log log = LogFactory.getLog(DataImportService.class);	
	
	@Autowired
	private RecordSessionManager sessionManager;
	@Autowired
	private CollectJobManager jobManager;
	@Autowired
	private SurveyManager surveyManager;
	
	private File packagedFile;
	
	private DataRestoreSummaryJob summaryJob;
	private DataRestoreJob dataRestoreJob;
	
	@Secured("ROLE_ADMIN")
	public JobProxy startSummaryCreation(String filePath, String selectedSurveyUri, boolean overwriteAll,
			boolean completeSummary) throws DataImportExeption {
		if ( summaryJob == null || ! summaryJob.isRunning() ) {
			log.info("Starting data import summary creation");
			
			packagedFile = new File(filePath);

			log.info("Using file: " + packagedFile.getAbsolutePath());
			
			CollectSurvey survey = surveyManager.getByUri(selectedSurveyUri);

			DataRestoreSummaryJob job = jobManager.createJob(DataRestoreSummaryJob.class);
			job.setCompleteSummary(completeSummary);
			job.setFile(packagedFile);
			job.setPublishedSurvey(survey);

			resetJobs();
			this.summaryJob = job;
			
			jobManager.start(job);
		} else {
			log.warn("Summary creation job already running");
		}
		return getCurrentJob();
	}
	
	@Secured("ROLE_ADMIN")
	public JobProxy startImport(List<Integer> entryIdsToImport, boolean validateRecords, boolean processInTransaction) throws Exception {
		if ( dataRestoreJob == null || ! dataRestoreJob.isRunning() ) {
			log.info("Starting data restore");

			DataRestoreJob job;
			if (processInTransaction) {
				job = jobManager.createJob(TransactionalDataRestoreJob.JOB_NAME, TransactionalDataRestoreJob.class);				
			} else {
				job = jobManager.createJob(DataRestoreJob.JOB_NAME, DataRestoreJob.class);
			}
			job.setFile(packagedFile);
			RecordProvider recordProvider = summaryJob.getRecordProvider();
			job.setRecordProvider(recordProvider);
			((XMLParsingRecordProvider) recordProvider).setValidateRecords(validateRecords);
			job.setPackagedSurvey(summaryJob.getPackagedSurvey());
			job.setPublishedSurvey(summaryJob.getPublishedSurvey());
			job.setEntryIdsToImport(entryIdsToImport);
			job.setRestoreUploadedFiles(true);
			job.setValidateRecords(validateRecords);
			
			resetJobs();
			this.dataRestoreJob = job;
			
			jobManager.start(job);
		} else {
			log.warn("Data restore job already running");
		}
		return getCurrentJob();
	}

	@Secured("ROLE_ADMIN")
	public JobProxy getCurrentJob() {
		JobProxy proxy = null;
		if ( summaryJob != null ) {
			proxy = new DataRestoreSummaryJobProxy(summaryJob);
		} else if ( dataRestoreJob != null) {
			proxy = new DataRestoreJobProxy(dataRestoreJob);
		}
		return proxy;
	}
	
	@Secured("ROLE_ADMIN")
	public DataImportSummaryProxy getSummary() {
		if ( summaryJob != null ) {
			DataImportSummary summary = summaryJob.getSummary();
			SessionState sessionState = sessionManager.getSessionState();
			Locale locale = sessionState.getLocale();
			DataImportSummaryProxy proxy = new DataImportSummaryProxy(summary, locale);
			return proxy;
		} else {
			return null;
		}
	}

	@Secured("ROLE_ADMIN")
	public void cancel() {
		if ( summaryJob != null ) {
			summaryJob.abort();
		} else if ( dataRestoreJob != null ) {
			dataRestoreJob.abort();
		}
	}

	private void resetJobs() {
		this.summaryJob = null;
		this.dataRestoreJob = null;
	}
	
}
