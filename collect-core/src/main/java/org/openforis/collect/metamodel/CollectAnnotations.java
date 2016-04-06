package org.openforis.collect.metamodel;

import static org.openforis.collect.metamodel.ui.UIOptionsConstants.UI_NAMESPACE_URI;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.openforis.collect.metamodel.ui.UIOptions.CodeAttributeLayoutType;
import org.openforis.collect.metamodel.ui.UIOptions.Orientation;
import org.openforis.collect.metamodel.ui.UIOptionsConstants;
import org.openforis.collect.model.CollectRecord.Step;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.commons.versioning.Version;
import org.openforis.idm.metamodel.Annotatable;
import org.openforis.idm.metamodel.AttributeDefinition;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.TaxonAttributeDefinition;
import org.openforis.idm.metamodel.TextAttributeDefinition;

/**
 * 
 * @author S. Ricci
 *
 */
public class CollectAnnotations {

	public static String COLLECT_NAMESPACE_URI = "http://www.openforis.org/collect/3.0/collect";
	public static String COLLECT_PREFIX = "collect";

	public static String COLLECT_EARTH_NAMESPACE_URI = "http://www.openforis.org/collect/3.0/collectearth";
	public static String COLLECT_EARTH_PREFIX = "collectearth";
	
	public enum Annotation {
		//collect namespace
		INCLUDE_IN_DATA_EXPORT(new QName(COLLECT_NAMESPACE_URI, "includeInDataExport"), true),
		PHASE_TO_APPLY_DEFAULT_VALUE(new QName(COLLECT_NAMESPACE_URI, "phaseToApplyDefaultValue"), Step.ENTRY),
		EDITABLE(new QName(COLLECT_NAMESPACE_URI, "editable"), true),
		TARGET(new QName(COLLECT_NAMESPACE_URI, "target"), SurveyTarget.COLLECT_DESKTOP),
		COLLECT_VERSION(new QName(COLLECT_NAMESPACE_URI, "collectVersion"), "3.4.0"),
		
		//ui namespace
		TAB_SET(new QName(UI_NAMESPACE_URI, UIOptionsConstants.TAB_SET_NAME)),
		TAB_NAME(new QName(UI_NAMESPACE_URI, UIOptionsConstants.TAB)),
		LAYOUT(new QName(UI_NAMESPACE_URI, UIOptionsConstants.LAYOUT)),
		DIRECTION(new QName(UI_NAMESPACE_URI, UIOptionsConstants.DIRECTION)),
		COUNT_IN_SUMMARY_LIST(new QName(UI_NAMESPACE_URI, UIOptionsConstants.COUNT)),
		SHOW_ROW_NUMBERS(new QName(UI_NAMESPACE_URI, UIOptionsConstants.SHOW_ROW_NUMBERS)),
		AUTOCOMPLETE(new QName(UI_NAMESPACE_URI, UIOptionsConstants.AUTOCOMPLETE)),
		FIELDS_ORDER(new QName(UI_NAMESPACE_URI, UIOptionsConstants.FIELDS_ORDER)),
		VISIBLE_FIELDS(new QName(UI_NAMESPACE_URI, UIOptionsConstants.VISIBLE_FIELDS)),
		SHOW_ALLOWED_VALUES_PREVIEW(new QName(UI_NAMESPACE_URI, UIOptionsConstants.SHOW_ALLOWED_VALUES_PREVIEW), false),
		HIDE(new QName(UI_NAMESPACE_URI, UIOptionsConstants.HIDE), false),
		HIDE_WHEN_NOT_RELEVANT(new QName(UI_NAMESPACE_URI, UIOptionsConstants.HIDE_WHEN_NOT_RELEVANT), false),
		COLUMN(new QName(UI_NAMESPACE_URI, UIOptionsConstants.COLUMN), 1),
		COLUMN_SPAN(new QName(UI_NAMESPACE_URI, UIOptionsConstants.COLUMN_SPAN), 1),
		CODE_ATTRIBUTE_LAYOUT_DIRECTION(new QName(UI_NAMESPACE_URI, UIOptionsConstants.DIRECTION), "vertical"),
		CODE_ATTRIBUTE_LAYOUT_TYPE(new QName(UI_NAMESPACE_URI, UIOptionsConstants.LAYOUT_TYPE), CodeAttributeLayoutType.TEXT),
		CODE_ATTRIBUTE_SHOW_CODE(new QName(UI_NAMESPACE_URI, UIOptionsConstants.SHOW_CODE), true),
		TAXON_ATTRIBUTE_SHOW_FAMILY(new QName(UI_NAMESPACE_URI, UIOptionsConstants.SHOW_FAMILY), false),
		TAXON_ATTRIBUTE_INCLUDE_UNIQUE_VERNACULAR_NAME(new QName(UI_NAMESPACE_URI, UIOptionsConstants.INCLUDE_UNIQUE_VERNACULAR_NAME), false),
		WIDTH(new QName(UI_NAMESPACE_URI, UIOptionsConstants.WIDTH)),
		LABEL_WIDTH(new QName(UI_NAMESPACE_URI, UIOptionsConstants.LABEL_WIDTH)),
		LABEL_ORIENTATION(new QName(UI_NAMESPACE_URI, UIOptionsConstants.LABEL_ORIENTATION), Orientation.HORIZONTAL),
		AUTO_UPPERCASE(new QName(UI_NAMESPACE_URI, UIOptionsConstants.AUTO_UPPERCASE), false),
		
