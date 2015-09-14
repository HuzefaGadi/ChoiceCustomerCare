package com.unfc.choicecustomercare.models;

import java.io.Serializable;
import java.util.List;

/**
 * Hai Nguyen - 8/27/15.
 */
public class BaseEntity implements Serializable {

	protected int id;
	protected boolean result;
	protected List<String> messages;

	public List<String> getMessages() {
		return messages;
	}

	public boolean isResult() {
		return result;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
