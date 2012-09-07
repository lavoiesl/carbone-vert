/**
 * This file is part of the AMEE Java Client Library.
 *
 * Copyright (c) 2008 AMEE UK Ltd. (http://www.amee.com)
 *
 * The AMEE Java Client Library is free software, released under the MIT
 * license. See mit-license.txt for details.
 */

package com.amee.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.amee.client.model.base.AmeeValue;
import com.amee.client.model.data.AmeeDataItem;
import com.amee.client.model.data.AmeeDrillDown;
import com.amee.client.model.profile.AmeeProfile;
import com.amee.client.model.profile.AmeeProfileCategory;
import com.amee.client.model.profile.AmeeProfileItem;
import com.amee.client.service.AmeeContext;
import com.amee.client.service.AmeeObjectFactory;
import com.amee.client.util.Choice;

/**
 * This class copies from one profile to another. It can copy profiles between
 * two different servers, e.g. stage and live.
 * 
 * KNOWN ISSUES: - metadata item can't be copied - should warn if toProfile or
 * item exists - name of create profiled items seems to be set to the
 * dataItemUid???
 * 
 * @author nalu
 */
public class ProfileCopy {

	public static FileWriter writer;
	private final String toProfileUid, fromProfileUid;
	private final String fromUrl, toUrl;
	private final Map<String, String> dataItemCache = new HashMap<String, String>();
	private static boolean isTest = false;

	/**
	 * The fromUid must not be null, but the toUid can be null in which case the
	 * new profile will be created.
	 */
	public ProfileCopy(final String fromUid, final String toUid,
			final String fromUrl, final String toUrl) {
		fromProfileUid = fromUid;
		toProfileUid = toUid;
		this.fromUrl = fromUrl;
		this.toUrl = toUrl;
	}

