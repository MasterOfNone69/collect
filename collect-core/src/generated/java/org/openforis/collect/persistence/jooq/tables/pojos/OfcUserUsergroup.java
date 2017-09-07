/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables.pojos;


import java.io.Serializable;
import java.sql.Timestamp;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OfcUserUsergroup implements Serializable {

	private static final long serialVersionUID = 709949427;

	private Integer   userId;
	private Integer   groupId;
	private String    roleCode;
	private String    statusCode;
	private Timestamp requestDate;
	private Timestamp memberSince;

	public OfcUserUsergroup() {}

	public OfcUserUsergroup(OfcUserUsergroup value) {
		this.userId = value.userId;
		this.groupId = value.groupId;
		this.roleCode = value.roleCode;
		this.statusCode = value.statusCode;
		this.requestDate = value.requestDate;
		this.memberSince = value.memberSince;
	}

	public OfcUserUsergroup(
		Integer   userId,
		Integer   groupId,
		String    roleCode,
		String    statusCode,
		Timestamp requestDate,
		Timestamp memberSince
	) {
		this.userId = userId;
		this.groupId = groupId;
		this.roleCode = roleCode;
		this.statusCode = statusCode;
		this.requestDate = requestDate;
		this.memberSince = memberSince;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Timestamp getRequestDate() {
		return this.requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public Timestamp getMemberSince() {
		return this.memberSince;
	}

	public void setMemberSince(Timestamp memberSince) {
		this.memberSince = memberSince;
	}
}