		//collect earth
		COLLECT_EARTH_FROM_CSV(new QName(COLLECT_EARTH_NAMESPACE_URI, "fromcsv"), false),
		COLLECT_EARTH_INCLUDE_IN_HEADER(new QName(COLLECT_EARTH_NAMESPACE_URI, "includeinheader"), false),
		COLLECT_EARTH_PLOT_AREA(new QName(COLLECT_EARTH_NAMESPACE_URI, "plotarea"), 1d),
		COLLECT_EARTH_BING_KEY(new QName(COLLECT_EARTH_NAMESPACE_URI, "bingKey"), "GENERATE YOUR OWN BING MAPS KEY AT https://www.bingmapsportal.com"),
		COLLECT_EARTH_SAMPLE_POINTS(new QName(COLLECT_EARTH_NAMESPACE_URI, "samplepoints"), 25), //0, 1, 9 (3x3), 25 (5x5), 49 (7x7)
		COLLECT_EARTH_OPEN_BING_MAPS(new QName(COLLECT_EARTH_NAMESPACE_URI, "openBingMaps"), false), 
		COLLECT_EARTH_OPEN_GEE_EXPLORER(new QName(COLLECT_EARTH_NAMESPACE_URI, "openExplorer"), true), 
		COLLECT_EARTH_OPEN_GEE_CODE_EDITOR(new QName(COLLECT_EARTH_NAMESPACE_URI, "openCodeEditor"), true), 
		COLLECT_EARTH_OPEN_STREET_VIEW(new QName(COLLECT_EARTH_NAMESPACE_URI, "openStreetView"), false)
		;
		
		private QName qName;
		private Object defaultValue;

		private Annotation(QName qname) {
			this.qName = qname;
		}

		private Annotation(QName qname, Object defaultValue) {
			this(qname);
			this.defaultValue = defaultValue;
		}
		
		public QName getQName() {
			return qName;
		}

		@SuppressWarnings("unchecked")
		public <T extends Object> T getDefaultValue() {
			return (T) defaultValue;
		}
	}
	
	private CollectSurvey survey;
	
	public CollectAnnotations(CollectSurvey survey) {
		super();
		this.survey = survey;
	}
	
	public boolean isIncludedInDataExport(NodeDefinition defn) {
		return getAnnotationBooleanValue(defn, Annotation.INCLUDE_IN_DATA_EXPORT);
	}
	
	public void setIncludeInDataExport(NodeDefinition defn, boolean value) {
		setAnnotationValue(defn, Annotation.INCLUDE_IN_DATA_EXPORT, value);
	}
	
	public String getAutoCompleteGroup(TextAttributeDefinition def) {
		return def.getAnnotation(Annotation.AUTOCOMPLETE.getQName());
	}
	
	public void setAutoCompleteGroup(TextAttributeDefinition def, String value) {
		def.setAnnotation(Annotation.AUTOCOMPLETE.getQName(), value);
	}
	
	public Version getCollectVersion() {
		String versionStr = survey.getAnnotation(Annotation.COLLECT_VERSION.getQName());
		if (StringUtils.isBlank(versionStr)) {
			versionStr = Annotation.COLLECT_VERSION.getDefaultValue();
		}
		return new Version(versionStr);
	}

