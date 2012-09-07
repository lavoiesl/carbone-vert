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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.amee.client.AmeeException;
import com.amee.client.model.base.AmeeObject;
import com.amee.client.model.base.AmeeObjectReference;
import com.amee.client.model.base.AmeeObjectType;
import com.amee.client.service.AmeeObjectFactory;
import com.amee.client.util.Choice;

public class AmeeDrillDown extends AmeeObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6906398515968603130L;
	private String choiceName = "";
	private List<Choice> choices = new ArrayList<Choice>();
	private List<Choice> selections = new ArrayList<Choice>();

	public AmeeDrillDown() {
		super();
	}

	public AmeeDrillDown(final AmeeObjectReference ref) {
		super(ref);
	}

	public AmeeDrillDown(final String path, final AmeeObjectType objectType) {
		super(path, objectType);
	}

	public void populate(final AmeeDrillDown copy) {
		super.populate(copy);
		copy.setChoiceName(choiceName);
		copy.setChoices(new ArrayList<Choice>(choices));
		copy.setSelections(new ArrayList<Choice>(selections));
	}

	@Override
	public AmeeObject getCopy() {
		final AmeeDrillDown copy = new AmeeDrillDown();
		populate(copy);
		return copy;
	}

	public void addSelection(final Choice choice) {
		selections.add(choice);
	}

	public void addSelection(final String name, final String value) {
		addSelection(new Choice(name, value));
	}

	public void clearSelections() {
		selections.clear();
	}

	public void addChoice(final Choice choice) {
		choices.add(choice);
	}

	public void addChoice(final String name, final String value) {
		addChoice(new Choice(name, value));
	}

	public void clearChoices() {
		choices.clear();
	}

	public boolean hasChoices() {
		return !getChoices().isEmpty() && !getChoiceName().equals("uid");
	}

	public boolean isDataItemFound() {
		return getDataItemPathSegment() != null;
	}

	public String getDataItemPathSegment() {
		// a uid is available if choices contains at least one item and the
		// current choiceName is 'uid'
		// Special case: using a uid to work with a metadata item won't work,
		// instead return its path, which is 'metadata'
		if (getParentUri().equals("data/metadata")) {
			return "metadata";
		} else if (!getChoices().isEmpty() && getChoiceName().equals("uid")) {
			return getChoices().get(0).getValue();
		} else {
			return null;
		}
	}

	public AmeeDataItem getDataItem() throws AmeeException {
		AmeeDataItem ameeDataItem = null;
		final String uid = getDataItemPathSegment();
		if (uid != null) {
			ameeDataItem = (AmeeDataItem) AmeeObjectFactory.getInstance()
					.getObject(
							new AmeeObjectReference(getParentUri() + "/" + uid,
									AmeeObjectType.DATA_ITEM));
		}
		return ameeDataItem;
	}

	public String getNewUri() throws AmeeException {
		String params = "";
		String uri = getUri();
		if (uri == null || uri.length() == 0) {
			throw new AmeeException("Could not create URI.");
		}
		// get rid of old params
		final int pos = uri.indexOf("?");
		if (pos >= 0) {
			uri = uri.substring(0, pos);
		}
		// create new params
		for (final Choice selection : getSelections()) {
			try {
				params += "&" + URLEncoder.encode(selection.getName(), "UTF-8")
						+ "="
						+ URLEncoder.encode(selection.getValue(), "UTF-8");
			} catch (final UnsupportedEncodingException e) {
				// this is never going to happen
				throw new AmeeException("Caught UnsupportedEncodingException: "
						+ e.getMessage());
			}
		}
		if (params.startsWith("&")) {
			params = "?" + params.substring(1);
		}
		// new uri is old path + new params
		return uri + params;
	}

	@Override
	public void fetch() throws AmeeException {
		setObjectReference(new AmeeObjectReference(getNewUri(),
				AmeeObjectType.DRILL_DOWN));
		super.fetch();
	}

	public String getChoiceName() {
		return choiceName;
	}

	public void setChoiceName(final String choiceName) {
		if (choiceName != null) {
			this.choiceName = choiceName;
		}
	}

	public List<Choice> getChoices() {
		return new ArrayList<Choice>(choices);
	}

	public void setChoices(final List<Choice> choices) {
		if (choices != null) {
			this.choices = choices;
		}
	}

	public List<Choice> getSelections() {
		return new ArrayList<Choice>(selections);
	}

	public void setSelections(final List<Choice> selections) {
		if (selections != null) {
			this.selections = selections;
		}
	}
}