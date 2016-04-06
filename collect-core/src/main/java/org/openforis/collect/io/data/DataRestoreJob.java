/**
 * 
 */
package org.openforis.collect.io.data;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.openforis.collect.io.SurveyBackupJob;
import org.openforis.collect.io.SurveyBackupJob.OutputFormat;
import org.openforis.collect.io.data.backup.BackupStorageManager;
import org.openforis.collect.io.data.restore.RestoredBackupStorageManager;
import org.openforis.collect.manager.RecordFileManager;
import org.openforis.collect.manager.RecordManager;
import org.openforis.collect.manager.UserManager;
import org.openforis.collect.model.RecordFilter;
import org.openforis.concurrency.Task;
import org.openforis.concurrency.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author S. Ricci
 *
 */
@Component(value=DataRestoreJob.JOB_NAME)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DataRestoreJob extends DataRestoreBaseJob {
	
	public static final String JOB_NAME = "dataRestoreJob";
	
	@Autowired
	private RecordFileManager recordFileManager;
	@Autowired
	protected RestoredBackupStorageManager restoredBackupStorageManager;
	@Autowired
	protected BackupStorageManager backupStorageManager;

	//input parameters
	private boolean overwriteAll;
	private boolean restoreUploadedFiles;
	private List<Integer> entryIdsToImport; //ignored when overwriteAll is true
	private boolean storeRestoredFile;
	private File tempFile;
	
	//output
	private List<RecordImportError> errors;
		
	private boolean deleteAllRecordsBeforeRestore = false;

	@Override
	public void createInternalVariables() throws Throwable {
		super.createInternalVariables();
		this.errors = new ArrayList<RecordImportError>();
	}
	
	@Override
	protected void buildTasks() throws Throwable {
		super.buildTasks();
		if (storeRestoredFile) {
			if (isBackupNeeded()) {
				addTask(SurveyBackupJob.class);
			}
			addTask(new StoreBackupFileTask());
		}
		if (deleteAllRecordsBeforeRestore) {
			addTask(new DeleteRecordsTask());
		}
		addTask(DataRestoreTask.class);
		if ( restoreUploadedFiles && isUploadedFilesIncluded() ) {
			addTask(RecordFileRestoreTask.class);
		}
	}
	
	private boolean isBackupNeeded() {
		if (newSurvey) {
			return false;
		}
		Date lastBackupDate = backupStorageManager.getLastBackupDate(surveyName);
		RecordFilter recordFilter = new RecordFilter(publishedSurvey);
		recordFilter.setModifiedSince(lastBackupDate);
		return recordManager.countRecords(recordFilter) > 0 || 
				(lastBackupDate != null && publishedSurvey.getModifiedDate().after(lastBackupDate));
	}

	private boolean isUploadedFilesIncluded() throws IOException {
		List<String> dataEntries = backupFileExtractor.listEntriesInPath(SurveyBackupJob.UPLOADED_FILES_FOLDER);
		return ! dataEntries.isEmpty();
	}

	@Override
	protected void initializeTask(Worker task) {
		if (task instanceof SurveyBackupJob) {
			SurveyBackupJob t = (SurveyBackupJob) task;
			t.setFull(true);
			t.setIncludeData(true);
			t.setIncludeRecordFiles(true);
			t.setOutputFormat(OutputFormat.DESKTOP_FULL);
			t.setRecordFilter(new RecordFilter(publishedSurvey));
			t.setSurvey(publishedSurvey);
		} else if ( task instanceof DataRestoreTask ) {
			DataRestoreTask t = (DataRestoreTask) task;
			t.setRecordManager(recordManager);
			t.setUserManager(userManager);
			t.setRecordProvider(recordProvider);
			t.setOverwriteAll(overwriteAll);
			t.setEntryIdsToImport(entryIdsToImport);
		} else if ( task instanceof RecordFileRestoreTask ) {
			RecordFileRestoreTask t = (RecordFileRestoreTask) task;
			t.setRecordManager(recordManager);
			t.setRecordFileManager(recordFileManager);
			t.setFile(file);
			t.setOldBackupFormat(oldBackupFormat);
			t.setOverwriteAll(overwriteAll);
			t.setEntryIdsToImport(entryIdsToImport);
			t.setSurvey(publishedSurvey);
		}
		super.initializeTask(task);
	}
	
	@Override
	protected void onTaskCompleted(Worker task) {
		super.onTaskCompleted(task);
		if (task instanceof DataRestoreTask) {
			this.errors.addAll(((DataRestoreTask) task).getErrors());
		}
	}
	
	@Override
	protected void onCompleted() {
		super.onCompleted();
		if (storeRestoredFile) {
			restoredBackupStorageManager.moveToFinalFolder(surveyName, tempFile);
		}
	}
	
	@Override
	protected void onEnd() {
		super.onEnd();
		if (recordProvider instanceof Closeable) {
			IOUtils.closeQuietly((Closeable) recordProvider);
		}
	}

	public RecordManager getRecordManager() {
		return recordManager;
	}

	public void setRecordManager(RecordManager recordManager) {
		this.recordManager = recordManager;
	}
	
	public RecordFileManager getRecordFileManager() {
		return recordFileManager;
	}

	public void setRecordFileManager(RecordFileManager recordFileManager) {
		this.recordFileManager = recordFileManager;
	}
	
	public UserManager getUserManager() {
		return userManager;
	}
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public List<Integer> getEntryIdsToImport() {
		return entryIdsToImport;
	}
	
	public void setEntryIdsToImport(List<Integer> entryIdsToImport) {
		this.entryIdsToImport = entryIdsToImport;
	}
	
	public boolean isRestoreUploadedFiles() {
		return restoreUploadedFiles;
	}
	
	public void setRestoreUploadedFiles(boolean restoreUploadedFiles) {
		this.restoreUploadedFiles = restoreUploadedFiles;
	}

	public boolean isOverwriteAll() {
		return overwriteAll;
	}

	public void setOverwriteAll(boolean overwriteAll) {
		this.overwriteAll = overwriteAll;
	}
	
	public boolean isStoreRestoredFile() {
		return storeRestoredFile;
	}
	
	public void setStoreRestoredFile(boolean storeRestoredFile) {
		this.storeRestoredFile = storeRestoredFile;
	}
	
	public List<RecordImportError> getErrors() {
		return errors;
	}

	public void setDeleteAllRecordsBeforeRestore(boolean deleteAllRecords) {
		this.deleteAllRecordsBeforeRestore = deleteAllRecords;
	}

	private class StoreBackupFileTask extends Task {
		protected void execute() throws Throwable {
			DataRestoreJob.this.tempFile = restoredBackupStorageManager.storeTemporaryFile(surveyName, file);
		}
		
	}
	
	private class DeleteRecordsTask extends Task {
		protected void execute() throws Throwable {
			recordManager.deleteBySurvey(publishedSurvey.getId());
		}
	}

}
