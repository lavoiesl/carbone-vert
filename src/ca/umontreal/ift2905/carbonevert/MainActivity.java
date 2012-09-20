package ca.umontreal.ift2905.carbonevert;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TabHost tabHost = getTabHost();
		
		tabHost.addTab(tabHost.newTabSpec("Browse")
			        .setIndicator("Browse", getResources().getDrawable(android.R.drawable.ic_search_category_default))
			        .setContent(new Intent(this, BrowseActivity.class)
			        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		
		tabHost.addTab(tabHost.newTabSpec("Favorites")
		        .setIndicator("Favorites", getResources().getDrawable(android.R.drawable.btn_star_big_on))
		        .setContent(new Intent(this, FavoritesActivity.class)
		        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		
		tabHost.addTab(tabHost.newTabSpec("Activities")
		        .setIndicator("Activities", getResources().getDrawable(android.R.drawable.ic_menu_gallery))
		        .setContent(new Intent(this, ActivitiesActivity.class)
		        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		
		tabHost.addTab(tabHost.newTabSpec("History")
		        .setIndicator("History", getResources().getDrawable(android.R.drawable.ic_dialog_info))
		        .setContent(new Intent(this, HistoryActivity.class)
		        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
	}
}
