/**
 * 
 */
package org.openforis.collect.model.proxy;

import java.util.Locale;

import org.granite.messaging.amf.io.util.externalizer.annotation.ExternalizedProperty;
import org.openforis.collect.manager.CodeListManager;
import org.openforis.collect.metamodel.proxy.CodeListItemProxy;
import org.openforis.idm.metamodel.CodeAttributeDefinition;
import org.openforis.idm.metamodel.CodeList;
import org.openforis.idm.metamodel.CodeListItem;
import org.openforis.idm.model.CodeAttribute;

/**
 * @author S. Ricci
 *
 */
public class CodeAttributeProxy extends AttributeProxy {

	private transient CodeAttribute codeAttribute;
	
	public CodeAttributeProxy(EntityProxy parent,
			CodeAttribute attribute, Locale locale) {
		super(parent, attribute, locale);
		this.codeAttribute = attribute;
	}

	@ExternalizedProperty
	public CodeListItemProxy getCodeListItem() {
		if ( isEnumerator() ) {
			CodeListManager codeListManager = getCodeListManager();
			CodeListItem codeListItem = codeListManager.loadItemByAttribute(codeAttribute);
			return codeListItem == null ? null: new CodeListItemProxy(codeListItem);
		} else {
			return null;
		}
	}

	private CodeListManager getCodeListManager() {
		return getContextBean(CodeListManager.class);
	}

	@ExternalizedProperty
	public boolean isEnumerator() {
		return codeAttribute.isEnumerator();
	}

	protected boolean isExternalCodeList() {
		CodeAttributeDefinition defn = codeAttribute.getDefinition();
		CodeList list = defn.getList();
		return list.isExternal();
	}
	
}
