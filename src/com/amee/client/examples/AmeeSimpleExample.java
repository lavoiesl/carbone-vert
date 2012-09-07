/**
 * This file is part of the AMEE Java Client Library.
 *
 * Copyright (c) 2008 AMEE UK Ltd. (http://www.amee.com)
 *
 * The AMEE Java Client Library is free software, released under the MIT
 * license. See mit-license.txt for details.
 */

package com.amee.client.examples;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.amee.client.AmeeException;
import com.amee.client.model.data.AmeeDataItem;
import com.amee.client.model.data.AmeeDrillDown;

/**
 * This opens a window and allows the user to choose the fuel and size of car -
 * km per month is hard-wired to 1000. The possible choices of fuel and size are
 * hardcoded. See AmeeDrillExample for how to use the drill down to dynamically
 * discover choices.
 */
public class AmeeSimpleExample extends AmeeExample {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6341046720736103248L;
	private JComboBox fuelBox;
	private JComboBox sizeBox;

	public AmeeSimpleExample(final String profileUID, final String path) {
		super(profileUID, path);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String[] args) {
		if (args.length != 3) {
			System.err.println("Required args: username password AMEE-URL");
			System.exit(0);
		}
		AmeeExample.setLoginDetails(args[0], args[1], args[2]);

		// NOTE: Please replace profileUid with one that you know exists,
		// otherwise
		// a new profile will be created everytime. Look in the stdout and
		// it'll give you the uid of a newly created profile
		// String profileUID="2B9DFBFE86FE";
		final String profileUID = "74050B631066";
		new AmeeSimpleExample(profileUID, "transport/car/generic");
		Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * Creates combo boxes by drilling down. Note: the choices for subsequent
	 * combo boxes will be for the first choice in the preceding combo box. For
	 * example, if first combo box is Diesel|Petrol|Petrol Hybrid then the
	 * second combo box will be populated with options for Diesel. This might
	 * not work for other profile categories.
	 * 
	 * @param jf
	 *            The JFrame to which the combo boxes will be added.
	 */
	@Override
	public void addComboBoxes() throws AmeeException {
		// Combo boxes will be added to this panel
		final JPanel jp = new JPanel();
		getContentPane().add(jp);

		final String[] fuelChoices = { "fuel", "petrol", "diesel" };
		fuelBox = new JComboBox(fuelChoices);
		final String[] sizeChoices = { "size", "small", "medium", "large" };
		sizeBox = new JComboBox(sizeChoices);

		jp.add(fuelBox);
		jp.add(sizeBox);

		final ActionListener al = new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				try {
					processComboSelection(evt);
				} catch (final AmeeException ex) {
					ex.printStackTrace();
				}
			}
		};

		fuelBox.addActionListener(al);
		sizeBox.addActionListener(al);
	}

	/** This method processes the selection in one of the combo boxes */
	@Override
	public void processComboSelection(final ActionEvent evt)
			throws AmeeException {
		// First get the name and value of the choice selected by the user
		if (fuelBox.getSelectedIndex() == 0 || sizeBox.getSelectedIndex() == 0) {
			System.out.println("Further selections to be made.");
			return; // user still has to make selection(s)
		}

		final String fuel = fuelBox.getSelectedItem().toString();
		final String size = sizeBox.getSelectedItem().toString();

		System.out.println("fuel=" + fuel + ", size=" + size);

		final AmeeDrillDown ameeDrillDown = objectFactory.getDrillDown(path
				+ "/drill");
		// Now add the selection, first removing any existing selections of that
		// name
		ameeDrillDown.addSelection("fuel", fuel);
		ameeDrillDown.addSelection("size", size);
		ameeDrillDown.fetch(); // the fetch() method forces a call to the AMEE
								// API
		final AmeeDataItem ameeDataItem = ameeDrillDown.getDataItem();
		if (ameeDataItem != null) {
			createProfileItem(ameeDataItem);
		} else {
			System.out
					.println("duid is null - a selected combo box items is incorrect");
		}
	}

}
