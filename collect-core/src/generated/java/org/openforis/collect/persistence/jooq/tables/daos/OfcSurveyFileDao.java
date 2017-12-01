/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.openforis.collect.persistence.jooq.tables.OfcSurveyFile;
import org.openforis.collect.persistence.jooq.tables.records.OfcSurveyFileRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OfcSurveyFileDao extends DAOImpl<OfcSurveyFileRecord, org.openforis.collect.persistence.jooq.tables.pojos.OfcSurveyFile, Integer> {

	/**
	 * Create a new OfcSurveyFileDao without any configuration
	 */
	public OfcSurveyFileDao() {
		super(OfcSurveyFile.OFC_SURVEY_FILE, org.openforis.collect.persistence.jooq.tables.pojos.OfcSurveyFile.class);
	}

	/**
	 * Create a new OfcSurveyFileDao with an attached configuration
	 */
	public OfcSurveyFileDao(Configuration configuration) {
		super(OfcSurveyFile.OFC_SURVEY_FILE, org.openforis.collect.persistence.jooq.tables.pojos.OfcSurveyFile.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer getId(org.openforis.collect.persistence.jooq.tables.pojos.OfcSurveyFile object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>id IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcSurveyFile> fetchById(Integer... values) {
		return fetch(OfcSurveyFile.OFC_SURVEY_FILE.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>id = value</code>
	 */
	public org.openforis.collect.persistence.jooq.tables.pojos.OfcSurveyFile fetchOneById(Integer value) {
		return fetchOne(OfcSurveyFile.OFC_SURVEY_FILE.ID, value);
	}

	/**
	 * Fetch records that have <code>survey_id IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcSurveyFile> fetchBySurveyId(Integer... values) {
		return fetch(OfcSurveyFile.OFC_SURVEY_FILE.SURVEY_ID, values);
	}

	/**
	 * Fetch records that have <code>type IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcSurveyFile> fetchByType(String... values) {
		return fetch(OfcSurveyFile.OFC_SURVEY_FILE.TYPE, values);
	}

	/**
	 * Fetch records that have <code>filename IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcSurveyFile> fetchByFilename(String... values) {
		return fetch(OfcSurveyFile.OFC_SURVEY_FILE.FILENAME, values);
	}

	/**
	 * Fetch records that have <code>content IN (values)</code>
	 */
	public List<org.openforis.collect.persistence.jooq.tables.pojos.OfcSurveyFile> fetchByContent(byte[]... values) {
		return fetch(OfcSurveyFile.OFC_SURVEY_FILE.CONTENT, values);
	}
}
