package org.openforis.collect.metamodel.uiconfiguration.view;

import java.util.ArrayList;
import java.util.List;

import org.openforis.collect.metamodel.ui.UIColumn;
import org.openforis.collect.metamodel.ui.UIColumnGroup;
import org.openforis.collect.metamodel.ui.UITableHeadingComponent;

public abstract class UITableHeadingComponentView<O extends UITableHeadingComponent> extends UIModelObjectView<O> {

	public UITableHeadingComponentView(O uiObject) {
		super(uiObject);
	}

	@SuppressWarnings("unchecked")
	public static <V extends UITableHeadingComponentView<?>, C extends UITableHeadingComponent> 
			List<V> fromObjects(List<C> components) {
		List<V> views = new ArrayList<V>(components.size());
		for (C c : components) {
			if (c instanceof UIColumn) {
				views.add((V) new UIColumnView((UIColumn) c));
			} else {
				views.add((V) new UIColumnGroupView((UIColumnGroup) c));
			}
		}
		return views;
	}
	
	public int getCol() {
		return uiObject.getCol();
	}
	
	public int getColSpan() {
		return uiObject.getColSpan();
	}
	
	public int getRow() {
		return uiObject.getRow();
	}
	
	public int getRowSpan() {
		return uiObject.getRowSpan();
	}
}