/**
 * 
 */
package org.openforis.collect.designer.viewmodel;

import org.openforis.collect.designer.form.NodeDefinitionFormObject;
import org.openforis.collect.designer.form.SurveyObjectFormObject;
import org.openforis.collect.designer.metamodel.NodeType;
import org.openforis.idm.metamodel.EntityDefinition;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

/**
 * @author S. Ricci
 *
 */
public class EntityDefinitionVM extends NodeDefinitionVM<EntityDefinition> {

	@Init(superclass=false)
	public void init(@ExecutionArgParam("parentEntity") EntityDefinition parentEntity, 
			@ExecutionArgParam("item") EntityDefinition attributeDefn, 
			@ExecutionArgParam("newItem") Boolean newItem,
			@ExecutionArgParam("doNotCommitChangesImmediately") Boolean doNotCommitChangesImmediately) {
		super.initInternal(parentEntity, attributeDefn, newItem);
		boolean doNotCommitChangesImmediatelyBool = doNotCommitChangesImmediately == null ? false: doNotCommitChangesImmediately.booleanValue();
		this.commitChangesOnApply = ! doNotCommitChangesImmediatelyBool;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected SurveyObjectFormObject<EntityDefinition> createFormObject() {
		return (NodeDefinitionFormObject<EntityDefinition>) 
				NodeDefinitionFormObject.newInstance(parentEntity, NodeType.ENTITY, null);
	}
	
}
