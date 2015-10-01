package org.openforis.collect.datacleansing;

import java.util.UUID;

import org.openforis.collect.model.CollectSurvey;
import org.openforis.idm.metamodel.AttributeDefinition;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.PersistedSurveyObject;

/**
 * 
 * @author S. Ricci
 *
 */
public class DataQuery extends PersistedSurveyObject {

	private static final long serialVersionUID = 1L;
	
	private String title;
	private String description;
	private int entityDefinitionId;
	private int attributeDefinitionId;
	private String conditions;

	public DataQuery(CollectSurvey survey) {
		super(survey);
	}
	
	public DataQuery(CollectSurvey survey, UUID uuid) {
		super(survey, uuid);
	}

	public EntityDefinition getEntityDefinition() {
		NodeDefinition def = getSurvey().getSchema().getDefinitionById(entityDefinitionId);
		return (EntityDefinition) def;
	}
	
	public AttributeDefinition getAttributeDefinition() {
		NodeDefinition def = getSurvey().getSchema().getDefinitionById(attributeDefinitionId);
		return (AttributeDefinition) def;
	}
	
	public void setAttributeDefinition(AttributeDefinition def) {
		attributeDefinitionId = def.getId();
	}
	
	public void setEntityDefinition(EntityDefinition def) {
		entityDefinitionId = def.getId();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEntityDefinitionId() {
		return entityDefinitionId;
	}

	public void setEntityDefinitionId(int entityDefinitionId) {
		this.entityDefinitionId = entityDefinitionId;
	}

	public int getAttributeDefinitionId() {
		return attributeDefinitionId;
	}

	public void setAttributeDefinitionId(int attributeDefinitionId) {
		this.attributeDefinitionId = attributeDefinitionId;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	@Override
	public boolean deepEquals(Object obj, boolean ignoreId) {
		if (this == obj)
			return true;
		if (!super.deepEquals(obj, ignoreId))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataQuery other = (DataQuery) obj;
		if (attributeDefinitionId != other.attributeDefinitionId)
			return false;
		if (conditions == null) {
			if (other.conditions != null)
				return false;
		} else if (!conditions.equals(other.conditions))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (entityDefinitionId != other.entityDefinitionId)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	

}