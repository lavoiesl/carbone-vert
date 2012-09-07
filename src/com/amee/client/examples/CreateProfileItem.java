/**
 * This file is part of the AMEE Java Client Library.
 *
 * Copyright (c) 2008 AMEE UK Ltd. (http://www.amee.com)
 *
 * The AMEE Java Client Library is free software, released under the MIT
 * license. See mit-license.txt for details.
 */

package com.amee.client.examples;

import java.util.ArrayList;
import java.util.List;

import com.amee.client.AmeeException;
import com.amee.client.model.base.AmeeValue;
import com.amee.client.model.profile.AmeeProfile;
import com.amee.client.model.profile.AmeeProfileCategory;
import com.amee.client.model.profile.AmeeProfileItem;
import com.amee.client.model.profile.ReturnNote;
import com.amee.client.model.profile.ReturnValue;
import com.amee.client.service.AmeeContext;
import com.amee.client.service.AmeeObjectFactory;
import com.amee.client.util.Choice;

public class CreateProfileItem {

	public static void main(final String[] args) throws AmeeException {

		// Set up AMEE connection
		AmeeContext.getInstance().setUsername("username-here");
		AmeeContext.getInstance().setPassword("password-here");
		AmeeContext.getInstance().setBaseUrl("https://stage.amee.com");

		// Parameters
		final String profileUID = "67B1817A59A0"; // Change this to one that
													// exists for you.
		final String profileCategory = "home/energy/quantity";
		final String dataItemUID = "66056991EE23"; // The data item UID that you
													// want to create a profile
													// item for.

		// Get profile object
		final AmeeProfile profile = AmeeObjectFactory.getInstance().getProfile(
				profileUID);
		// Get category
		final AmeeProfileCategory cat = AmeeObjectFactory.getInstance()
				.getProfileCategory(profile, profileCategory);
		// Set options for new item
		final List<Choice> values = new ArrayList<Choice>();
		values.add(new Choice("energyConsumption", 42));
		values.add(new Choice("energyConsumptionUnit", "kWh"));
		values.add(new Choice("energyConsumptionPerUnit", "month"));
		// Create the item
		AmeeProfileItem item = cat.addProfileItem(dataItemUID, values);
		printProfileItem(item);

		// You can also create items directly on a profile object, avoiding a
		// GET to the category
		values.add(new Choice("name", "no_category_object"));
		item = profile.addProfileItem(profileCategory, dataItemUID, values);
		printProfileItem(item);

		// Or even without any objects at all, avoiding a GET to the profile as
		// well.
		values.remove(values.size() - 1);
		values.add(new Choice("name", "no_objects_at_all"));
		item = AmeeObjectFactory.getInstance().addProfileItem(profileUID,
				profileCategory, dataItemUID, values);
		printProfileItem(item);

	}

	static void printProfileItem(final AmeeProfileItem item)
			throws AmeeException {
		System.out.println("Created profile item OK");
		System.out.printf(" - profile item UID: %s\n", item.getUid());
		System.out.printf(" - data item UID: %s\n", item.getDataItemRef());
		// Print item values
		for (final AmeeValue value : item.getValues()) {
			if (!value.getValue().equals("")) {
				System.out.printf(" - %s: %s %s/%s\n", value.getName(),
						value.getValue(), value.getUnit(), value.getPerUnit());
			}
		}
		System.out.printf(" Results: \n");
		for (final ReturnValue value : item.getReturnValues()) {
			System.out.printf(" - %s: %s %s/%s\n", value.getName(),
					value.getValue(), value.getUnit(), value.getPerUnit());
		}
		for (final ReturnNote note : item.getNotes()) {
			System.out.printf(" - %s: %s\n", note.getName(), note.getNote());
		}
	}
}
