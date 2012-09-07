/**
 * This file is part of the AMEE Java Client Library.
 *
 * Copyright (c) 2008 AMEE UK Ltd. (http://www.amee.com)
 *
 * The AMEE Java Client Library is free software, released under the MIT
 * license. See mit-license.txt for details.
 */

package com.amee.client.examples;

import com.amee.client.AmeeException;
import com.amee.client.model.base.AmeeValue;
import com.amee.client.model.profile.AmeeProfile;
import com.amee.client.model.profile.AmeeProfileCategory;
import com.amee.client.model.profile.AmeeProfileItem;
import com.amee.client.service.AmeeContext;
import com.amee.client.service.AmeeObjectFactory;

public class ViewProfileItem {

	public static void main(final String[] args) throws AmeeException {

		// Set up AMEE connection
		AmeeContext.getInstance().setUsername("username-here");
		AmeeContext.getInstance().setPassword("password-here");
		AmeeContext.getInstance().setBaseUrl("http://stage.amee.com");

		// Parameters
		final String profileUID = "05ECD93B7752"; // Change this to one that
													// exists for you.
		final String profileCategory = "home/energy/quantity";
		final String itemUID = "B358BC0CCC23"; // Change this to an item UID
												// that exists in the above
												// ProfileCategory.

		// Get profile object
		final AmeeProfile profile = AmeeObjectFactory.getInstance().getProfile(
				profileUID);
		// Get category
		final AmeeProfileCategory cat = AmeeObjectFactory.getInstance()
				.getProfileCategory(profile, profileCategory);
		// Find the item we want
		for (final AmeeProfileItem item : cat.getProfileItems()) {
			if (item.getUid().compareTo(itemUID) == 0) {
				// Print data
				System.out.print(" - name: ");
				System.out.println(item.getName());
				for (final AmeeValue value : item.getValues()) {
					System.out.printf(" - %s (%s): %s %s/%s\n",
							value.getName(), value.getUid(), value.getValue(),
							value.getUnit(), value.getPerUnit());
				}
				System.out.printf(" - CO2 total: %s %s\n", item.getAmount(),
						item.getAmountUnit());
			}
		}
	}
}
