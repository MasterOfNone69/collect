/**
 * 
 */
package org.openforis.collect.persistence.jooq;

import java.util.List;

import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.idm.metamodel.PersistedSurveyObject;

/**
 * @author S. Ricci
 *
 */
public abstract class SurveyObjectMappingJooqDaoSupport<T extends PersistedSurveyObject, C extends SurveyObjectMappingDSLContext<T>> 
	extends MappingJooqDaoSupport<T, C>  {

	public SurveyObjectMappingJooqDaoSupport(Class<C> jooqFactoryClass) {
		super(jooqFactoryClass);
	}

	public T loadById(CollectSurvey survey, int id) {
		C dsl = dsl(survey);
		ResultQuery<?> selectQuery = dsl.selectByIdQuery(id);
		Record r = selectQuery.fetchOne();
		return r == null ? null : dsl.fromRecord(r);
	}
	
	public abstract List<T> loadBySurvey(CollectSurvey survey);
	
	@Override
	public void insert(T item) {
		C dsl = dsl((CollectSurvey) item.getSurvey());
		dsl.insertQuery(item).execute();
	}
	
	@Override
	public void update(T item) {
		C dsl = dsl((CollectSurvey) item.getSurvey());
		dsl.updateQuery(item).execute();
	}
	
	protected C dsl(CollectSurvey survey) {
		try {
			return jooqFactoryClass.getConstructor(Configuration.class, CollectSurvey.class)
					.newInstance(getConfiguration(), survey);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create " + jooqFactoryClass, e);
		}
	}

	
}
