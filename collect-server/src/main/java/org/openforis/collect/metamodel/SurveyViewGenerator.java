package org.openforis.collect.metamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openforis.collect.designer.metamodel.AttributeType;
import org.openforis.collect.designer.metamodel.NodeType;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.idm.metamodel.AttributeDefinition;
import org.openforis.idm.metamodel.CodeList;
import org.openforis.idm.metamodel.CodeListItem;
import org.openforis.idm.metamodel.CodeListService;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.NodeDefinitionVisitor;
import org.openforis.idm.metamodel.NodeLabel.Type;
import org.openforis.idm.model.Coordinate;

/**
 * 
 * @author S. Ricci
 *
 */
public class SurveyViewGenerator {

	private Locale locale;
	
	public SurveyViewGenerator(Locale locale) {
		super();
		this.locale = locale;
	}

	public SurveyView generateView(CollectSurvey survey) {
		final SurveyView surveyView = new SurveyView(survey.getId(), survey.getName(), survey.isTemporary(), survey.getTarget());
		final String langCode = locale.getLanguage();
		
		List<CodeList> codeLists = survey.getCodeLists();
		for (CodeList codeList : codeLists) {
			CodeListView codeListView = new CodeListView();
			codeListView.setId(codeList.getId());
			codeListView.setName(codeList.getName());
			
			CodeListService service = survey.getContext().getCodeListService();
			List<CodeListItem> items = service.loadRootItems(codeList);
			for (CodeListItem item : items) {
				codeListView.items.add(createCodeListItemView(item, langCode));
			}
			
			surveyView.codeLists.add(codeListView);
		}
		
		final Map<Integer, NodeDefView> viewById = new HashMap<Integer, NodeDefView>();
		survey.getSchema().traverse(new NodeDefinitionVisitor() {
			public void visit(NodeDefinition def) {
				int id = def.getId();
				String name = def.getName();
				String label = getLabel(def, langCode);
				NodeDefView view;
				if (def instanceof EntityDefinition) {
					view = new EntityDefView(((EntityDefinition) def).isRoot(), id, name, label, def.isMultiple());
				} else {
					AttributeDefinition attrDef = (AttributeDefinition) def;
					view = new AttributeDefView(id, name, label, AttributeType.valueOf(attrDef), attrDef.getFieldNames(),
							attrDef.isKey(), attrDef.isMultiple());
				}
				NodeDefinition parentDef = def.getParentDefinition();
				if (parentDef == null) {
					surveyView.addRootEntity((EntityDefView) view);
				} else {
					EntityDefView parentView = (EntityDefView) viewById.get(parentDef.getId());
					parentView.addChild(view);
				}
				viewById.put(id, view);
			}

		});
		return surveyView;
	}
	
	private CodeListItemView createCodeListItemView(CodeListItem item, String langCode) {
		CodeListService service = item.getSurvey().getContext().getCodeListService();
		
		CodeListItemView itemView = new CodeListItemView();
		itemView.code = item.getCode();
		itemView.label = item.getLabel(langCode);
		
		List<CodeListItemView> childItemsView = new ArrayList<CodeListItemView>();
		List<CodeListItem> childItems = service.loadChildItems(item);
		for (CodeListItem childItem : childItems) {
			childItemsView.add(createCodeListItemView(childItem, langCode));
		}
		itemView.items.addAll(childItemsView);
		return itemView;
	}

	private String getLabel(NodeDefinition def, String langCode) {
		String label = def.getLabel(Type.INSTANCE, langCode);
		if (label == null && ! def.getSurvey().isDefaultLanguage(langCode)) {
			label = def.getLabel(Type.INSTANCE);
		}
		return label;
	}
	
	public static class SurveyView {
		
		public enum Shape {
			CIRCLE, SQUARE
		}
		
		public enum Distribution {
			RANDOM, GRIDDED
		}
		
