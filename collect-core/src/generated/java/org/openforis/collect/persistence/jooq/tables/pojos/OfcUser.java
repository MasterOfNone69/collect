/**
 * This class is generated by jOOQ
 */
package org.openforis.collect.persistence.jooq.tables.pojos;


import java.io.Serializable;

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
public class OfcUser implements Serializable {

	private static final long serialVersionUID = -1463248731;

	private Integer id;
	private String  username;
	private String  password;
	private String  enabled;

	public OfcUser() {}

	public OfcUser(OfcUser value) {
		this.id = value.id;
		this.username = value.username;
		this.password = value.password;
		this.enabled = value.enabled;
	}

	public OfcUser(
		Integer id,
		String  username,
		String  password,
		String  enabled
	) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEnabled() {
		return this.enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
}
