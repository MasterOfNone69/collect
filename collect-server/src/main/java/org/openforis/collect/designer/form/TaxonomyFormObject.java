package org.openforis.collect.designer.form;

import org.openforis.collect.model.CollectTaxonomy;

public class TaxonomyFormObject extends FormObject<CollectTaxonomy> {

	private String name;

	@Override
	public void loadFrom(CollectTaxonomy source, String languageCode) {
		super.loadFrom(source, languageCode);
		name = source.getName();
	}
	
	@Override
	public void saveTo(CollectTaxonomy dest, String language) {
		dest.setName(name);
	}

	@Override
	protected void reset() {
		name = null;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
