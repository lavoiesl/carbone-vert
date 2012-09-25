package ca.umontreal.ift2905.carbonevert;

import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class ActivitiesActivity extends OrmLiteBaseListActivity<DatabaseHelper> {

	private ArrayAdapter<ActivityData> adapter = null;
	private RuntimeExceptionDao<ActivityData, Integer> activityDao = null;
	private EditText filterText = null;

	private final TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(final Editable s) {
		}

		public void beforeTextChanged(final CharSequence s, final int start,
				final int count, final int after) {
		}

		public void onTextChanged(final CharSequence s, final int start,
				final int before, final int count) {
			adapter.getFilter().filter(s);
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		filterText.removeTextChangedListener(filterTextWatcher);
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitie_layout);

		adapter = getActivitiesAdapter();

		final ListView listView = getListView();
		listView.setAdapter(adapter);

		filterText = (EditText) findViewById(R.id.activity_search_box);
		filterText.addTextChangedListener(filterTextWatcher);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				final String item = ((TextView) view).getText().toString();
				Toast.makeText(getBaseContext(), item, Toast.LENGTH_SHORT)
						.show();
			}
		});

		final Button plus = (Button) findViewById(R.id.activity_plus);
		plus.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final ActivityData obj = ActivityData.testData("test "
						+ adapter.getCount());
				addActivityData(obj);
			}
		});
	}

	private void addActivityData(ActivityData obj) {
		getHelper().getActivityDao().create(obj); // Add to database
		Toast.makeText(getBaseContext(), "size:" + getHelper().getActivityDao().queryForAll().size(), Toast.LENGTH_SHORT)
		.show();
		
		adapter.add(obj); // Add to current list
		try {
			Thread.sleep(5);
		} catch (final InterruptedException e) {
			// ignore
		}
	}

	private ActivityArrayAdapter getActivitiesAdapter() {
		// query for all of the data objects in the database
		final List<ActivityData> list = getHelper().getActivityDao().queryForAll();
		return new ActivityArrayAdapter(this, list);
	}
}
