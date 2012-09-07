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
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.amee.client.AmeeException;
import com.amee.client.model.data.AmeeDataItem;
import com.amee.client.model.data.AmeeDrillDown;
import com.amee.client.util.Choice;

/**
 * This opens a window and allows the user to choose the fuel and size of car -
 * km per month is hard-wired to 1000. The combo boxes are populated with
 * choices using drill down.
 */
public class AmeeDrillExample extends AmeeExample {
	/**
	 * 
	 */
	private static final long serialVersionUID = -970592728861881106L;
	private AmeeDrillDown ameeDrillDown;

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
		new AmeeDrillExample(profileUID, "transport/car/generic");
		Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * Creates a new instance of AmeeDrillExample
	 */
	public AmeeDrillExample(final String profileUID, final String path) {
		super(profileUID, path);
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
		// First perform a drill down to find available choices for the category
		// specifed by path
		ameeDrillDown = objectFactory.getDrillDown(path + "/drill");
		while (ameeDrillDown.hasChoices()) { // If there are choices...
			final JComboBox jcb = new JComboBox(); // ... create a combo box to
													// store them
			jp.add(jcb);
			jcb.addItem(ameeDrillDown.getChoiceName()); // first item is the
														// choice name, e.g.
														// fuel
			// System.out.println("choice = "+ameeDrillDown.getChoiceName());
			final List<Choice> list = ameeDrillDown.getChoices();
			for (final Object elem : list) { // Now add all choice, for fuel
												// they are: diesel, petrol and
												// petrol hybrid
				// System.out.println("value = "+elem);
				jcb.addItem(elem.toString());
			}
			// In order to get the next level of choices for the drill down, we
			// select the first choice in the list
			ameeDrillDown.addSelection(ameeDrillDown.getChoiceName(),
					ameeDrillDown.getChoices().get(0).getValue());
			ameeDrillDown.fetch(); // the fetch() method forces a call to the
									// AMEE API
			jcb.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent evt) {
					try {
						processComboSelection(evt);
					} catch (final AmeeException ex) {
						ex.printStackTrace();
					}
				}
			});
		}
		// clear the drill down object so it can now be used for user selection
		ameeDrillDown.clearSelections();
		ameeDrillDown.clearChoices();
	}

	/** This method processes the selection in one of the combo boxes */
	@Override
	public void processComboSelection(final ActionEvent evt)
			throws AmeeException {
		// First get the name and value of the choice selected by the user
		final JComboBox jcb = (JComboBox) evt.getSource();
		final String name = jcb.getItemAt(0).toString();
		final String value = jcb.getSelectedItem().toString();
		System.out.println("name=" + name + ", value=" + value);

		// Now add the selection, first removing any existing selections of that
		// name
		final List<Choice> selections = ameeDrillDown.getSelections();
		Choice toReplace = null;
		for (final Choice sel : selections) {
			if (sel.getName().equals(name)) {
				toReplace = sel;
			}
		}
		if (toReplace != null) {
			selections.remove(toReplace);
			ameeDrillDown.setSelections(selections);
		}
		ameeDrillDown.addSelection(name, value);
		ameeDrillDown.fetch(); // the fetch() method forces a call to the AMEE
								// API
		final AmeeDataItem ameeDataItem = ameeDrillDown.getDataItem();
		if (ameeDataItem != null) {// if non null, the drill down is complete -
									// user has made all selections, no choices
									// left
			createProfileItem(ameeDataItem);

		} else {
			System.out
					.println("datauid is null - user still needs to make selections");
		}
	}

}
