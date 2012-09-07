/**
 * This file is part of the AMEE Java Client Library.
 *
 * Copyright (c) 2008 AMEE UK Ltd. (http://www.amee.com)
 *
 * The AMEE Java Client Library is free software, released under the MIT
 * license. See mit-license.txt for details.
 */

package com.amee.client.cache;

import java.io.Serializable;

import com.amee.client.model.base.AmeeObject;
import com.amee.client.model.base.AmeeObjectReference;

public class AmeeObjectCacheEntry implements Serializable, Comparable<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1577029534651728316L;
	private AmeeObjectReference objectReference = null;
	private AmeeObject object = null;

	private AmeeObjectCacheEntry() {
		super();
	}

	public AmeeObjectCacheEntry(final AmeeObjectReference ref,
			final AmeeObject object) {
		this();
		setObjectReference(ref);
		setObject(object);
	}

	@Override
	public boolean equals(final Object o) {
		return getObjectReference().equals(o);
	}

	public int compareTo(final Object o) {
		return getObjectReference().compareTo(o);
	}

	@Override
	public int hashCode() {
		return getObjectReference().hashCode();
	}

	@Override
	public String toString() {
		return getObjectReference().toString();
	}

	public AmeeObjectReference getObjectReference() {
		return objectReference;
	}

	public void setObjectReference(final AmeeObjectReference ref) {
		if (ref != null) {
			objectReference = ref;
		}
	}

	public AmeeObject getObject() {
		return object;
	}

	public void setObject(final AmeeObject object) {
		if (object != null) {
			this.object = object;
		}
	}
}