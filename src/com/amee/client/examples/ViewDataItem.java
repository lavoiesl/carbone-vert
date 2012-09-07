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
import com.amee.client.model.data.AmeeDataCategory;
import com.amee.client.model.data.AmeeDataItem;
import com.amee.client.service.AmeeContext;
import com.amee.client.service.AmeeObjectFactory;

public class ViewDataItem {

	public static void main(final String[] args) throws AmeeException {

		// Set up AMEE connection
		AmeeContext.getInstance().setUsername("username-here");
		AmeeContext.getInstance().setPassword("password-here");
		AmeeContext.getInstance().setBaseUrl("http://stage.amee.com");

		// Parameters
		final String dataCategory = "home/energy/quantity";
		final String dataItemLabel = "gas";

		// Get category
		final AmeeDataCategory cat = AmeeObjectFactory.getInstance()
				.getDataCategory(dataCategory);
		for (final AmeeDataItem item : cat.getDataItems()) {
			if (item.getLabel().compareTo(dataItemLabel) == 0) {
				// Print data
				System.out.println("---------------------");
				System.out.print("Name: ");
				System.out.println(item.getName());
				System.out.print("Path: ");
				System.out.println(item.getUri());
				System.out.print("Label: ");
				System.out.println(item.getLabel());
				System.out.print("UID: ");
				System.out.println(item.getUid());
				System.out.println("Values:");
				for (final AmeeValue v : item.getValues()) {
					System.out.print("  - ");
					System.out.print(v.getName());
					System.out.print(": ");
					System.out.println(v.getValue());
				}
			}
		}
	}
}