	public void setCollectVersion(Version version) {
		setAnnotationValue(survey, Annotation.COLLECT_VERSION, version.toString());
	}
	
	public SurveyTarget getSurveyTarget() {
		String val = survey.getAnnotation(Annotation.TARGET.getQName());
		if (StringUtils.isBlank(val)) {
			return (SurveyTarget) Annotation.TARGET.defaultValue;
		} else {
			return SurveyTarget.fromCode(val);
		}
	}
	
	public void setSurveyTarget(SurveyTarget target) {
		String val = target == null || target == Annotation.TARGET.defaultValue ? null: target.getCode();
		survey.setAnnotation(Annotation.TARGET.getQName(), val);
	}

	public Step getPhaseToApplyDefaultValue(AttributeDefinition def) {
		return getAnnotaionEnumValue(def, Annotation.PHASE_TO_APPLY_DEFAULT_VALUE);
	}

	public void setPhaseToApplyDefaultValue(AttributeDefinition def, Step value) {
		setAnnotationEnumValue(def, Annotation.PHASE_TO_APPLY_DEFAULT_VALUE, value);
	}

	public boolean isEditable(AttributeDefinition defn) {
		return getAnnotationBooleanValue(defn, Annotation.EDITABLE);
	}
	
	public void setEditable(AttributeDefinition defn, boolean value) {
		setAnnotationValue(defn, Annotation.EDITABLE, value);
	}
	
	public boolean isShowFamily(TaxonAttributeDefinition defn) {
		return getAnnotationBooleanValue(defn, Annotation.TAXON_ATTRIBUTE_SHOW_FAMILY);
	}
	
	public void setShowFamily(TaxonAttributeDefinition defn, boolean value) {
		setAnnotationValue(defn, Annotation.TAXON_ATTRIBUTE_SHOW_FAMILY, value);
	}
	
	public boolean isIncludeUniqueVernacularName(TaxonAttributeDefinition defn) {
		return getAnnotationBooleanValue(defn, Annotation.TAXON_ATTRIBUTE_INCLUDE_UNIQUE_VERNACULAR_NAME);
	}
	
	public void setIncludeUniqueVernacularName(TaxonAttributeDefinition defn, boolean value) {
		setAnnotationValue(defn, Annotation.TAXON_ATTRIBUTE_INCLUDE_UNIQUE_VERNACULAR_NAME, value);
	}
	
	public boolean isFromCollectEarthCSV(AttributeDefinition defn) {
		return getAnnotationBooleanValue(defn, Annotation.COLLECT_EARTH_FROM_CSV);
	}
	
	public void setFromCollectEarthCSV(AttributeDefinition defn, boolean value) {
		setAnnotationValue(defn, Annotation.COLLECT_EARTH_FROM_CSV, value);
	}
	
	public boolean isIncludedInCollectEarthHeader(AttributeDefinition defn) {
		return getAnnotationBooleanValue(defn, Annotation.COLLECT_EARTH_INCLUDE_IN_HEADER);
	}
	
	public void setIncludedInCollectEarthHeader(AttributeDefinition defn, boolean value) {
		setAnnotationValue(defn, Annotation.COLLECT_EARTH_INCLUDE_IN_HEADER, value);
	}
	
	public Double getCollectEarthPlotArea() {
		return getAnnotationDoubleValue(survey, Annotation.COLLECT_EARTH_PLOT_AREA);
	}
	
	public void setCollectEarthPlotArea(Double value) {
		setAnnotationValue(survey, Annotation.COLLECT_EARTH_PLOT_AREA, value);
	}
	
	public String getBingMapsKey() {
		String bingMapsKey = survey.getAnnotation(Annotation.COLLECT_EARTH_BING_KEY.getQName());
		if (StringUtils.isBlank(bingMapsKey)) {
			bingMapsKey = Annotation.COLLECT_EARTH_BING_KEY.getDefaultValue();
		}
		return bingMapsKey;
	}
	
	public void setBingMapsKey(String value) {
		setAnnotationValue(survey, Annotation.COLLECT_EARTH_BING_KEY, value);
	}
	
	public boolean isBingMapsEnabled() {
		return getAnnotationBooleanValue(survey, Annotation.COLLECT_EARTH_OPEN_BING_MAPS);
	}
	
