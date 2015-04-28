/**
 * 
 */
package org.openforis.collect.datacleansing.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openforis.collect.datacleansing.DataErrorQuery;
import org.openforis.collect.datacleansing.DataErrorReport;
import org.openforis.collect.datacleansing.DataErrorReportItem;
import org.openforis.collect.datacleansing.persistence.DataErrorReportDao;
import org.openforis.collect.datacleansing.persistence.DataErrorReportItemDao;
import org.openforis.collect.manager.AbstractSurveyObjectManager;
import org.openforis.collect.manager.RecordManager;
import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.CollectSurvey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author S. Ricci
 *
 */
@Component
public class DataErrorReportManager extends AbstractSurveyObjectManager<DataErrorReport, DataErrorReportDao> {

	@Autowired
	private RecordManager recordManager;
	@Autowired
	private DataErrorQueryManager errorQueryManager;
	@Autowired
	private DataErrorReportItemDao errorReportItemDao;

	@Override
	@Autowired
	@Qualifier("dataErrorReportDao")
	public void setDao(DataErrorReportDao dao) {
		super.setDao(dao);
	}
	
	public void saveItems(DataErrorReport report, List<DataErrorReportItem> items) {
		errorReportItemDao.insert(report, items);
	}
	
	public List<DataErrorReportItem> loadItems(DataErrorReport report, int offset, int limit) {
		CollectSurvey survey = report.getSurvey();
		Map<Integer, CollectRecord> recordCache = new HashMap<Integer, CollectRecord>();
		List<DataErrorReportItem> items = errorReportItemDao.loadByReport(report, offset, limit);
		for (DataErrorReportItem item : items) {
			int recordId = item.getRecordId();
			CollectRecord record = recordCache.get(recordId);
			if (record == null) {
				record = recordManager.load(survey, recordId);
				recordCache.put(recordId, record);
			}
			item.setRecord(record);
		}
		return items;
	}

	@Override
	protected void initializeItem(DataErrorReport i) {
		super.initializeItem(i);
		initQuery(i);
	}
	
	private void initQuery(DataErrorReport report) {
		DataErrorQuery query = errorQueryManager.loadById((CollectSurvey) report.getSurvey(), report.getQueryId());
		report.setQuery(query);
	}
	
}
