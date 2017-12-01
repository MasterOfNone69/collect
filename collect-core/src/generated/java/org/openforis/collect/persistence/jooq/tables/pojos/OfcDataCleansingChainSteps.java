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
public class OfcDataCleansingChainSteps implements Serializable {

	private static final long serialVersionUID = 1618174339;

	private Integer chainId;
	private Integer stepId;
	private Integer pos;

	public OfcDataCleansingChainSteps() {}

	public OfcDataCleansingChainSteps(OfcDataCleansingChainSteps value) {
		this.chainId = value.chainId;
		this.stepId = value.stepId;
		this.pos = value.pos;
	}

	public OfcDataCleansingChainSteps(
		Integer chainId,
		Integer stepId,
		Integer pos
	) {
		this.chainId = chainId;
		this.stepId = stepId;
		this.pos = pos;
	}

	public Integer getChainId() {
		return this.chainId;
	}

	public void setChainId(Integer chainId) {
		this.chainId = chainId;
	}

	public Integer getStepId() {
		return this.stepId;
	}

	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}

	public Integer getPos() {
		return this.pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}
}
