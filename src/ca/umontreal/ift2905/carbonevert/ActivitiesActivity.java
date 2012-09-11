package ca.umontreal.ift2905.carbonevert;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitiesActivity extends ListActivity {//OrmLiteBaseActivity<DatabaseHelper> {
	//private final String LOG_TAG = getClass().getSimpleName();
	final String[] tableau = new String[] { "aaa","bbb","ccc","ddd","eee","fff","allo","bonjour","toto","blub" };

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitie_layout);
//
//		ListAdapter adapter = new ArrayAdapter<String>(
//				this,  R.layout.list_layout, R.id.titre ,tableau);
//		setListAdapter(adapter);

		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_layout,tableau));
		 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
		
		//getDemoText();
	}

	/**
	 * Do our sample database stuff.
	 */
//	private String getDemoText() {
//		// get our dao
//		final RuntimeExceptionDao<ActivityData, Integer> activityDao = getHelper()
//				.getActivityDao();
//		// query for all of the data objects in the database
//		final List<ActivityData> list = activityDao.queryForAll();
//		// our string builder for building the content-view
//		final StringBuilder sb = new StringBuilder();
//		sb.append("got ").append(list.size()).append(" entries in ")
//				.append("activities").append("\n");
//
//		// if we already have items in the database
//		int simpleC = 0;
//		for (final ActivityData simple : list) {
//			sb.append("------------------------------------------\n");
//			sb.append("[").append(simpleC).append("] = ").append(simple)
//					.append("\n");
//			simpleC++;
//		}
//		sb.append("------------------------------------------\n");
//		for (final ActivityData activity : list) {
//			activityDao.delete(activity);
//			sb.append("deleted id ").append(activity.getId()).append("\n");
//			Log.i(LOG_TAG, "deleting activity(" + activity.getId() + ")");
//			simpleC++;
//		}
//
//		int createNum;
//		do {
//			createNum = new Random().nextInt(3) + 1;
//		} while (createNum == list.size());
//		for (int i = 0; i < createNum; i++) {
//			// create a new simple object
//			final ActivityData activity = new ActivityData();
//			activity.setDate(new Date());
//			// store it in the database
//			activityDao.create(activity);
//			Log.i(LOG_TAG, "created activity(" + activity.getDate() + ")");
//			// output it
//			sb.append("------------------------------------------\n");
//			sb.append("created new entry #").append(i + 1).append(":\n");
//			sb.append(activity).append("\n");
//			try {
//				Thread.sleep(5);
//			} catch (final InterruptedException e) {
//				// ignore
//			}
//		}
//
//		Log.i(LOG_TAG, "Done with page at " + System.currentTimeMillis());
//		return sb.toString();
//	}
}