package ca.umontreal.ift2905.carbonevert;

import java.util.Date;
import java.util.List;
import java.util.Random;

import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	
	private final String LOG_TAG = getClass().getSimpleName();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec spec1=tabHost.newTabSpec("Tab 1");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("Tab 1");

		TabSpec spec2=tabHost.newTabSpec("Tab 2");
		spec2.setIndicator("Tab 2");
		spec2.setContent(R.id.tab2);

		TabSpec spec3=tabHost.newTabSpec("Tab 3");
		spec3.setIndicator("Tab 3");
		spec3.setContent(R.id.tab3);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		
		getDemoText();
	}
	/**
	 * Do our sample database stuff.
	 */
	private String getDemoText() {
		// get our dao
		RuntimeExceptionDao<ActivityData, Integer> activityDao = getHelper().getActivityDao();
		// query for all of the data objects in the database
		List<ActivityData> list = activityDao.queryForAll();
		// our string builder for building the content-view
		StringBuilder sb = new StringBuilder();
		sb.append("got ").append(list.size()).append(" entries in ").append("activities").append("\n");

		// if we already have items in the database
		int simpleC = 0;
		for (ActivityData simple : list) {
			sb.append("------------------------------------------\n");
			sb.append("[").append(simpleC).append("] = ").append(simple).append("\n");
			simpleC++;
		}
		sb.append("------------------------------------------\n");
		for (ActivityData activity : list) {
			activityDao.delete(activity);
			sb.append("deleted id ").append(activity.getId()).append("\n");
			Log.i(LOG_TAG, "deleting activity(" + activity.getId() + ")");
			simpleC++;
		}

		int createNum;
		do {
			createNum = new Random().nextInt(3) + 1;
		} while (createNum == list.size());
		for (int i = 0; i < createNum; i++) {
			// create a new simple object
			ActivityData activity = new ActivityData();
			activity.setDate(new Date());
			// store it in the database
			activityDao.create(activity);
			Log.i(LOG_TAG, "created activity(" + activity.getDate() + ")");
			// output it
			sb.append("------------------------------------------\n");
			sb.append("created new entry #").append(i + 1).append(":\n");
			sb.append(activity).append("\n");
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// ignore
			}
		}

		Log.i(LOG_TAG, "Done with page at " + System.currentTimeMillis());
		return sb.toString();
	}

}
