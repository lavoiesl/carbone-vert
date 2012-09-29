package ca.umontreal.ift2905.carbonevert;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;
import ca.umontreal.ift2905.carbonevert.model.UnitData;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

public class ActivitieEditActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	private Dao<ActivityData, Integer> dao;
	private ActivityData activity;
	
	
	private void fillUnitSpinner() throws SQLException {
		final Dao<UnitData, Integer> units = getHelper().getDao(UnitData.class);
		final Spinner spinner = (Spinner) findViewById(R.id.categorieESpinner);

		final ArrayAdapter<UnitData> adapter = createArrayAdapter(units);
		spinner.setAdapter(adapter);
	}

	private void fillRepeatSpinner() {
		final Spinner spinner = (Spinner) findViewById(R.id.repeatESpinner);
		
		List<String> items = Arrays.asList(
			"One Time",
			"Daily",
			"Every Thursday",
			"Monthly(12th)",
			"For 5 days",
			"Until select Date"
		);

		final ArrayAdapter<String> adapter = createArrayAdapter(items);
		spinner.setAdapter(adapter);
	}
	
	private <T> ArrayAdapter<T> createArrayAdapter(Iterable<T> items) {
		final int spinner = android.R.layout.simple_spinner_item; 
		final int dropdown = android.R.layout.simple_spinner_dropdown_item; 
		final ArrayAdapter<T> adapter = new ArrayAdapter<T>(getBaseContext(), spinner);
		adapter.setDropDownViewResource(dropdown);

		for (T item : items) {
			adapter.add(item);
		}
		
		return adapter;
	}
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitie_edit_layout);
		
		final Bundle b = this.getIntent().getExtras();
		final int id = b.getInt("activity_id");

		try {
			fillUnitSpinner();
			dao = getHelper().getDao(ActivityData.class);
			activity = dao.queryForId(id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		fillRepeatSpinner();
		fillActivityFields();
	}
	
	private void fillActivityFields() {
		Log.d("ActivityEdit", "fillActivityFields");
		final Button categoryButton = (Button) findViewById(R.id.cathegorieEButton);
		categoryButton.setText(activity.getProduct().getCategory().toString());

		final Button productButton = (Button) findViewById(R.id.productEButton);
		productButton.setText(activity.getProduct().toString());
}
}
