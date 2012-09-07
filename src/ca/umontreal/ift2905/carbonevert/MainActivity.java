package ca.umontreal.ift2905.carbonevert;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TabHost tabHost = getTabHost();

		// Tab for Activities
		final TabSpec activitiespec = tabHost.newTabSpec("Activities");
		// setting Title and Icon for the Tab
		activitiespec.setIndicator("Activities");
		final Intent activitiesIntent = new Intent(this,
				ActivitiesActivity.class);
		activitiespec.setContent(activitiesIntent);

		// Tab for browse
		final TabSpec browsepec = tabHost.newTabSpec("Browse");
		browsepec.setIndicator("Browse");
		final Intent browseIntent = new Intent(this, BrowseActivity.class);
		browsepec.setContent(browseIntent);

		// Tab for favourites
		final TabSpec favouritespec = tabHost.newTabSpec("Favourites");
		favouritespec.setIndicator("Favourites");
		final Intent favouritesIntent = new Intent(this,
				FavouritesActivity.class);
		favouritespec.setContent(favouritesIntent);

		// Tab for favourites
		final TabSpec summaryspec = tabHost.newTabSpec("Summary");
		summaryspec.setIndicator("Summary");
		final Intent summarysIntent = new Intent(this, SummaryActivity.class);
		summaryspec.setContent(summarysIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(activitiespec); // Adding activities tab
		tabHost.addTab(browsepec); // Adding browse tab
		tabHost.addTab(favouritespec); // Adding favourites tab
		tabHost.addTab(summaryspec);// Adding summary tab
	}
}
