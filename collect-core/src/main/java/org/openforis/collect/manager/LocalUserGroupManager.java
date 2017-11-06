package org.openforis.collect.manager;

import static org.openforis.collect.model.UserInGroup.UserGroupJoinRequestStatus.ACCEPTED;
import static org.openforis.collect.model.UserInGroup.UserGroupJoinRequestStatus.PENDING;
import static org.openforis.collect.model.UserInGroup.UserGroupRole.OWNER;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openforis.collect.model.User;
import org.openforis.collect.model.UserGroup;
import org.openforis.collect.model.UserGroup.Visibility;
import org.openforis.collect.model.UserInGroup;
import org.openforis.collect.model.UserInGroup.UserGroupRole;
import org.openforis.collect.model.UserRole;
import org.openforis.collect.persistence.UserGroupDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class LocalUserGroupManager extends AbstractPersistedObjectManager<UserGroup, Integer, UserGroupDao> implements UserGroupManager {

	private static final String DEFAULT_PUBLIC_USER_GROUP_NAME = "default_public_group";
	private static final String DEFAULT_PRIVATE_USER_GROUP_SUFFIX = "_default_private_group";
	private static final String DEFAULT_PRIVATE_USER_GROUP_LABEL_SUFFIX = " Default Private Group";
	
	private static UserGroup DEFAULT_PUBLIC_USER_GROUP = null;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public UserGroup createDefaultPrivateUserGroup(User user) {
		UserGroup userGroup = new UserGroup();
		userGroup.setName(getDefaultPrivateUserGroupName(user));
		userGroup.setLabel(user.getUsername() + DEFAULT_PRIVATE_USER_GROUP_LABEL_SUFFIX);
		userGroup.setVisibility(Visibility.PRIVATE);
		dao.insert(userGroup);
		UserInGroup userInGroup = new UserInGroup();
		userInGroup.setGroupId(userGroup.getId());
		userInGroup.setUserId(user.getId());
		userInGroup.setRole(OWNER);
		userInGroup.setJoinStatus(ACCEPTED);
		userInGroup.setRequestDate(new Date());
		userInGroup.setMemberSince(new Date());
		dao.insertRelation(userGroup, userInGroup);
		return userGroup;
	}

	public String getDefaultPrivateUserGroupName(User user) {
		return user.getUsername() + DEFAULT_PRIVATE_USER_GROUP_SUFFIX;
	}
	
	public String getDefaultPublicUserGroupName() {
		return DEFAULT_PUBLIC_USER_GROUP_NAME;
	}

	public UserGroup getDefaultPublicUserGroup() {
		if (DEFAULT_PUBLIC_USER_GROUP == null) {
			DEFAULT_PUBLIC_USER_GROUP = dao.loadByName(getDefaultPublicUserGroupName());
		}
		return DEFAULT_PUBLIC_USER_GROUP;
	}
	
	@Override
	public UserGroup loadById(Integer id) {
		UserGroup group = super.loadById(id);
		fillLazyLoadedFields(group);
		return group;
	}
	
	
	@Override
	public List<UserGroup> findAllUserDefinedGroups() {
		return fillLazyLoadedFields(dao.findGroups(false, null));
	}
	
	@Override
	public List<UserGroup> findAllRelatedUserGroups(User user) {
		List<UserGroup> relatedUserGroups = findByUser(user);
		Set<UserGroup> result = new HashSet<UserGroup>(relatedUserGroups);
		//include ancestors
		for (UserGroup userGroup : relatedUserGroups) {
			List<UserGroup> ancestors = findAncestorGroups(userGroup);
			result.addAll(ancestors);
		}
		return fillLazyLoadedFields(new ArrayList<UserGroup>(result));
	}
	
	private List<UserGroup> findAncestorGroups(UserGroup userGroup) {
		List<UserGroup> result = new ArrayList<UserGroup>();
		UserGroup currentGroup = userGroup;
		while (currentGroup.getParentId() != null) {
			UserGroup parentGroup = loadById(currentGroup.getParentId());
			result.add(0, parentGroup);
			currentGroup = parentGroup;
		}
		return result;
	}

	@Override
	public UserGroup findByName(String userGroupName) {
		UserGroup group = dao.loadByName(userGroupName);
		return fillLazyLoadedFields(group);
	}

	@Override
	public List<UserInGroup> findUsersInGroup(UserGroup userGroup) {
		return dao.findUsersByGroup(userGroup.getId());
	}
	
	@Override
	public UserInGroup findUserInGroup(UserGroup userGroup, User user) {
		return dao.findUserInGroup(userGroup.getId(), user.getId());
	}
	
	@Override
	public UserInGroup findUserInGroupOrDescendants(UserGroup userGroup, User user) {
		UserInGroup userInGroup = findUserInGroup(userGroup, user);
		if (userInGroup == null) {
			List<Integer> descendantGroupIds = dao.findChildrenGroupIds(userGroup.getId());
			for (Integer descendantGroupId : descendantGroupIds) {
				userInGroup = dao.findUserInGroup(descendantGroupId, user.getId());
				if (userInGroup != null) {
					return userInGroup;
				}
			}
			return null;
		} else {
			return userInGroup;
		}
	}
	
	@Override
	public List<UserGroup> findByUser(User user) {
		List<UserGroup> result;
		List<UserRole> userRoles = user.getRoles();
		if (userRoles.contains(UserRole.ADMIN)) {
			result = dao.loadAll();
		} else {
			result = dao.findByUser(user);
		}
		return fillLazyLoadedFields(result);
	}
	
	@Override
	public List<UserGroup> findPublicUserGroups() {
		return fillLazyLoadedFields(dao.findPublicGroups());
	}

	public List<UserGroup> findPublicUserDefinedGroups() {
		return fillLazyLoadedFields(dao.findPublicUserDefinedGroups());
	}
	
	@Override
	public List<UserGroup> findDescendantGroups(UserGroup group) {
		List<UserGroup> result = new ArrayList<UserGroup>();
		List<Integer> childrenGroupIds = dao.findChildrenGroupIds(group.getId());
		for (Integer childId : childrenGroupIds) {
			UserGroup childGroup = loadById(childId);
			result.add(childGroup);
		}
		return result;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public UserGroup save(UserGroup userGroup) {
		dao.save(userGroup);
		List<UserInGroup> oldUsersInGroup = dao.findUsersByGroup(userGroup.getId());
		Map<Integer, UserInGroup> parameterUsersInGroupByUserId = new HashMap<Integer, UserInGroup>();
		for (UserInGroup userInGroup : userGroup.getUsers()) {
			parameterUsersInGroupByUserId.put(userInGroup.getUserId(), userInGroup);
		}
		Map<Integer, UserInGroup> removedUsersInGroupByUserId = new HashMap<Integer, UserInGroup>();
		Map<Integer, UserInGroup> updatedUsersInGroupByUserId = new HashMap<Integer, UserInGroup>();
		Map<Integer, UserInGroup> newUsersInGroupByUserId = new HashMap<Integer, UserInGroup>();

		for (UserInGroup oldUserInGroup : oldUsersInGroup) {
			UserInGroup updatedUserInGroup = parameterUsersInGroupByUserId.get(oldUserInGroup.getUserId());
			if (updatedUserInGroup != null) {
				oldUserInGroup.setJoinStatus(updatedUserInGroup.getJoinStatus());
				oldUserInGroup.setRole(updatedUserInGroup.getRole());
				updatedUsersInGroupByUserId.put(oldUserInGroup.getUserId(), oldUserInGroup);
			} else {
				removedUsersInGroupByUserId.put(oldUserInGroup.getUserId(), oldUserInGroup);
			}
		}

		for (UserInGroup newUserInGroup : parameterUsersInGroupByUserId.values()) {
			if (! oldUsersInGroup.contains(newUserInGroup)) {
				newUserInGroup.setRequestDate(new Date());
				if (newUserInGroup.getJoinStatus() == ACCEPTED) {
					newUserInGroup.setMemberSince(new Date());
				}
				newUsersInGroupByUserId.put(newUserInGroup.getUserId(), newUserInGroup);
			}
		}

		for (UserInGroup userInGroup : removedUsersInGroupByUserId.values()) {
			deleteRelation(userGroup.getId(), userInGroup.getUserId());
		}
		for (UserInGroup userInGroup : updatedUsersInGroupByUserId.values()) {
			dao.updateRelation(userInGroup);
		}
		for (UserInGroup userInGroup : newUsersInGroupByUserId.values()) {
			dao.insertRelation(userGroup, userInGroup);
		}
		return userGroup;
	}
	
	@Override
	public void deleteRelation(int userGroupId, int userId) {
		dao.deleteRelation(userGroupId, userId);
	}
	
	@Override
	public void deleteAllUserRelations(int userId) {
		dao.deleteAllUserRelations(userId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void requestJoin(User user, UserGroup userGroup, UserGroupRole role) {
		UserInGroup userInGroup = new UserInGroup();
		userInGroup.setGroupId(userGroup.getId());
		userInGroup.setUserId(user.getId());
		userInGroup.setRole(role);
		userInGroup.setRequestDate(new Date());
		userInGroup.setJoinStatus(PENDING);
		dao.insertRelation(userGroup, userInGroup);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void acceptJoinRequest(User user, UserGroup userGroup) {
		dao.acceptRelation(user, userGroup);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(int id) {
		dao.deleteById(id);
	}
	
	private List<UserGroup> fillLazyLoadedFields(List<UserGroup> groups) {
		for (UserGroup group : groups) {
			fillLazyLoadedFields(group);
		}
		return groups;
	}

	private UserGroup fillLazyLoadedFields(UserGroup group) {
		if (group == null) {
			return null;
		} else {
			List<UserInGroup> usersInGroup = dao.findUsersByGroup(group.getId());
			group.setUsers(new HashSet<UserInGroup>(usersInGroup));
			Set<Integer> childrenGroupIds = new HashSet<Integer>(dao.findChildrenGroupIds(group.getId()));
			group.setChildrenGroupIds(childrenGroupIds);
			return group;
		}
	}

	public static class UserGroupTree {
		
		private List<UserGroupTreeNode> roots = new ArrayList<UserGroupTreeNode>();
		
		public List<UserGroupTreeNode> getRoots() {
			return roots;
		}
		
		public static class UserGroupTreeNode {
			private UserGroup userGroup;
			private List<UserGroupTreeNode> children = new ArrayList<UserGroupTreeNode>();
			
			public UserGroupTreeNode(UserGroup userGroup) {
				super();
				this.userGroup = userGroup;
			}
			
			public UserGroup getUserGroup() {
				return userGroup;
			}
			
			public List<UserGroupTreeNode> getChildren() {
				return children;
			}
			
		}
		
	}
	
	@Override
	public Map<String, String> getQualifiers(UserGroup group, User user) {
		UserInGroup userInGroup = findUserInGroupOrDescendants(group, user);
		if (userInGroup == null) {
			throw new IllegalArgumentException(String.format("User %s not allowed to see records for user group %s", 
					user.getUsername(), group.getName()));
		}
		UserGroup associatedGroup = loadById(userInGroup.getGroupId());
		return associatedGroup.getQualifiersByName();
	}

	@Override
	public UserGroup findUserGroupByResource(String resourceType, String resourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findResourcesByUserGroup(int userGroupId, String resourceType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void associateResource(int userGroupId, String resourceType, String resourceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disassociateResource(int userGroupId, String resourceType, String resourceId) {
		// TODO Auto-generated method stub
		
	}

}