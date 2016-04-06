/**
 * 
 */
package org.openforis.idm.metamodel;

import java.util.List;

import org.openforis.commons.lang.Numbers;
import org.openforis.idm.model.Coordinate;
import org.openforis.idm.model.CoordinateAttribute;
import org.openforis.idm.model.Node;
import org.openforis.idm.model.Value;

/**
 * @author G. Miceli
 * @author M. Togna
 */
public class CoordinateAttributeDefinition extends AttributeDefinition  {

	private static final long serialVersionUID = 1L;

	public static final String SRS_FIELD_NAME = "srs";
	public static final String X_FIELD_NAME = "x";
	public static final String Y_FIELD_NAME = "y";
	
	private final FieldDefinition<Double> xField = new FieldDefinition<Double>(X_FIELD_NAME, "x", "x", Double.class, this);
	private final FieldDefinition<Double> yField = new FieldDefinition<Double>(Y_FIELD_NAME, "y", "y", Double.class, this);
	private final FieldDefinition<String> srsIdField = new FieldDefinition<String>(SRS_FIELD_NAME, "srs", "srs", String.class, this);

	private final FieldDefinitionMap fieldDefinitionByName = new FieldDefinitionMap(xField, yField, srsIdField);

	CoordinateAttributeDefinition(Survey survey, int id) {
		super(survey, id);
	}

	CoordinateAttributeDefinition(CoordinateAttributeDefinition obj, int id) {
		super(obj, id);
	}
	
	@Override
	public Node<?> createNode() {
		return new CoordinateAttribute(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Coordinate createValue(String string) {
		return Coordinate.parseCoordinate(string);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Coordinate createValue(Object val) {
		if (val == null) {
			return null;
		} else if (val instanceof String) {
			return createValue((String) val);
		} else {
			throw new IllegalArgumentException("Invalid value type: " + val.getClass());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <V extends Value> V createValueFromFieldStringValues(List<String> fieldValues) {
		if (fieldValues == null || fieldValues.isEmpty()) {
			return null;
		} else if (fieldValues.size() == 3) {
			return (V) new Coordinate(
					Numbers.toDoubleObject(fieldValues.get(0)),
					Numbers.toDoubleObject(fieldValues.get(1)),
					fieldValues.get(2));
		} else {
			throw new IllegalArgumentException("Excpected " + 3 + " field values maximum, found: " + fieldValues.size());
		}
	}
	
	@Override
	protected FieldDefinitionMap getFieldDefinitionMap() {
		return fieldDefinitionByName;
	}
	
	@Override
	public boolean hasMainField() {
		return false;
	}
	
	@Override
	public String getMainFieldName() {
		throw new IllegalArgumentException("Main field not defined");
	}
	
	@Override
	public Class<? extends Value> getValueType() {
		return Coordinate.class;
	}
	
	public FieldDefinition<Double> getXField() {
		return xField;
	}
	
	public FieldDefinition<Double> getYField() {
		return yField;
	}
	
	public FieldDefinition<String> getSrsIdField() {
		return srsIdField;
	}
}
