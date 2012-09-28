package ca.umontreal.ift2905.carbonevert;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novak.picker.NumberPicker;
import com.example.novak.picker.NumberPicker.OnChangedListener;

public class ActivitieEditActivity extends Activity {
	
	Button categoryButton = null;
	Button productButton = null;
	TextView quantityTextView = null;
	Spinner unitSpinner = null;
	DatePicker datePicker = null;
	Spinner repeatSpinner = null;
	EditText notesText = null;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitie_edit_layout);
		
		categoryButton = (Button) findViewById(R.id.cathegorieEButton);
		productButton = (Button) findViewById(R.id.productEButton);
		quantityTextView = (TextView) findViewById(R.id.quantityETextView);
		unitSpinner = (Spinner) findViewById(R.id.categorieESpinner);
		datePicker = (DatePicker) findViewById(R.id.dateEPicker);
		repeatSpinner = (Spinner) findViewById(R.id.repeatESpinner);
		notesText = (EditText) findViewById(R.id.noteEditText);
		
		Button save = (Button) findViewById(R.id.saveEButton);
		Button cancel = (Button) findViewById(R.id.cancelEButton);
		
		
		categoryButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		
		productButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "ADDED to FAVORITES", Toast.LENGTH_LONG).show();
			}
		});
		
		
		NumberPicker picker = (NumberPicker) findViewById(R.id.pref_num_pickerE);
		picker.setOnChangeListener(new OnChangedListener() {
			public void onChanged(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				TextView totalTextView = (TextView) findViewById(R.id.totalETextView);
				totalTextView.setText("Total : "+4*newVal+" kg CO2");
			}
		});
		
		
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "SAVED", Toast.LENGTH_LONG).show();
			}
		});
		
		
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		
		
		final ArrayAdapter<CharSequence> categorieAdapter = new ArrayAdapter<CharSequence>(
				getBaseContext(), android.R.layout.simple_spinner_item);
		categorieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		unitSpinner.setAdapter(categorieAdapter);

		categorieAdapter.add("kg");
		categorieAdapter.add("lbs");
		categorieAdapter.add("L");
		categorieAdapter.add("ml");

		
		

		final ArrayAdapter<CharSequence> repeatAdapter = new ArrayAdapter<CharSequence>(
				getBaseContext(), android.R.layout.simple_spinner_item);
		repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		repeatSpinner.setAdapter(repeatAdapter);

		repeatAdapter.add("One Time");
		repeatAdapter.add("Daily");
		repeatAdapter.add("Every Thursday");
		repeatAdapter.add("Monthly(12th)");
		repeatAdapter.add("For 5 days");
		repeatAdapter.add("Until select Date");
		
	}
}
