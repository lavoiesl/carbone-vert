package ca.umontreal.ift2905.carbonevert;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import ca.umontreal.ift2905.carbonevert.db.DBHelper;

public class ActivitiesActivity extends ListActivity {//OrmLiteBaseActivity<DatabaseHelper> {
	private EditText filterText = null;
	ArrayAdapter<String> adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.activitie_layout);
		ListView list= getListView();
	    
	    DBHelper helper=new DBHelper(this);
		ArrayList<String> items=helper.getNames();
	    
	    filterText = (EditText) findViewById(R.id.search_box);
	    filterText.addTextChangedListener(filterTextWatcher);

		adapter = new ArrayAdapter<String>(this, R.layout.list_layout,items);
		list.setAdapter(adapter);
		helper.close();
	    
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

	    public void afterTextChanged(Editable s) {
	    }

	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	    }

	    public void onTextChanged(CharSequence s, int start, int before,
	            int count) {
	        adapter.getFilter().filter(s);
	    }

	};

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    filterText.removeTextChangedListener(filterTextWatcher);
	}
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
