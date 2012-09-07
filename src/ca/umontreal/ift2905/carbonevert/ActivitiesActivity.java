package ca.umontreal.ift2905.carbonevert;

import java.util.Date;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.util.Log;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class ActivitiesActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	private final String LOG_TAG = getClass().getSimpleName();

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_layout);

		getDemoText();
	}

	/**
	 * Do our sample database stuff.
	 */
	private String getDemoText() {
		// get our dao
		final RuntimeExceptionDao<ActivityData, Integer> activityDao = getHelper()
				.getActivityDao();
		// query for all of the data objects in the database
		final List<ActivityData> list = activityDao.queryForAll();
		// our string builder for building the content-view
		final StringBuilder sb = new StringBuilder();
		sb.append("got ").append(list.size()).append(" entries in ")
				.append("activities").append("\n");

		// if we already have items in the database
		int simpleC = 0;
		for (final ActivityData simple : list) {
			sb.append("------------------------------------------\n");
			sb.append("[").append(simpleC).append("] = ").append(simple)
					.append("\n");
			simpleC++;
		}
		sb.append("------------------------------------------\n");
		for (final ActivityData activity : list) {
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
			final ActivityData activity = new ActivityData();
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
			} catch (final InterruptedException e) {
				// ignore
			}
		}

		Log.i(LOG_TAG, "Done with page at " + System.currentTimeMillis());
		return sb.toString();
	}
}