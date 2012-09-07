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

import com.amee.client.model.base.AmeeItem;
import com.amee.client.model.base.AmeeObject;
import com.amee.client.model.base.AmeeObjectReference;
import com.amee.client.model.base.AmeeObjectType;

public class AmeeDataItem extends AmeeItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 434533006830972628L;

	public AmeeDataItem() {
		super();
	}

	public AmeeDataItem(final AmeeObjectReference ref) {
		super(ref);
	}

	public AmeeDataItem(final String path, final AmeeObjectType objectType) {
		super(path, objectType);
	}

	public void populate(final AmeeDataItem copy) {
		super.populate(copy);
	}

	@Override
	public AmeeObject getCopy() {
		final AmeeDataItem copy = new AmeeDataItem();
		populate(copy);
		return copy;
	}

	@Override
	public void setParentRef() {
		setParentRef(new AmeeObjectReference(getObjectReference()
				.getParentUri(), AmeeObjectType.DATA_CATEGORY));
	}
}
