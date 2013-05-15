package org.openforis.collect.relational.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openforis.collect.relational.CollectRdbException;
import org.openforis.idm.metamodel.CodeList;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.Survey;
import org.openforis.idm.model.Entity;
import org.openforis.idm.model.Record;

/**
 * 
 * @author G. Miceli
 *
 */
public final class RelationalSchema {

	private Survey survey;
	private String name;
	private LinkedHashMap<String, Table<?>> tables;
	private Map<CodeListTableKey, CodeListTable> codeListTables;
	private Map<String, DataTable> rootDataTables;
	
	RelationalSchema(Survey survey, String name) throws CollectRdbException {
		this.survey = survey;
		this.name = name;
		this.tables = new LinkedHashMap<String, Table<?>>();
		this.codeListTables = new HashMap<CodeListTableKey, CodeListTable>();
		this.rootDataTables = new HashMap<String, DataTable>();
	}

	public Survey getSurvey() {
		return survey;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Table<?>> getTables() {
		List<Table<?>> tableList = new ArrayList<Table<?>>(tables.values());
		return Collections.unmodifiableList(tableList);
	}
	
	public List<CodeListTable> getCodeListTables() {
		List<CodeListTable> tableList = new ArrayList<CodeListTable>(codeListTables.values());
		return Collections.unmodifiableList(tableList);
	}
	
	public CodeListTable getCodeListTable(CodeList list, Integer levelIdx) {
		CodeListTableKey key = new CodeListTableKey(list.getId(), levelIdx);
		return codeListTables.get(key);
	}

	public Dataset getReferenceData() {
		Dataset dataset = new Dataset();
		List<CodeListTable> codeListTables = getCodeListTables();
		for (CodeListTable codeListTable : codeListTables) {
			Dataset codeListDataset = codeListTable.extractData();
			dataset.addRows(codeListDataset.getRows());
		}
		return dataset;
	}
	
	public boolean containsTable(String name) {
		return tables.containsKey(name);
	}

	/**
	 * Adds a table to the schema.  If table with the same name 
	 * already exists it will be replaced.
	 * @param table
	 */
	void addTable(Table<?> table) {
		String name = table.getName();
		tables.put(name, table);
		if ( table instanceof DataTable ) {
			DataTable dataTable = (DataTable) table;
			if ( dataTable.getParent() == null ) {
				NodeDefinition defn = dataTable.getNodeDefinition();
				rootDataTables.put(defn.getName(), dataTable);
			}
		} else if ( table instanceof CodeListTable ) {
			CodeListTable codeListTable = (CodeListTable) table;
			CodeList codeList = codeListTable.getCodeList();
			CodeListTableKey key = new CodeListTableKey(codeList.getId(), codeListTable.getLevelIdx());
			codeListTables.put(key, codeListTable);
		}
	}

	public Dataset createDataset(Record record) {
		Entity root = record.getRootEntity();
		EntityDefinition rootDefn = root.getDefinition();
		String name = rootDefn.getName();
		DataTable dataTable = rootDataTables.get(name);
		if ( dataTable == null ) {
			throw new IllegalArgumentException("Invalid root entity "+name);
		}
		return dataTable.extractData(root);
	}
	
	private static class CodeListTableKey {
		private int listId;
		private Integer levelIdx;
		
		CodeListTableKey(int listId, Integer levelIdx) {
			this.listId = listId;
			this.levelIdx = levelIdx;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((levelIdx == null) ? 0 : levelIdx.hashCode());
			result = prime * result + listId;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CodeListTableKey other = (CodeListTableKey) obj;
			if (levelIdx == null) {
				if (other.levelIdx != null)
					return false;
			} else if (!levelIdx.equals(other.levelIdx))
				return false;
			if (listId != other.listId)
				return false;
			return true;
		}
		
	}

	public void print(PrintStream out) {
		for (Table<?> table : getTables()) {
			table.print(System.out);
		}
	}
}