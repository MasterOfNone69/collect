package org.openforis.collect.designer.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openforis.collect.designer.form.TaxonomyFormObject;
import org.openforis.collect.manager.SpeciesManager;
import org.openforis.collect.model.CollectTaxonomy;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.NodeDefinitionVisitor;
import org.openforis.idm.metamodel.TaxonAttributeDefinition;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class SpeciesListsVM extends SurveyObjectBaseVM<CollectTaxonomy> {

	private static final String SPECIES_LISTS_UPDATED_GLOBAL_COMMAND = "speciesListsUpdated";

	@WireVariable
	private SpeciesManager speciesManager;
	
	@Override
	protected List<CollectTaxonomy> getItemsInternal() {
		return speciesManager.loadTaxonomiesBySurvey(getSurvey());
	}

	@Override
	protected void moveSelectedItemInSurvey(int indexTo) {
	}

	@Override
	protected TaxonomyFormObject createFormObject() {
		return new TaxonomyFormObject();
	}

	@Override
	protected CollectTaxonomy createItemInstance() {
		return new CollectTaxonomy();
	}

	@Override
	protected void addNewItemToSurvey() {
		speciesManager.save(editedItem);
	}

	@Override
	protected void deleteItemFromSurvey(CollectTaxonomy item) {
		speciesManager.delete(item);
		dispatchSpeciesListsUpdatedCommand();
		SurveyEditVM.dispatchSurveySaveCommand();
	}
	
	public static void dispatchSpeciesListsUpdatedCommand() {
		BindUtils.postGlobalCommand(null, null, SPECIES_LISTS_UPDATED_GLOBAL_COMMAND, null);
	}
	
	public String getWarnings(CollectTaxonomy taxonomy) {
		return null;
	}
	
	public boolean hasWarnings(CollectTaxonomy taxonomy) {
		return false;
	}

	@Command
	public void deleteSpeciesList(CollectTaxonomy taxonomy) {
		List<NodeDefinition> references = getReferences(taxonomy);
		if ( references.isEmpty() ) {
			super.deleteItem(taxonomy);
		} else {
		}
	}
	
	private List<NodeDefinition> getReferences(CollectTaxonomy item) {
		List<NodeDefinition> references = new ArrayList<NodeDefinition>();
		survey.getSchema().traverse(new NodeDefinitionVisitor() {
			public void visit(NodeDefinition defn) {
				if ( defn instanceof TaxonAttributeDefinition ) {
					String taxonomyName = ((TaxonAttributeDefinition) defn).getTaxonomy();
					if ( StringUtils.equals(taxonomyName, item.getName()) ) {
						references.add(defn);
					}
				}
			}
		});
		return references;
	}
}
