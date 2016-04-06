/**
 * 
 */
package org.openforis.idm.metamodel;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openforis.idm.metamodel.expression.SchemaPathExpression;
import org.openforis.idm.model.NodePathPointer;
import org.openforis.idm.model.expression.ExpressionEvaluator;
import org.openforis.idm.model.expression.InvalidExpressionException;
import org.openforis.idm.path.Path;

/**
 * @author M. Togna
 *
 */
class StateDependencyMap {
	
	private static final Log LOG = LogFactory.getLog(StateDependencyMap.class);
	
	private ExpressionEvaluator expressionEvaluator;
	private Map<NodeDefinition, Set<NodePathPointer>> dependentsBySource;
	private Map<NodeDefinition, Set<NodePathPointer>> sourcesByDependent;

	StateDependencyMap(ExpressionEvaluator expressionEvaluator) {
		this.expressionEvaluator = expressionEvaluator;
		this.dependentsBySource = new HashMap<NodeDefinition, Set<NodePathPointer>>();
		this.sourcesByDependent = new HashMap<NodeDefinition, Set<NodePathPointer>>();
	}
	
	Set<NodePathPointer> getDependencySet(NodeDefinition def){
		Set<NodePathPointer> set = dependentsBySource.get(def);
		if(set == null){
			return Collections.emptySet();
		} else {
			return set;
		}
	}
	
	Set<NodePathPointer> getSources(NodeDefinition def) {
		Set<NodePathPointer> set = sourcesByDependent.get(def);
		if(set == null){
			return Collections.emptySet();
		} else {
			return set;
		}
	}
	
	private void addDependency(NodeDefinition def, NodePathPointer value){
		Set<NodePathPointer> set = dependentsBySource.get(def);
		if(set == null){
			set = new HashSet<NodePathPointer>();
			dependentsBySource.put(def, set);
		}
		set.add(value);
	}
	
	void addDependency(NodeDefinition sourceNodeDef, String pathToDependentParent, NodeDefinition dependentNodeDef) {
		NodePathPointer nodePointer = new NodePathPointer(pathToDependentParent, dependentNodeDef);
		addDependency(sourceNodeDef, nodePointer);
	}
		
	void addSource(NodeDefinition context, String dependentParentEntityPath, NodeDefinition dependentNodeDef) {
		Set<NodePathPointer> set = sourcesByDependent.get(context);
		if(set == null){
			set = new HashSet<NodePathPointer>();
			sourcesByDependent.put(context, set);
		}
		set.add(new NodePathPointer(dependentParentEntityPath, dependentNodeDef));
	}

	void registerDependencies(NodeDefinition nodeDefinition, NodeDefinition dependentDefinition) {
		NodeDefinition context = nodeDefinition.getParentDefinition();
		String relativePath = context.getRelativePath(dependentDefinition);
		registerDependencies(nodeDefinition, relativePath);
	}
	
	/**
	 * Registers the dependencies related to an expression using the parent definition of the specified
	 * node definition as context
	 * 
	 * @param dependentDef context node definition
	 * @param expression expression to evaluate
	 */
	void registerDependencies(NodeDefinition dependentDef, String expression) {
		if (StringUtils.isBlank(expression)) {
			return;
		}
		try {
			EntityDefinition dependentParentDef = dependentDef.getParentEntityDefinition();
			Set<String> sourcePaths = getReferencedPaths(expression);
			for (String sourcePath : sourcePaths) {
				String sourceAbsolutePath = toNodeDefinitionAbsolutePath(sourcePath);
				
				SchemaPathExpression sourceExpression = new SchemaPathExpression(sourceAbsolutePath);

				NodeDefinition sourceDef = sourceExpression.evaluate(dependentParentDef, dependentDef);

				EntityDefinition commonAncestor = getCommonAncestor(dependentParentDef, sourceAbsolutePath);

				registerDependent(commonAncestor, sourceDef, dependentDef);
				registerSource(commonAncestor, sourceDef, dependentDef);
			}
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("Unable to register dependencies for node " + dependentDef.getPath() + " with expression " + expression, e);
			}
		}
	}

	protected void registerDependent(EntityDefinition commonAncestor, NodeDefinition sourceDef, NodeDefinition dependentDef) {
		registerDependency(false, commonAncestor, sourceDef, dependentDef);
	}
	
	protected void registerSource(EntityDefinition commonAncestor, NodeDefinition sourceDef, NodeDefinition dependentDef) {
		registerDependency(true, commonAncestor, dependentDef, sourceDef);
	}
	
	private void registerDependency(boolean source, EntityDefinition commonAncestor, NodeDefinition sourceDef, NodeDefinition dependentDef) {
		EntityDefinition sourceParentDef = sourceDef.getParentEntityDefinition();
		EntityDefinition dependentParentDef = dependentDef.getParentEntityDefinition();
		
		StringBuilder pathToDependentParentSB = new StringBuilder();
		pathToDependentParentSB.append(sourceParentDef.getRelativePath(commonAncestor));
		if ( commonAncestor != dependentParentDef ) {
			pathToDependentParentSB.append("/");
			pathToDependentParentSB.append(commonAncestor.getRelativePath(dependentParentDef));
		}
		if ( source ) {
			addSource(sourceDef, pathToDependentParentSB.toString(), dependentDef);
		} else {
			addDependency(sourceDef, pathToDependentParentSB.toString(), dependentDef);
		}
	}

	private EntityDefinition getCommonAncestor(EntityDefinition parent, String path) {
		String[] parts = path.split("/");
		EntityDefinition commonAncestor = parent;
		for (String part : parts) {
			if(part.equals(Path.NORMALIZED_PARENT_FUNCTION)) {
				commonAncestor = commonAncestor.getParentEntityDefinition();
			} else {
				break;
			}
		}
		return commonAncestor;
	}
	
	private String toNodeDefinitionAbsolutePath(String path) {
		String absolutePath = Path.getAbsolutePath(path);
		absolutePath = removeAttributeRef(absolutePath);
		return absolutePath;
	}

	private String removeAttributeRef(String sourceAbsolutePath) {
		int lastIndexOfAt = sourceAbsolutePath.lastIndexOf("/@");
		if (lastIndexOfAt > 0) {
			sourceAbsolutePath = sourceAbsolutePath.substring(0, lastIndexOfAt);
		}
		return sourceAbsolutePath;
	}
	
	private Set<String> getReferencedPaths(String expression) throws InvalidExpressionException {
		Set<String> paths = expressionEvaluator.determineReferencedPaths(expression);
		return paths;
	}

}
