package ca.umontreal.ift2905.carbonevert;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;
import ca.umontreal.ift2905.carbonevert.model.ProductData;
import ca.umontreal.ift2905.carbonevert.model.UnitData;

import com.example.novak.picker.NumberPicker;
import com.example.novak.picker.NumberPicker.OnChangedListener;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

public class ActivityEditActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	private Dao<ActivityData, Integer> dao;
	private ActivityData activity = null;
	
	private NumberPicker quantityPicker = null;
	private Button categoryButton = null;
	private Button productButton = null;
	private TextView quantityTextView = null;
	private Spinner unitSpinner = null;
	private DatePicker datePicker = null;
	private Spinner repeatSpinner = null;
	private EditText notesText = null;
	
	
	private void fillUnitSpinner() throws SQLException {
		final Dao<UnitData, Integer> units = getHelper().getDao(UnitData.class);

		unitSpinner = (Spinner) findViewById(R.id.categorieESpinner);
		final ArrayAdapter<UnitData> adapter = createArrayAdapter(units);
		unitSpinner.setAdapter(adapter);
	}

	private void fillRepeatSpinner() {
		List<String> items = Arrays.asList(
				"One Time",
				"Daily",
				"Every Thursday",
				"Monthly(12th)",
				"For 5 days",
				"Until select Date"
			);

		repeatSpinner = (Spinner) findViewById(R.id.repeatESpinner);
		final ArrayAdapter<String> adapter = createArrayAdapter(items);
		repeatSpinner.setAdapter(adapter);
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
	
	private void loadActivity() throws NotFoundException, SQLException {
		final Bundle b = this.getIntent().getExtras();
		
		int id = b.getInt("activity_id");
		if (id != 0) {
			activity = dao.queryForId(id);
			if (activity != null) {
				return;
			}
		}

		id = b.getInt("product_id");
		if (id != 0) {
			final Dao<ProductData, Integer> products = getHelper().getDao(ProductData.class);
			final ProductData product = products.queryForId(id);
			if (product != null) {
				Log.d("ActivityEdit", "Creating activity for product "+ id);
				activity = new ActivityData();
				activity.setProduct(product);
				return;
			}
		}			

		throw new NotFoundException();
	}
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_layout);
		
		
		try {
			dao = getHelper().getDao(ActivityData.class);
			loadActivity();
			fillUnitSpinner();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (NotFoundException e) {
			// No activity to show, quit
			onBackPressed();
		}

				
		bindButtons();
		fillRepeatSpinner();
		fillActivityFields();
	}
	
	private void bindButtons() {
		categoryButton = (Button) findViewById(R.id.categoryEButton);
		productButton = (Button) findViewById(R.id.productEButton);
		quantityTextView = (TextView) findViewById(R.id.quantityETextView);
		datePicker = (DatePicker) findViewById(R.id.dateEPicker);
		notesText = (EditText) findViewById(R.id.noteEditText);
		final Button save = (Button) findViewById(R.id.saveEButton);
		final Button cancel = (Button) findViewById(R.id.cancelEButton);
		quantityPicker = (NumberPicker) findViewById(R.id.pref_num_pickerE);
		
		quantityPicker.setOnChangeListener(new OnChangedListener() {
			public void onChanged(NumberPicker picker, int oldVal, int newVal) {
				TextView totalTextView = (TextView) findViewById(R.id.totalETextView);
				totalTextView.setText("Total : "+4*newVal+" kg CO2");
			}
		});

		categoryButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final HashMap<String, Integer> options = new HashMap<String, Integer>();
				options.put("category_id", activity.getProduct().getCategory().getId());

				startActivity(BrowseActivity.class, options);
			}
		});
		
		productButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "ADDED to FAVORITES", Toast.LENGTH_LONG).show();
			}
		});
		
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				save();
				onBackPressed();
			}
		});
		
		
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	private void save() {
		try {
			UnitData unit = (UnitData) unitSpinner.getSelectedItem();
			int quantity = quantityPicker.getCurrent();
			activity.setUnit(unit);
			activity.setQuantity(quantity);
			activity.setDate(new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
//			activity.setCarbon(quantity * activity.getProduct().getUnit(unit).getCarbonRatio());

			if (activity.getId() == 0) {
				// New activity
				dao.create(activity);
			} else {
				dao.update(activity);
			}
			Toast.makeText(getBaseContext(), "Saved activity for " + activity.getProduct(), Toast.LENGTH_LONG).show();		
		} catch (SQLException e) {
			Toast.makeText(getBaseContext(), "Error while saving your activity.", Toast.LENGTH_LONG).show();
		}
	}

	public void startActivity(Class<? extends Activity> clazz, Map<String, Integer> options) {
		final Intent intent = new Intent(getBaseContext(), clazz);
		for (Map.Entry<String, Integer> entry : options.entrySet()) {
			intent.putExtra(entry.getKey(), entry.getValue());
		}
		Log.i("Activity", "Starting " + clazz.getSimpleName() + " with options " + options);
		startActivity(intent);
	}

	private void fillActivityFields() {
		Log.d("ActivityEdit", "fillActivityFields");
		quantityPicker.setCurrent(activity.getQuantity());
		
		if (activity.getUnit() != null) {
			@SuppressWarnings("unchecked")
			final ArrayAdapter<UnitData> unitAdapter = (ArrayAdapter<UnitData>) unitSpinner.getAdapter();
			unitSpinner.setSelection(unitAdapter.getPosition(activity.getUnit()));
		}
		
		Date date = activity.getDate();
		if (date != null) {
			datePicker.updateDate(date.getYear(), date.getMonth(), date.getDate());
		}

		categoryButton.setText(activity.getProduct().getCategory().toString());
		productButton.setText(activity.getProduct().toString());
	}
}