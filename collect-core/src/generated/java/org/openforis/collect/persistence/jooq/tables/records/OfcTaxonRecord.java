/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;
import org.openforis.collect.persistence.jooq.tables.OfcTaxon;


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
public class OfcTaxonRecord extends UpdatableRecordImpl<OfcTaxonRecord> implements Record8<Integer, Integer, String, String, String, Integer, Integer, Integer> {

	private static final long serialVersionUID = 1782174357;

	/**
	 * Setter for <code>collect.ofc_taxon.id</code>.
	 */
	public void setId(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>collect.ofc_taxon.id</code>.
	 */
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>collect.ofc_taxon.taxon_id</code>.
	 */
	public void setTaxonId(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>collect.ofc_taxon.taxon_id</code>.
	 */
	public Integer getTaxonId() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>collect.ofc_taxon.code</code>.
	 */
	public void setCode(String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>collect.ofc_taxon.code</code>.
	 */
	public String getCode() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>collect.ofc_taxon.scientific_name</code>.
	 */
	public void setScientificName(String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>collect.ofc_taxon.scientific_name</code>.
	 */
	public String getScientificName() {
		return (String) getValue(3);
	}

	/**
	 * Setter for <code>collect.ofc_taxon.taxon_rank</code>.
	 */
	public void setTaxonRank(String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>collect.ofc_taxon.taxon_rank</code>.
	 */
	public String getTaxonRank() {
		return (String) getValue(4);
	}

	/**
	 * Setter for <code>collect.ofc_taxon.taxonomy_id</code>.
	 */
	public void setTaxonomyId(Integer value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>collect.ofc_taxon.taxonomy_id</code>.
	 */
	public Integer getTaxonomyId() {
		return (Integer) getValue(5);
	}

	/**
	 * Setter for <code>collect.ofc_taxon.step</code>.
	 */
	public void setStep(Integer value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>collect.ofc_taxon.step</code>.
	 */
	public Integer getStep() {
		return (Integer) getValue(6);
	}

	/**
	 * Setter for <code>collect.ofc_taxon.parent_id</code>.
	 */
	public void setParentId(Integer value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>collect.ofc_taxon.parent_id</code>.
	 */
	public Integer getParentId() {
		return (Integer) getValue(7);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record8 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row8<Integer, Integer, String, String, String, Integer, Integer, Integer> fieldsRow() {
		return (Row8) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row8<Integer, Integer, String, String, String, Integer, Integer, Integer> valuesRow() {
		return (Row8) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return OfcTaxon.OFC_TAXON.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return OfcTaxon.OFC_TAXON.TAXON_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return OfcTaxon.OFC_TAXON.CODE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field4() {
		return OfcTaxon.OFC_TAXON.SCIENTIFIC_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field5() {
		return OfcTaxon.OFC_TAXON.TAXON_RANK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field6() {
		return OfcTaxon.OFC_TAXON.TAXONOMY_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field7() {
		return OfcTaxon.OFC_TAXON.STEP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field8() {
		return OfcTaxon.OFC_TAXON.PARENT_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getTaxonId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value4() {
		return getScientificName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value5() {
		return getTaxonRank();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value6() {
		return getTaxonomyId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value7() {
		return getStep();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value8() {
		return getParentId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcTaxonRecord value1(Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcTaxonRecord value2(Integer value) {
		setTaxonId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcTaxonRecord value3(String value) {
		setCode(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcTaxonRecord value4(String value) {
		setScientificName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcTaxonRecord value5(String value) {
		setTaxonRank(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcTaxonRecord value6(Integer value) {
		setTaxonomyId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcTaxonRecord value7(Integer value) {
		setStep(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcTaxonRecord value8(Integer value) {
		setParentId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfcTaxonRecord values(Integer value1, Integer value2, String value3, String value4, String value5, Integer value6, Integer value7, Integer value8) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		value6(value6);
		value7(value7);
		value8(value8);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached OfcTaxonRecord
	 */
	public OfcTaxonRecord() {
		super(OfcTaxon.OFC_TAXON);
	}

	/**
	 * Create a detached, initialised OfcTaxonRecord
	 */
	public OfcTaxonRecord(Integer id, Integer taxonId, String code, String scientificName, String taxonRank, Integer taxonomyId, Integer step, Integer parentId) {
		super(OfcTaxon.OFC_TAXON);

		setValue(0, id);
		setValue(1, taxonId);
		setValue(2, code);
		setValue(3, scientificName);
		setValue(4, taxonRank);
		setValue(5, taxonomyId);
		setValue(6, step);
		setValue(7, parentId);
	}
}
