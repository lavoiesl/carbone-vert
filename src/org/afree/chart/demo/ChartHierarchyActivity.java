/* ===========================================================
 * AFreeChart : a free chart library for Android(tm) platform.
 *              (based on JFreeChart and JCommon)
 * ===========================================================
 *
 * (C) Copyright 2010, by ICOMSYSTECH Co.,Ltd.
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:
 *    AFreeChart: http://code.google.com/p/afreechart/
 *    JFreeChart: http://www.jfree.org/jfreechart/index.html
 *    JCommon   : http://www.jfree.org/jcommon/index.html
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * [Android is a trademark of Google Inc.]
 *
 * -----------------
 * ChartHierarchyActivity.java
 * -----------------
 * (C) Copyright 2010, 2011, by ICOMSYSTECH Co.,Ltd.
 *
 * Original Author:  Niwano Masayoshi (for ICOMSYSTECH Co.,Ltd);
 * Contributor(s):   Yamakami Souichirou (for ICOMSYSTECH Co.,Ltd);
 *
 * Changes
 * -------
 * 19-Nov-2010 : Version 0.0.1 (NM);
 * 18-Oct-2011 : Change the package name (SY);
 */

package org.afree.chart.demo;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * ChartHierarchyActivity
 */
public class ChartHierarchyActivity extends ListActivity {

	private final String TARGET_PACKAGE_NAME = "org.afree.chart";

	public static final String CTEGORY_NAME = "org.afree.chart.demo";

	/**
	 * Called when the activity is starting.
	 * 
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		String path = intent.getStringExtra("org.afree.demo.Path");

		if (path == null) {
			path = "";
		}

		setListAdapter(new SimpleAdapter(this, getData(path),
				android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] { android.R.id.text1 }));
		getListView().setTextFilterEnabled(true);
	}

	protected List<Map<String, Object>> getData(final String prefix) {
		final List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();

		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(CTEGORY_NAME);

		final PackageManager pm = getPackageManager();
		final List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

		if (null == list) {
			return myData;
		}

		String[] prefixPath;

		if (prefix.equals("")) {
			prefixPath = null;
		} else {
			prefixPath = prefix.split("/");
		}

		final int len = list.size();

		final Map<String, Boolean> entries = new HashMap<String, Boolean>();

		for (int i = 0; i < len; i++) {
			final ResolveInfo info = list.get(i);

			// Narrow down package
			if (!info.activityInfo.applicationInfo.packageName
					.equals(TARGET_PACKAGE_NAME)) {
				continue;
			}

			final CharSequence labelSeq = info.loadLabel(pm);
			final String label = labelSeq != null ? labelSeq.toString()
					: info.activityInfo.name;

			if (prefix.length() == 0 || label.startsWith(prefix)) {

				final String[] labelPath = label.split("/");

				final String nextLabel = prefixPath == null ? labelPath[0]
						: labelPath[prefixPath.length];

				if ((prefixPath != null ? prefixPath.length : 0) == labelPath.length - 1) {
					addItem(myData,
							nextLabel,
							activityIntent(
									info.activityInfo.applicationInfo.packageName,
									info.activityInfo.name));

				} else {
					if (entries.get(nextLabel) == null) {
						addItem(myData, nextLabel,
								browseIntent(prefix.equals("") ? nextLabel
										: prefix + "/" + nextLabel));
						entries.put(nextLabel, true);
					}
				}
			}
		}

		Collections.sort(myData, sDisplayNameComparator);

		return myData;
	}

	private final static Comparator<Map<String, Object>> sDisplayNameComparator = new Comparator<Map<String, Object>>() {
		private final Collator collator = Collator.getInstance();

		public int compare(final Map<String, Object> map1,
				final Map<String, Object> map2) {
			return collator.compare(map1.get("title"), map2.get("title"));
		}
	};

	protected Intent activityIntent(final String pkg, final String componentName) {
		final Intent result = new Intent();
		result.setClassName(pkg, componentName);
		return result;
	}

	protected Intent browseIntent(final String path) {
		final Intent result = new Intent();
		result.setClass(this, ChartHierarchyActivity.class);
		result.putExtra("org.afree.demo.Path", path);
		return result;
	}

	protected void addItem(final List<Map<String, Object>> myData,
			final String name, final Intent intent) {
		final Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put("intent", intent);
		myData.add(temp);
	}

	@Override
	protected void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
		@SuppressWarnings("unchecked")
		final Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		final Intent intent = (Intent) map.get("intent");
		startActivity(intent);
	}

}
