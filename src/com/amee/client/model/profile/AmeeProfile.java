/**
 * This file is part of the AMEE Java Client Library.
 *
 * Copyright (c) 2008 AMEE UK Ltd. (http://www.amee.com)
 *
 * The AMEE Java Client Library is free software, released under the MIT
 * license. See mit-license.txt for details.
 */

package com.amee.client.model.profile;

import java.io.Serializable;
import java.util.List;

import com.amee.client.AmeeException;
import com.amee.client.model.base.AmeeObject;
import com.amee.client.model.base.AmeeObjectReference;
import com.amee.client.model.base.AmeeObjectType;
import com.amee.client.service.AmeeObjectFactory;
import com.amee.client.util.Choice;

public class AmeeProfile extends AmeeProfileCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6626013183685443817L;
	private String validFrom = "";

	public AmeeProfile() {
		super();
	}

	public AmeeProfile(final AmeeObjectReference ref) {
		super(ref);
	}

	public AmeeProfile(final String path, final AmeeObjectType objectType) {
		super(path, objectType);
	}

	public void populate(final AmeeProfile copy) {
		super.populate(copy);
		copy.setValidFrom(validFrom);
	}

	@Override
	public AmeeObject getCopy() {
		final AmeeProfile copy = new AmeeProfile();
		populate(copy);
		return copy;
	}

	@Override
	public void setProfileRef() {
		setProfileRef(null);
	}

	@Override
	public void setParentRef() {
		setParentRef(null);
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(final String validFrom) {
		if (validFrom != null) {
			this.validFrom = validFrom;
		}
	}

	public AmeeProfileItem addProfileItem(final String categoryUri,
			final String dataItemUid, final List<Choice> values)
			throws AmeeException {
		return AmeeObjectFactory.getInstance().addProfileItem(this,
				categoryUri, dataItemUid, values);
	}

}