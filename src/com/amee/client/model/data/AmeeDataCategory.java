/**
 * This file is part of the AMEE Java Client Library.
 *
 * Copyright (c) 2008 AMEE UK Ltd. (http://www.amee.com)
 *
 * The AMEE Java Client Library is free software, released under the MIT
 * license. See mit-license.txt for details.
 */

package com.amee.client.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.amee.client.AmeeException;
import com.amee.client.model.base.AmeeCategory;
import com.amee.client.model.base.AmeeItem;
import com.amee.client.model.base.AmeeObject;
import com.amee.client.model.base.AmeeObjectReference;
import com.amee.client.model.base.AmeeObjectType;
import com.amee.client.service.AmeeObjectFactory;
import com.amee.client.util.Choice;

public class AmeeDataCategory extends AmeeCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 269124979868984983L;

	public AmeeDataCategory() {
		super();
	}

	public AmeeDataCategory(final AmeeObjectReference ref) {
		super(ref);
	}

	public AmeeDataCategory(final String path, final AmeeObjectType objectType) {
		super(path, objectType);
	}

	public void populate(final AmeeDataCategory copy) {
		super.populate(copy);
	}

	@Override
	public AmeeObject getCopy() {
		final AmeeDataCategory copy = new AmeeDataCategory();
		populate(copy);
		return copy;
	}

	@Override
	public void setParentRef() {
		if (getUri().equals("data")) {
			setParentRef(null);
		} else {
			setParentRef(new AmeeObjectReference(getObjectReference()
					.getParentUri(), AmeeObjectType.DATA_CATEGORY));
		}
	}

	@Override
	public AmeeCategory getNewChildCategory(final AmeeObjectReference ref) {
		return new AmeeDataCategory(ref);
	}

	@Override
	public AmeeObjectType getChildCategoryObjectType() {
		return AmeeObjectType.DATA_CATEGORY;
	}

	@Override
	public AmeeItem getNewChildItem(final AmeeObjectReference ref) {
		return new AmeeDataItem(ref);
	}

	@Override
	public AmeeObjectType getChildItemObjectType() {
		return AmeeObjectType.DATA_ITEM;
	}

	public List<AmeeDataCategory> getDataCategories() throws AmeeException {
		final List<AmeeDataCategory> dataCategories = new ArrayList<AmeeDataCategory>();
		for (final AmeeCategory category : super.getCategories()) {
			dataCategories.add((AmeeDataCategory) category);
		}
		return dataCategories;
	}

	public List<AmeeDataItem> getDataItems() throws AmeeException {
		final List<AmeeDataItem> dataItems = new ArrayList<AmeeDataItem>();
		for (final AmeeItem item : super.getItems()) {
			dataItems.add((AmeeDataItem) item);
		}
		return dataItems;
	}

	public AmeeDataItem addDataItem(final String dataItemDefinitionUid)
			throws AmeeException {
		return addDataItem(dataItemDefinitionUid, null);
	}

	public AmeeDataItem addDataItem(final String dataItemDefinitionUid,
			final List<Choice> values) throws AmeeException {
		return AmeeObjectFactory.getInstance().getDataItem(this,
				dataItemDefinitionUid, values);
	}

}