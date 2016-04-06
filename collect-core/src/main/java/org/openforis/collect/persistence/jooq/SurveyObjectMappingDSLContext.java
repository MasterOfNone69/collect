/**
 * 
 */
package org.openforis.collect.persistence.jooq;

import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.Sequence;
import org.jooq.StoreQuery;
import org.jooq.TableField;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.idm.metamodel.PersistedSurveyObject;

/**
 * @author S. Ricci
 *
 */
public abstract class SurveyObjectMappingDSLContext<T extends PersistedSurveyObject> extends MappingDSLContext<T> {

	private static final long serialVersionUID = 1L;
	
	protected CollectSurvey survey;

	public SurveyObjectMappingDSLContext(Configuration config,
			TableField<?, Integer> idField,
			Sequence<? extends Number> idSequence, 
			Class<T> clazz, CollectSurvey survey) {
		super(config, idField, idSequence, clazz);
		this.survey = survey;
	}

	@Override
	protected void fromObject(T o, StoreQuery<?> q) {
		q.addValue(getIdField(), o.getId());
	}

	@Override
	protected void fromRecord(Record r, T o) {
		o.setId(r.getValue(getIdField()));
	}
	
	@Override
	protected void setId(T entity, int id) {
		entity.setId(id);
	}

	@Override
	protected Integer getId(T entity) {
		return entity.getId();
	}
	
	public CollectSurvey getSurvey() {
		return survey;
	}
	
}
