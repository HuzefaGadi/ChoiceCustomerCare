package com.unfc.choicecustomercare.models;

/**
 * Hai Nguyen - 8/28/15.
 */
public class MenuEntity extends BaseEntity {

	private int textResourceId;
	private int iconResourceId;

	public MenuEntity(int textResourceId, int iconResourceId) {
		this.textResourceId = textResourceId;
		this.iconResourceId = iconResourceId;
	}

	public int getTextResourceId() {
		return textResourceId;
	}

	public int getIconResourceId() {
		return iconResourceId;
	}
}
