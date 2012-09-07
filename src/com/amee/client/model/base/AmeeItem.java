/**
 * This file is part of the AMEE Java Client Library.
 *
 * Copyright (c) 2008 AMEE UK Ltd. (http://www.amee.com)
 *
 * The AMEE Java Client Library is free software, released under the MIT
 * license. See mit-license.txt for details.
 */

package com.amee.client.model.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.amee.client.AmeeException;
import com.amee.client.service.AmeeObjectFactory;
import com.amee.client.util.Choice;

public abstract class AmeeItem extends AmeeObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4295703355921139132L;
	private AmeeObjectReference parentRef = null;
	private String label = null;
	private List<AmeeObjectReference> valueRefs = new ArrayList<AmeeObjectReference>();

	public AmeeItem() {
		super();
	}

	public AmeeItem(final AmeeObjectReference ref) {
		super(ref);
	}

	public AmeeItem(final String path, final AmeeObjectType objectType) {
		super(path, objectType);
	}

	public void populate(final AmeeItem copy) {
		super.populate(copy);
		copy.setParentRef(parentRef);
		copy.setLabel(label);
		copy.setValueRefs(new ArrayList<AmeeObjectReference>(valueRefs));
	}

	public abstract void setParentRef();

	public List<AmeeValue> getValues() throws AmeeException {
		final AmeeObjectFactory ameeObjectFactory = AmeeObjectFactory
				.getInstance();
		final List<AmeeValue> values = new ArrayList<AmeeValue>();
		AmeeObject ameeObject;
		for (final AmeeObjectReference ref : getValueRefs()) {
			ameeObject = ameeObjectFactory.getObject(ref);
			if (ameeObject != null) {
				values.add((AmeeValue) ameeObject);
			}
		}
		return values;
	}

	public void setValues(final List<Choice> values) throws AmeeException {
		AmeeObjectFactory.getInstance().setItemValues(this, values);
	}

	public AmeeCategory getParent() throws AmeeException {
		AmeeCategory category = null;
		if (getParentRef() != null) {
			category = (AmeeCategory) AmeeObjectFactory.getInstance()
					.getObject(getParentRef());
		}
		return category;
	}

	public AmeeObjectReference getParentRef() {
		if (parentRef == null) {
			setParentRef();
		}
		return parentRef;
	}

	public void setParentRef(final AmeeObjectReference parentRef) {
		if (parentRef != null) {
			this.parentRef = parentRef;
		}
	}

	public AmeeValue getValue(final String localPath) throws AmeeException {
		return (AmeeValue) AmeeObjectFactory.getInstance().getObject(
				getUri() + "/" + localPath, AmeeObjectType.VALUE);
	}

	public String getLabel() throws AmeeException {
		if (!isFetched()) {
			fetch();
		}
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public List<AmeeObjectReference> getValueRefs() throws AmeeException {
		return getValueRefs(true);
	}

	public List<AmeeObjectReference> getValueRefs(
			final boolean fetchIfNotFetched) throws AmeeException {
		if (fetchIfNotFetched && !isFetched()) {
			fetch();
		}
		return valueRefs;
	}

	public void addValueRef(final AmeeObjectReference ref) {
		valueRefs.add(ref);
	}

	public void clearValueRefs() {
		valueRefs.clear();
	}

	public void setValueRefs(final List<AmeeObjectReference> valueRefs) {
		if (valueRefs != null) {
			this.valueRefs = valueRefs;
		}
	}
}