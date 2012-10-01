package ca.umontreal.ift2905.carbonevert.activity;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import android.widget.Toast;
import ca.umontreal.ift2905.carbonevert.R;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;
import ca.umontreal.ift2905.carbonevert.model.ProductUnitData;

import com.j256.ormlite.dao.Dao;

public class ActivityEditActivity extends AbstractProductActivity {
	private ActivityData activity = null;
	
	private DatePicker datePicker = null;
	private Spinner repeatSpinner = null;
	private EditText notesText = null;
	
	protected void setContentView() {
		setContentView(R.layout.activity_edit_layout);
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

		repeatSpinner = (Spinner) findViewById(R.id.activityEditRepeatSpinner);
		final ArrayAdapter<String> adapter = createArrayAdapter(items);
		repeatSpinner.setAdapter(adapter);
	}
	
	protected void loadEntity() throws NotFoundException, SQLException {
		final Bundle b = this.getIntent().getExtras();
		
		int id = b.getInt("activity_id");
		if (id != 0) {
			final Dao<ActivityData, Integer> dao = getHelper().getDao(ActivityData.class);
			activity = dao.queryForId(id);
			if (activity != null) {
				product = activity.getProduct();
				return;
			}
		}
		
		super.loadEntity(); // Will throw NotFoundException
		Log.d("ActivityEdit", "Creating activity for product "+ product.getId());
		activity = new ActivityData();
		activity.setProduct(product);
	}
	
	@Override
	protected void bindButtons() {
		super.bindButtons();

		datePicker = (DatePicker) findViewById(R.id.activityEditDatePicker);
		notesText = (EditText) findViewById(R.id.activityEditNotesEditText);
		final Button save = (Button) findViewById(R.id.activityEditSaveButton);
		final Button cancel = (Button) findViewById(R.id.activityEditCancelButton);
				
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
			final ProductUnitData unit = (ProductUnitData) unitSpinner.getSelectedItem();
			final int quantity = quantityPicker.getCurrent();
			final Dao<ActivityData, Integer> dao = getHelper().getDao(ActivityData.class);
			activity.setUnit(unit);
			activity.setQuantity(quantity);
			activity.setDate(new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
			activity.setNotes(notesText.getText().toString());
			// TODO manage ratio
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
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fillRepeatSpinner();
	}
	
	@Override
	protected void fillFields() {
		super.fillFields();

		quantityPicker.setCurrent(activity.getQuantity());
		setQuantity(activity.getQuantity());
		
		if (activity.getUnit() != null) {
			@SuppressWarnings("unchecked")
			final ArrayAdapter<ProductUnitData> unitAdapter = (ArrayAdapter<ProductUnitData>) unitSpinner.getAdapter();
			unitSpinner.setSelection(unitAdapter.getPosition(activity.getUnit()));
			setUnit(activity.getUnit());
		}
		
		notesText.setText(activity.getNotes());
		
		Date date = activity.getDate();
		if (date != null) {
			datePicker.updateDate(date.getYear(), date.getMonth(), date.getDate());
		}
	}
}
