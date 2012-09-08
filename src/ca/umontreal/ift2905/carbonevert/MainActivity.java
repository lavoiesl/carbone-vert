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
		activitiespec.setIndicator("Activities", getResources().getDrawable(android.R.drawable.ic_menu_gallery));
		final Intent activitiesIntent = new Intent(this, ActivitiesActivity.class);
		activitiespec.setContent(activitiesIntent);

		// Tab for browse
		final TabSpec browsepec = tabHost.newTabSpec("Browse");
		browsepec.setIndicator("Browse", getResources().getDrawable(android.R.drawable.ic_search_category_default));
		final Intent browseIntent = new Intent(this, BrowseActivity.class);
		browsepec.setContent(browseIntent);

		// Tab for favorites
		final TabSpec favoritespec = tabHost.newTabSpec("Favorites");
		favoritespec.setIndicator("Favorites", getResources().getDrawable(android.R.drawable.btn_star_big_on));
		final Intent favoritesIntent = new Intent(this, FavoritesActivity.class);
		favoritespec.setContent(favoritesIntent);

		// Tab for summary
		final TabSpec summaryspec = tabHost.newTabSpec("Summary");
		summaryspec.setIndicator("Summary", getResources().getDrawable(android.R.drawable.ic_dialog_info));
		final Intent summarysIntent = new Intent(this, SummaryActivity.class);
		summaryspec.setContent(summarysIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(browsepec); // Adding browse tab
		tabHost.addTab(favoritespec); // Adding favorites tab
		tabHost.addTab(activitiespec); // Adding activities tab
		tabHost.addTab(summaryspec);// Adding summary tab
	}
}
