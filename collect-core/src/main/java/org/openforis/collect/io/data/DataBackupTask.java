package org.openforis.collect.io.data;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.collections.CollectionUtils;
import org.openforis.collect.io.data.BackupDataExtractor.BackupRecordEntry;
import org.openforis.collect.manager.RecordManager;
import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.CollectRecord.Step;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.model.RecordFilter;
import org.openforis.collect.persistence.xml.DataMarshaller;
import org.openforis.concurrency.Task;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author S. Ricci
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DataBackupTask extends Task {

	private RecordManager recordManager;
	private DataMarshaller dataMarshaller;
	
	//input
	private ZipOutputStream zipOutputStream;
	private CollectSurvey survey;
	private Step[] steps;
	private RecordFilter recordFilter;
	private List<DataBackupError> errors;
	
	public DataBackupTask() {
		super();
		//export all steps by default
		this.steps = Step.values();
		this.errors = new ArrayList<DataBackupError>();
	}
	
	@Override
	protected void createInternalVariables() throws Throwable {
		if ( recordFilter == null ) {
			recordFilter = new RecordFilter(survey);
		}
		super.createInternalVariables();
	}
	
	@Override
	protected long countTotalItems() {
		int count = 0;
		List<CollectRecord> recordSummaries = recordManager.loadSummaries(recordFilter);
		if ( CollectionUtils.isNotEmpty(recordSummaries) && steps != null && steps.length > 0 ) {
			for (CollectRecord summary : recordSummaries) {
				for (Step step : steps) {
					if ( step.getStepNumber() <= summary.getStep().getStepNumber() ) {
						count ++;
					}
				}
			}
		}
		return count;
	}

	@Override
	protected void execute() throws Throwable {
		List<CollectRecord> recordSummaries = recordManager.loadSummaries(recordFilter);
		if ( CollectionUtils.isNotEmpty(recordSummaries) && steps != null && steps.length > 0 ) {
			for (CollectRecord summary : recordSummaries) {
				for (Step step : steps) {
					if ( isRunning() ) {
						if ( step.getStepNumber() <= summary.getStep().getStepNumber() ) {
							backup(summary, step);
							incrementProcessedItems();
						}
					} else {
						break;
					}
				}
			}
		}
	}

	private void backup(CollectRecord summary, Step step) {
		Integer id = summary.getId();
		try {
			CollectRecord record = recordManager.load(survey, id, step, false);
			BackupRecordEntry recordEntry = new BackupRecordEntry(step, id);
			ZipEntry entry = new ZipEntry(recordEntry.getName());
			zipOutputStream.putNextEntry(entry);
			OutputStreamWriter writer = new OutputStreamWriter(zipOutputStream);
			dataMarshaller.write(record, writer);
			zipOutputStream.closeEntry();
		} catch (Exception e) {
			DataBackupError error = new DataBackupError(summary.getId(), summary.getRootEntityKeyValues(), 
					summary.getStep(), e.getMessage());
			errors.add(error);
			log().error(error.toString(), e);
		}
	}
	
	public RecordManager getRecordManager() {
		return recordManager;
	}
	
	public void setRecordManager(RecordManager recordManager) {
		this.recordManager = recordManager;
	}
	
	public DataMarshaller getDataMarshaller() {
		return dataMarshaller;
	}
	
	public void setDataMarshaller(DataMarshaller dataMarshaller) {
		this.dataMarshaller = dataMarshaller;
	}
	
	public RecordFilter getRecordFilter() {
		return recordFilter;
	}
	
	public void setRecordFilter(RecordFilter recordFilter) {
		this.recordFilter = recordFilter;
	}
	
	public CollectSurvey getSurvey() {
		return survey;
	}
	
	public void setSurvey(CollectSurvey survey) {
		this.survey = survey;
	}
	
	public Step[] getSteps() {
		return steps;
	}
	
	public void setSteps(Step[] steps) {
		this.steps = steps;
	}

	public ZipOutputStream getZipOutputStream() {
		return zipOutputStream;
	}

	public void setZipOutputStream(ZipOutputStream zipOutputStream) {
		this.zipOutputStream = zipOutputStream;
	}

	public List<DataBackupError> getErrors() {
		return errors;
	}
	
}
