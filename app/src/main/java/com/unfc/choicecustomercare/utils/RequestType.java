package com.unfc.choicecustomercare.utils;

/**
 * Hai Nguyen - 8/25/15.
 */
public enum RequestType {

	TECH(1), NURSE(2), DOCTOR(3);
	private final int value;

	RequestType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
