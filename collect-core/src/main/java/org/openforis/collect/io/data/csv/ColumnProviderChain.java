package org.openforis.collect.io.data.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openforis.idm.model.Node;

/**
 * @author G. Miceli
 * @author S. Ricci
 * 
 */
public class ColumnProviderChain extends BasicColumnProvider {
	private List<ColumnProvider> providers;
	private List<String> headings;

	public ColumnProviderChain(CSVExportConfiguration config, String headingPrefix, ColumnProvider... providers) {
		this(config, headingPrefix, Arrays.asList(providers));
	}
	
	public ColumnProviderChain(CSVExportConfiguration config, List<ColumnProvider> providers) {
		this(config, "", providers);
	}

	public ColumnProviderChain(CSVExportConfiguration config, ColumnProvider... providers) {
		this(config, Arrays.asList(providers));
	}

	public ColumnProviderChain(CSVExportConfiguration config, String headingPrefix, List<ColumnProvider> providers) {
//		if ( providers == null || providers.isEmpty() ) {
//			throw new IllegalArgumentException("Providers may not be null or empty");
//		}
		super(config);
		this.providers = providers;
		this.headings = getColumnHeadingsInternal(headingPrefix);
	}
	
	public List<String> getColumnHeadings() {
		return headings;
	}

	public List<ColumnProvider> getColumnProviders() {
		return providers;
	}
	
	private List<String> getColumnHeadingsInternal(String headingPrefix) {
		ArrayList<String> h = new ArrayList<String>(); 
		for (ColumnProvider p : providers) {
			List<String> columnHeadings = p.getColumnHeadings();
			for (String heading : columnHeadings) {
				h.add(headingPrefix+heading);
			}
		}
		return Collections.unmodifiableList(h);
	}

	public List<String> extractValues(Node<?> axis) {
		ArrayList<String> v = new ArrayList<String>();
		for (ColumnProvider p : providers) {
			v.addAll(p.extractValues(axis));
		}
		return v;
	}
	
	protected List<String> emptyValues() {
		return Collections.nCopies(headings.size(), "");
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(null)
				.append("Column provider chain")
				.append("providers: " + getColumnProviders())
				.build();
	}
}