	/**
	 * 
	 * @param path
	 *            A path, e.g. home/lighting - leave off first and last slashes.
	 * @return The number of profile items copied.
	 * @throws com.amee.client.AmeeException
	 */
	public int copyCategory(final String path) throws AmeeException {
		AmeeProfile profile;
		final AmeeObjectFactory objectFactory = AmeeObjectFactory.getInstance();

		// Get the existing items
		AmeeContext.getInstance().setBaseUrl(fromUrl);
		profile = objectFactory.getProfile(fromProfileUid);
		AmeeProfileCategory profileCategory = objectFactory.getProfileCategory(
				profile, path);
		profileCategory.fetch();// the fetch() method forces a call to the AMEE
								// API
		final List<AmeeProfileItem> profileItems = profileCategory
				.getProfileItems();

		String fromDataItemUid;
		if (profileItems.isEmpty()) {
			return 0;
		}
		for (final AmeeProfileItem item : profileItems) {
			item.fetch();
			item.getDataItem();
		}

		// create the new ones
		AmeeContext.getInstance().setBaseUrl(toUrl);

		profile = objectFactory.getProfile(toProfileUid);

		for (final AmeeProfileItem fromProfileItem : profileItems) {
			try {
				fromDataItemUid = fromProfileItem.getDataItem().getUid();
				String toDataItemUid = dataItemCache.get(fromDataItemUid);
				if (toDataItemUid == null) {
					toDataItemUid = doDrillDown(path, fromProfileItem
							.getDataItem().getValues());// ***1
					dataItemCache.put(fromDataItemUid, toDataItemUid);
				}

				profileCategory = objectFactory.getProfileCategory(profile,
						path);
				// using data item uid explicitly will need to drill and cache
				// values
				final List<Choice> values = new ArrayList<Choice>();
				for (final AmeeValue value : fromProfileItem.getValues()) {
					values.add(new Choice(value.getName(), value.getValue()));
					// System.err.println(value.getName()+"="+value.getValue());
				}
				/*
				 * System.err.println("value.size()=" + values.size()); if
				 * (values.size() > 0) { toProfileItem.setValues(values); }
				 */
				values.add(new Choice("name", fromProfileItem.getName()));
				// System.err.println("toDataItemUid="+toDataItemUid);
				writer.write("cache," + path + "," + fromDataItemUid + ","
						+ toDataItemUid + "\n");
				if (!isTest) {
					final AmeeProfileItem toProfileItem = profileCategory
							.addProfileItem(toDataItemUid, values);
					final String s = path + "," + fromProfileItem.getUid()
							+ "," + toProfileItem.getUid();
					System.err.println(s);
					writer.write(s + "\n");
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return profileItems.size();
	}

	/**
	 * Given a set of data item values and a path, this drills down and returns
	 * the data item uid on the current server.
	 * 
	 * @param path
	 * @param values
	 * @return
	 * @throws com.amee.client.AmeeException
	 */
	private String doDrillDown(final String path, final List<AmeeValue> values)
			throws AmeeException {
		final Map<String, String> drillMap = new HashMap<String, String>();
		for (final AmeeValue value : values) {
			drillMap.put(value.getLocalPath(), value.getValue());
		}
		// System.err.println("drillMap=" + drillMap);

		final AmeeDrillDown ameeDrillDown = AmeeObjectFactory.getInstance()
				.getDrillDown(path + "/drill");
		while (ameeDrillDown.hasChoices()) {
			final String choiceName = ameeDrillDown.getChoiceName();
			final String selection = drillMap
					.get(ameeDrillDown.getChoiceName());
			// System.err.println("choiceName=" + choiceName);
			// System.err.println("drillSection=" + selection);
			ameeDrillDown.addSelection(choiceName, selection);
			ameeDrillDown.fetch();
		}
		final AmeeDataItem item = ameeDrillDown.getDataItem();
		// System.err.println("dataItemUid=" + item.getUid());
		return item.getUid();
	}

	public static Map<String, String> getUids(final String firstUid,
			final File file) {
		return getUids(firstUid, file, null);
	}

	/**
	 * 
	 * @param firstUid
	 *            Ignore all uids in the file until this one.
	 * @param file
	 *            A text file which contains a 12 digit profile uid at the start
	 *            of each line, e.g. B8380EED27F2 F091E18C5C03 1A7A2F6654DE
	 *            072B0A2FADB0 2AF8684FDA4A
	 * @param blacklist
	 *            A text file, same format as file, that contains profile uids
	 *            to ignore.
	 * @return A map in which keys are Strings containing the uids. The map
	 *         values are null, though the code can be tweaked to read toUids.
	 */
	public static Map<String, String> getUids(final String firstUid,
			final File file, final File blacklist) {
		Map<String, String> uidMap, blacklistMap = null;
		if (blacklist != null) {
			blacklistMap = loadUidsFromCSV(firstUid, blacklist, null);
		}
		uidMap = loadUidsFromCSV(firstUid, file, blacklistMap);

		return uidMap;
	}

	public static Map<String, String> loadUidsFromCSV(final String firstUid,
			final File file, final Map<String, String> blacklistMap) {
		System.err.println("Loading uids from " + file + "...");
		// ArrayList list = new ArrayList();
		final Map<String, String> map = new LinkedHashMap<String, String>();
		boolean foundFirst = false;

		try {
			final FileReader fr = new FileReader(file);
			final BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				line = line.trim();
				if (line.length() > 0) {
					// list.add(line.trim().substring(0, 12));
					// line = line.trim();
					final String fromUid = line.substring(0, 12);
					final String toUid = null;// line.substring(21, 33);
					if (firstUid == null || fromUid.equals(firstUid)) {
						foundFirst = true;
					}
					if (foundFirst) {
						if (blacklistMap == null
								|| blacklistMap.containsKey(fromUid) == false) {
							map.put(fromUid, toUid);
						}
					}
				}
				line = br.readLine();
			}
			br.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.err.println("...finished reading uids - map.size() = "
				+ map.size());
		return map;
	}

	/**
	 * The main method implements an example where profiles are copied from
	 * stage to live for an AMEE user specified in the command line args. You'll
	 * need to supply a list of valid profile uids in the uid_list.txt file -
	 * see javadoc for getUids.
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		if (args.length != 2) {
			System.err.println("Required args: username password");
			System.exit(0);
		}
		// isTest = true; //Don't actually copy
		final File dir = new File(
				"/home/nalu/docs/amee/clients/est/stage2live/13nov08");
		try {
			writer = new FileWriter(new File(dir, "output_cache.txt"));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		AmeeContext.getInstance().setUsername(args[0]);
		AmeeContext.getInstance().setPassword(args[1]);

		final String[] paths = {// metadata doesn't work
		"home/appliances/computers/generic", "home/appliances/cooking",
				"home/appliances/entertainment/generic",
				"home/appliances/kitchen/generic",
				"home/appliances/televisions/generic",
				"home/energy/electricity", "home/energy/electricityiso",
				"home/energy/quantity", "home/energy/uk/price",
				"home/energy/uk/seasonal", "home/heating", "home/lighting",
				"transport/car/generic", "transport/motorcycle/generic",
				"transport/plane/generic", "transport/bus/generic",
				"transport/train/generic", "transport/taxi/generic",
				"transport/other", "home/appliances/computers/generic",
				"home/appliances/entertainment/generic",
				"home/appliances/kitchen/generic",
				"home/appliances/televisions/generic" };
		// this file contains uids to be copied from stage
		final File file = new File(dir, "ezameeuser_est_live.csv");
		// this file contains uids that should be ignored
		final File blacklist = new File(dir, "blacklist.csv");
		final Map<String, String> uids = getUids(null, file, blacklist);
		// Map uids = new HashMap();
		// uids.put("516F8D8C179F", null);
		// ArrayList uids = new ArrayList();
		// uids.add("F1E658758A87");
		// String[] paths = {"home/appliances/cooking"};
		final Iterator<String> iter = uids.keySet().iterator();
		// iter.next();//skip the first one
		int nProfile = 1;
		final long start = System.currentTimeMillis();
		long last = start;
		while (iter.hasNext()) {
			final long now = System.currentTimeMillis();
			final long average = (now - start) / nProfile;
			long eta = average * (uids.size() - nProfile);
			eta /= 60000l;
			final long lastDuration = now - last;
			last = now;
			final String sh = "=== " + nProfile + ", last=" + lastDuration
					+ ", av=" + average + ", eta=" + eta;
			System.err.println(sh);
			try {
				writer.write(sh + "\n");
				final String fromUid = iter.next();
				// String toUid = (String) uids.get(fromUid);
				AmeeContext.getInstance().setBaseUrl("http://live.amee.com");
				final AmeeProfile toProfile = AmeeObjectFactory.getInstance()
						.addProfile();
				final String s = "from " + fromUid + " to "
						+ toProfile.getUid();
				System.err.println(s);
				writer.write(s + "\n");
				// System.err.println("from " + fromUid + " to " + toUid);

				final ProfileCopy pc = new ProfileCopy(fromUid,
						toProfile.getUid(), "http://stage.amee.com",
						"http://live.amee.com");
				for (final String path : paths) {
					final int n = pc.copyCategory(path);
					System.err.println(path + "," + n);
				}
			} catch (final AmeeException ex) {
				ex.printStackTrace();

			} catch (final Exception ex) {
				ex.printStackTrace();
			}
			nProfile++;
			/*
			 * if (nProfile > 3) { break; }
			 */
		}
		try {
			writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
