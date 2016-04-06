package org.openforis.collect.io.metadata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;
import org.openforis.collect.designer.metamodel.AttributeType;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.commons.io.csv.CsvWriter;
import org.openforis.concurrency.Job;
import org.openforis.concurrency.Task;
import org.openforis.idm.metamodel.AttributeDefinition;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.NodeDefinitionVisitor;
import org.openforis.idm.metamodel.NodeLabel.Type;
import org.openforis.idm.metamodel.Schema;

/**
 * 
 * @author S. Ricci
 *
 */
public class SchemaSummaryCSVExportJob extends Job {

	private CollectSurvey survey;
	private File outputFile;
	
	@Override
	protected void createInternalVariables() throws Throwable {
		super.createInternalVariables();
		outputFile = File.createTempFile("collect_schema_export", ".csv");
	}
	
	@Override
	protected void buildTasks() throws Throwable {
		Task task = new Task() {
			@Override
			protected void execute() throws Throwable {
				FileOutputStream out = new FileOutputStream(outputFile);
				final CsvWriter csvWriter = new CsvWriter(new BufferedWriter(new OutputStreamWriter(out)), ',', '"');
				try {
					csvWriter.writeHeaders(new String[] {"id", "path", "type", "attribute_type", "label"});
					
					Schema schema = survey.getSchema();
					schema.traverse(new NodeDefinitionVisitor() {
						@Override
						public void visit(NodeDefinition nodeDefn) {
							csvWriter.writeNext(new String[] {
									Integer.toString(nodeDefn.getId()), 
									nodeDefn.getPath(),
									nodeDefn instanceof EntityDefinition ? "entity": "attribute",
									nodeDefn instanceof AttributeDefinition ? AttributeType.valueOf((AttributeDefinition) nodeDefn).getLabel(): "",
									nodeDefn.getLabel(Type.INSTANCE)
								});
						}
					});
				} finally {
					IOUtils.closeQuietly(csvWriter);
				}
			}
		};
		addTask(task);
	}
	
	public void setSurvey(CollectSurvey survey) {
		this.survey = survey;
	}
	
	public File getOutputFile() {
		return outputFile;
	}
	
}