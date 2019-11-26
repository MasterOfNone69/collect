/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


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
public class OfcTaxonVernacularName implements Serializable {

	private static final long serialVersionUID = -1653439725;

	private Long    id;
	private String  vernacularName;
	private String  languageCode;
	private String  languageVariety;
	private Long    taxonId;
	private Integer step;
	private String  qualifier1;
	private String  qualifier2;
	private String  qualifier3;

	public OfcTaxonVernacularName() {}

	public OfcTaxonVernacularName(OfcTaxonVernacularName value) {
		this.id = value.id;
		this.vernacularName = value.vernacularName;
		this.languageCode = value.languageCode;
		this.languageVariety = value.languageVariety;
		this.taxonId = value.taxonId;
		this.step = value.step;
		this.qualifier1 = value.qualifier1;
		this.qualifier2 = value.qualifier2;
		this.qualifier3 = value.qualifier3;
	}

	public OfcTaxonVernacularName(
		Long    id,
		String  vernacularName,
		String  languageCode,
		String  languageVariety,
		Long    taxonId,
		Integer step,
		String  qualifier1,
		String  qualifier2,
		String  qualifier3
	) {
		this.id = id;
		this.vernacularName = vernacularName;
		this.languageCode = languageCode;
		this.languageVariety = languageVariety;
		this.taxonId = taxonId;
		this.step = step;
		this.qualifier1 = qualifier1;
		this.qualifier2 = qualifier2;
		this.qualifier3 = qualifier3;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVernacularName() {
		return this.vernacularName;
	}

	public void setVernacularName(String vernacularName) {
		this.vernacularName = vernacularName;
	}

	public String getLanguageCode() {
		return this.languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getLanguageVariety() {
		return this.languageVariety;
	}

	public void setLanguageVariety(String languageVariety) {
		this.languageVariety = languageVariety;
	}

	public Long getTaxonId() {
		return this.taxonId;
	}

	public void setTaxonId(Long taxonId) {
		this.taxonId = taxonId;
	}

	public Integer getStep() {
		return this.step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getQualifier1() {
		return this.qualifier1;
	}

	public void setQualifier1(String qualifier1) {
		this.qualifier1 = qualifier1;
	}

	public String getQualifier2() {
		return this.qualifier2;
	}

	public void setQualifier2(String qualifier2) {
		this.qualifier2 = qualifier2;
	}

	public String getQualifier3() {
		return this.qualifier3;
	}

	public void setQualifier3(String qualifier3) {
		this.qualifier3 = qualifier3;
	}
}
