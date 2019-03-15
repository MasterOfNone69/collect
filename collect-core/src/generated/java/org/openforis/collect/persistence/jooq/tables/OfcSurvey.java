/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.openforis.collect.persistence.jooq.Collect;
import org.openforis.collect.persistence.jooq.Keys;
import org.openforis.collect.persistence.jooq.tables.records.OfcSurveyRecord;


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
public class OfcSurvey extends TableImpl<OfcSurveyRecord> {

	private static final long serialVersionUID = -447434186;

	/**
	 * The reference instance of <code>collect.ofc_survey</code>
	 */
	public static final OfcSurvey OFC_SURVEY = new OfcSurvey();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<OfcSurveyRecord> getRecordType() {
		return OfcSurveyRecord.class;
	}

	/**
	 * The column <code>collect.ofc_survey.id</code>.
	 */
	public final TableField<OfcSurveyRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_survey.name</code>.
	 */
	public final TableField<OfcSurveyRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_survey.uri</code>.
	 */
	public final TableField<OfcSurveyRecord, String> URI = createField("uri", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_survey.idml</code>.
	 */
	public final TableField<OfcSurveyRecord, String> IDML = createField("idml", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

	/**
	 * The column <code>collect.ofc_survey.target</code>.
	 */
	public final TableField<OfcSurveyRecord, String> TARGET = createField("target", org.jooq.impl.SQLDataType.VARCHAR.length(5).defaulted(true), this, "");

	/**
	 * The column <code>collect.ofc_survey.date_created</code>.
	 */
	public final TableField<OfcSurveyRecord, Timestamp> DATE_CREATED = createField("date_created", org.jooq.impl.SQLDataType.TIMESTAMP.defaulted(true), this, "");

	/**
	 * The column <code>collect.ofc_survey.date_modified</code>.
	 */
	public final TableField<OfcSurveyRecord, Timestamp> DATE_MODIFIED = createField("date_modified", org.jooq.impl.SQLDataType.TIMESTAMP.defaulted(true), this, "");

	/**
	 * The column <code>collect.ofc_survey.collect_version</code>.
	 */
	public final TableField<OfcSurveyRecord, String> COLLECT_VERSION = createField("collect_version", org.jooq.impl.SQLDataType.VARCHAR.length(55).defaulted(true), this, "");

	/**
	 * The column <code>collect.ofc_survey.temporary</code>.
	 */
	public final TableField<OfcSurveyRecord, Boolean> TEMPORARY = createField("temporary", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>collect.ofc_survey.published_id</code>.
	 */
	public final TableField<OfcSurveyRecord, Integer> PUBLISHED_ID = createField("published_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>collect.ofc_survey.usergroup_id</code>.
	 */
	public final TableField<OfcSurveyRecord, Integer> USERGROUP_ID = createField("usergroup_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>collect.ofc_survey.availability</code>.
	 */
	public final TableField<OfcSurveyRecord, String> AVAILABILITY = createField("availability", org.jooq.impl.SQLDataType.CHAR.length(1), this, "");

	/**
	 * The column <code>collect.ofc_survey.title</code>.
	 */
	public final TableField<OfcSurveyRecord, String> TITLE = createField("title", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>collect.ofc_survey.langs</code>.
	 */
	public final TableField<OfcSurveyRecord, String> LANGS = createField("langs", org.jooq.impl.SQLDataType.VARCHAR.length(20), this, "");

	/**
	 * Create a <code>collect.ofc_survey</code> table reference
	 */
	public OfcSurvey() {
		this("ofc_survey", null);
	}

	/**
	 * Create an aliased <code>collect.ofc_survey</code> table reference
	 */
	public OfcSurvey(String alias) {
		this(alias, OFC_SURVEY);
	}

	private OfcSurvey(String alias, Table<OfcSurveyRecord> aliased) {
		this(alias, aliased, null);
	}

	private OfcSurvey(String alias, Table<OfcSurveyRecord> aliased, Field<?>[] parameters) {
		super(alias, Collect.COLLECT, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<OfcSurveyRecord> getPrimaryKey() {
		return Keys.OFC_SURVEY_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<OfcSurveyRecord>> getKeys() {
		return Arrays.<UniqueKey<OfcSurveyRecord>>asList(Keys.OFC_SURVEY_PKEY, Keys.OFC_SURVEY_NAME_KEY, Keys.OFC_SURVEY_URI_KEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<OfcSurveyRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<OfcSurveyRecord, ?>>asList(Keys.OFC_SURVEY__OFC_SURVEY_USERGROUP_FKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcSurvey as(String alias) {
		return new OfcSurvey(alias, this);
	}

	/**
	 * Rename this table
	 */
	public OfcSurvey rename(String name) {
		return new OfcSurvey(name, null);
	}
}
