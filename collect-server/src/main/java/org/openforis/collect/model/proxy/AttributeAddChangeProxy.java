package org.openforis.collect.model.proxy;

import org.granite.messaging.amf.io.util.externalizer.annotation.ExternalizedProperty;
import org.openforis.collect.ProxyContext;
import org.openforis.collect.model.AttributeChange;

/**
 * 
 * @author S. Ricci
 *
 */
public class AttributeAddChangeProxy extends AttributeChangeProxy implements NodeAddChangeProxy {

	public AttributeAddChangeProxy(AttributeChange change, ProxyContext context) {
		super(change, context);
	}

	@Override
	@ExternalizedProperty
	public NodeProxy getCreatedNode() {
		return NodeProxy.fromNode(change.getNode(), context);
	}

}