	public void setBingMapsEnabled( boolean value) {
		setAnnotationValue(survey, Annotation.COLLECT_EARTH_OPEN_BING_MAPS, value);
	}
	
	public boolean isGEEExplorerEnabled() {
		return getAnnotationBooleanValue(survey, Annotation.COLLECT_EARTH_OPEN_GEE_EXPLORER);
	}
	
	public void setGEEExplorerEnabled( boolean value) {
		setAnnotationValue(survey, Annotation.COLLECT_EARTH_OPEN_GEE_EXPLORER, value);
	}
	
	public boolean isGEECodeEditorEnabled() {
		return getAnnotationBooleanValue(survey, Annotation.COLLECT_EARTH_OPEN_GEE_CODE_EDITOR);
	}
	
	public void setGEECodeEditorEnabled( boolean value) {
		setAnnotationValue(survey, Annotation.COLLECT_EARTH_OPEN_GEE_CODE_EDITOR, value);
	}

	public boolean isStreetViewEnabled() {
		return getAnnotationBooleanValue(survey, Annotation.COLLECT_EARTH_OPEN_STREET_VIEW);
	}
	
	public void setStreetViewEnabled( boolean value) {
		setAnnotationValue(survey, Annotation.COLLECT_EARTH_OPEN_STREET_VIEW, value);
	}
	
	public int getCollectEarthSamplePoints() {
		return getAnnotationIntegerValue(survey, Annotation.COLLECT_EARTH_SAMPLE_POINTS);
	}
	
	public void setCollectEarthSamplePoints(Integer value) {
		setAnnotationValue(survey, Annotation.COLLECT_EARTH_SAMPLE_POINTS, value);
	}

	private Step getAnnotaionEnumValue(AttributeDefinition def, Annotation annotation) {
		String enumName = def.getAnnotation(annotation.getQName());
		if(StringUtils.isBlank(enumName)) {
			return annotation.getDefaultValue();
		} else {
			return Step.valueOf(enumName);
		}
	}
	
	private void setAnnotationEnumValue(Annotatable def, Annotation annotation, Enum<?> value) {
		String enumName;
		if ( value == null || value == annotation.getDefaultValue() ) {
			enumName = null;
		} else {
			enumName = value.name();
		}
		def.setAnnotation(annotation.getQName(), enumName);
	}
	
	private boolean getAnnotationBooleanValue(Annotatable annotatable, Annotation annotation) {
		String annotationValue = annotatable.getAnnotation(annotation.getQName());
		if ( StringUtils.isBlank(annotationValue) ) {
			Boolean defaultValue = annotation.getDefaultValue();
			return defaultValue.booleanValue();
		} else {
			return Boolean.valueOf(annotationValue);
		}
	}

	private void setAnnotationValue(Annotatable annotatable, Annotation annotation, boolean value) {
		String annotationValue;
		if ( annotation.getDefaultValue() != null && annotation.getDefaultValue().equals(value) ) {
			annotationValue = null;
		} else {
			annotationValue = Boolean.toString(value);
		}
		annotatable.setAnnotation(annotation.getQName(), annotationValue);
	}

	private Integer getAnnotationIntegerValue(Annotatable annotatable, Annotation annotation) {
		String annotationValue = annotatable.getAnnotation(annotation.getQName());
		if ( StringUtils.isBlank(annotationValue) ) {
			Integer defaultValue = annotation.getDefaultValue();
			return defaultValue;
		} else {
			return Integer.parseInt(annotationValue);
		}
	}
	
	private Double getAnnotationDoubleValue(Annotatable annotatable, Annotation annotation) {
		String annotationValue = annotatable.getAnnotation(annotation.getQName());
		if ( StringUtils.isBlank(annotationValue) ) {
			Double defaultValue = annotation.getDefaultValue();
			return defaultValue;
		} else {
			return Double.parseDouble(annotationValue);
		}		
	}
	
	private void setAnnotationValue(Annotatable annotatable, Annotation annotation, Object value) {
		String annotationValue;
		if ( value == null || value.equals(annotation.getDefaultValue()) ) {
			annotationValue = null;
		} else {
			annotationValue = value.toString();
		}
		annotatable.setAnnotation(annotation.getQName(), annotationValue);
	}
	
	public CollectSurvey getSurvey() {
		return survey;
	}
	
}