		private Integer id;
		private String name;
		private boolean temporary;
		private SurveyTarget target;
		private List<CodeListView> codeLists = new ArrayList<CodeListView>();
		private List<EntityDefView> rootEntities = new ArrayList<EntityDefView>();
		//TODO
		private List<Coordinate> aoiBoundary;
		private Integer numberOfPlots;
		private Shape plotShape;
		private Double plotWidth;
		private Double plotResolution;
		private Distribution plotDistribution;
		private Integer samplesPerPlot;
		private Shape sampleShape;
		private Double sampleWidth;
		private Double sampleResolution;
		private Distribution sampleDistribution;
		private List<String> imagery;
		
		public SurveyView(Integer id, String name, boolean temporary, SurveyTarget target) {
			this.id = id;
			this.name = name;
			this.temporary = temporary;
			this.target = target;
		}
		
		public Integer getId() {
			return id;
		}
		
		public String getName() {
			return name;
		}
		
		public boolean isTemporary() {
			return temporary;
		}
		
		public void setTemporary(boolean temporary) {
			this.temporary = temporary;
		}
		
		public SurveyTarget getTarget() {
			return target;
		}
		
		public void setTarget(SurveyTarget target) {
			this.target = target;
		}
		
		public void addRootEntity(EntityDefView rootEntity) {
			rootEntities.add(rootEntity);
		}
		
		public List<EntityDefView> getRootEntities() {
			return rootEntities;
		}
		
		public List<CodeListView> getCodeLists() {
			return codeLists;
		}
	}
	
	public static abstract class SurveyObjectView {
		
		protected int id;
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

	}
	
	public static class CodeListItemView extends SurveyObjectView {
		
		private String code;
		private String label;
		private String color;
		
		private List<CodeListItemView> items = new ArrayList<CodeListItemView>();

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public List<CodeListItemView> getItems() {
			return items;
		}

		public void setItems(List<CodeListItemView> items) {
			this.items = items;
		}
		
	}
	
	public static class CodeListView extends SurveyObjectView {
		
		private String name;
		private List<CodeListItemView> items = new ArrayList<CodeListItemView>();
		
		public List<CodeListItemView> getItems() {
			return items;
		}
		
		public void setItems(List<CodeListItemView> items) {
			this.items = items;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
	}

	public static abstract class NodeDefView extends SurveyObjectView {
		
		private String name;
		private String label;
		private NodeType type;
		private boolean key;
		private boolean multiple;
		
		public NodeDefView(int id, String name, String label, NodeType type, boolean key, boolean multiple) {
			super();
			this.id = id;
			this.name = name;
			this.label = label;
			this.type = type;
			this.key = key;
			this.multiple = multiple;
		}

		
		public String getName() {
			return name;
		}

		public String getLabel() {
			return label;
		}

		public NodeType getType() {
			return type;
		}
		
		public boolean isKey() {
			return key;
		}
		
		public boolean isMultiple() {
			return multiple;
		}
		
	}
	
	public static class EntityDefView extends NodeDefView {

		private List<NodeDefView> children;
		private boolean root;
		
		public EntityDefView(boolean root, int id, String name, String label, 
				boolean multiple) {
			super(id, name, label, NodeType.ENTITY, false, multiple);
			this.root = root;
			children = new ArrayList<NodeDefView>();
		}

		public boolean isRoot() {
			return root;
		}
		
		public List<NodeDefView> getChildren() {
			return children;
		}

		public void addChild(NodeDefView child) {
			children.add(child);
		}
		
		public void setChildren(List<NodeDefView> children) {
			this.children = children;
		}
		
	}
	
	public static class AttributeDefView extends NodeDefView {

		private AttributeType attributeType;
		private List<String> fieldNames;
		
		public AttributeDefView(int id, String name, String label, AttributeType type, 
				List<String> fieldNames, boolean key, boolean multiple) {
			super(id, name, label, NodeType.ATTRIBUTE, key, multiple);
			this.attributeType = type;
			this.fieldNames = fieldNames;
		}
		
		public AttributeType getAttributeType() {
			return attributeType;
		}
		
		public List<String> getFieldNames() {
			return this.fieldNames;
		}
	}
	
	
}
