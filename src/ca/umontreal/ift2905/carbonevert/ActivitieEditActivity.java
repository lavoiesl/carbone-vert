package ca.umontreal.ift2905.carbonevert;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ActivitieEditActivity extends Activity {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitie_edit_layout);
		
		final Spinner categorieSpinner = (Spinner) findViewById(R.id.categorieESpinner);

		final ArrayAdapter<CharSequence> categorieAdapter = new ArrayAdapter<CharSequence>(
				getBaseContext(), android.R.layout.simple_spinner_item);
		categorieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorieSpinner.setAdapter(categorieAdapter);

		categorieAdapter.add("kg");
		categorieAdapter.add("lbs");
		categorieAdapter.add("L");
		categorieAdapter.add("ml");


		final Spinner repeatSpinner = (Spinner) findViewById(R.id.repeatESpinner);

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
