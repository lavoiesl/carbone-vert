package ca.umontreal.ift2905.carbonevert;

import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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

		filterText = (EditText) findViewById(R.id.search_box);
		filterText.addTextChangedListener(filterTextWatcher);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				final String item = ((TextView) view).getText().toString();
				Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	private ActivityArrayAdapter getActivitiesAdapter() {
		// get our dao

		final RuntimeExceptionDao<ActivityData, Integer> activityDao = getHelper()
				.getActivityDao();
		// query for all of the data objects in the database
		final List<ActivityData> list = activityDao.queryForAll();
		return new ActivityArrayAdapter(this, list);
	}
}
