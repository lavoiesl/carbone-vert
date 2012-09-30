package ca.umontreal.ift2905.carbonevert.activity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import ca.umontreal.ift2905.carbonevert.EntityArrayAdapter;
import ca.umontreal.ift2905.carbonevert.R;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;
import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.Dao;

public class ActivitiesActivity extends OrmLiteBaseListActivity<DatabaseHelper> {

	private ArrayAdapter<ActivityData> adapter = null;
	private EditText filterText = null;
	private Dao<ActivityData, Integer> dao = null;

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
		setContentView(R.layout.activities_layout);

		filterText = (EditText) findViewById(R.id.activity_search_box);
		filterText.addTextChangedListener(filterTextWatcher);

		try {
			dao = getHelper().getDao(ActivityData.class);
			adapter = getActivitiesAdapter();
		} catch (final SQLException e) {
			Toast.makeText(getBaseContext(), "Error while listing activities",
					Toast.LENGTH_SHORT).show();
			return;
		}

		final ListView listView = getListView();
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				@SuppressWarnings("unchecked")
				final ActivityData item = ((EntityArrayAdapter<ActivityData>.ViewHolder) ((TextView) view)
						.getTag()).obj;
				selectActivity(item);
			}
		});

		final Button plus = (Button) findViewById(R.id.activity_plus);
		plus.setOnClickListener(new OnClickListener() {

			public void onClick(final View v) {
				final HashMap<String, Integer> options = new HashMap<String, Integer>();
				options.put("adding", 1); // Add mode
				startActivity(BrowseActivity.class, options);
			}
		});
	}
	
	public void startActivity(Class<? extends Activity> clazz, Map<String, Integer> options) {
		final Intent intent = new Intent(getBaseContext(), clazz);
		for (Map.Entry<String, Integer> entry : options.entrySet()) {
			intent.putExtra(entry.getKey(), entry.getValue());
		}
		Log.i("Activity", "Starting " + clazz.getSimpleName() + " with options " + options);
		startActivity(intent);
	}

	private void selectActivity(final ActivityData activity) {
		final HashMap<String, Integer> options = new HashMap<String, Integer>();
		options.put("activity_id", activity.getId());

		startActivity(ActivityEditActivity.class, options);
	}

	private EntityArrayAdapter<ActivityData> getActivitiesAdapter()
			throws SQLException {
		// query for all of the data objects in the database
		final List<ActivityData> list = dao.queryForAll();
		return new EntityArrayAdapter<ActivityData>(this, list);
	}
}
