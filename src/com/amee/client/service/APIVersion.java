package com.amee.client.service;

import java.util.HashMap;
import java.util.Map;

public enum APIVersion {

	ONE("1.0"), TWO("2.0");

	private String version;

	private static final Map<String, APIVersion> stringToEnum = new HashMap<String, APIVersion>();
	static {
		for (final APIVersion apiVersion : values()) {
			stringToEnum.put(apiVersion.toString(), apiVersion);
		}
	}

	APIVersion(final String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return version;
	}

	public static APIVersion fromString(final String version) {
		return stringToEnum.get(version);
	}

}
