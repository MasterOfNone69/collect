package org.openforis.collect.model;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openforis.collect.persistence.jooq.tables.pojos.OfcUsergroup;
import org.openforis.idm.metamodel.PersistedObject;

public class UserGroup extends OfcUsergroup implements PersistedObject {

	private static final long serialVersionUID = 1L;

	public enum Visibility {
		PUBLIC('P'), PRIVATE('N');
		
		private char code;
		
		Visibility(char code) {
			this.code = code;
		}
		
		public static Visibility fromCode(char code) {
			for (Visibility value : values()) {
				if (value.code == code) {
					return value;
				}
			}
			throw new IllegalArgumentException("Invalid User Group Visibility code: " + code);
		}
		
		public char getCode() {
			return code;
		}
	}
	
	private User createdByUser;

	public User getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(User createdByUser) {
		this.createdByUser = createdByUser;
		if (createdByUser != null) {
			this.setCreatedBy(createdByUser.getId());
		}
	}

	public Visibility getVisibility() {
		String code = getVisibilityCode();
		return StringUtils.isBlank(code) ? null : Visibility.fromCode(code.charAt(0));
	}

	public void setVisibility(Visibility visibility) {
		setVisibilityCode(visibility == null ? null : String.valueOf(visibility.getCode()));
	}
	
	public String getQualifierName() {
		return getQualifier1Name();
	}
	
	public void setQualifierName(String qualifierName) {
		setQualifier1Name(qualifierName);
	}
	
	public String getQualifierValue() {
		return getQualifier1Value();
	}
	
	public void setQualifierValue(String qualifierValue) {
		setQualifier1Value(qualifierValue);
	}
	
	private Set<UserInGroup> users = new HashSet<UserInGroup>();
	
	public Set<UserInGroup> getUsers() {
		return users;
	}
	
	public void setUsers(Set<UserInGroup> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserGroup other = (UserGroup) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
}