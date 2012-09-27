package ca.umontreal.ift2905.carbonevert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class HistoryActivity extends Activity {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_layout);

		final Button weekButton = (Button) findViewById(R.id.weekButton);
		weekButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
				final Intent intent = new Intent(v.getContext(), TestApi.class);
				startActivity(intent);
			}
		});

		final Button monthButton = (Button) findViewById(R.id.monthButton);
		monthButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
				final Intent intent = new Intent(v.getContext(), TestApi.class);
				startActivity(intent);
			}
		});

		final Button yearButton = (Button) findViewById(R.id.yearButton);
		yearButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
				final Intent intent = new Intent(v.getContext(), TestApi.class);
				startActivity(intent);
			}
		});

		final Button customButton = (Button) findViewById(R.id.customButton);
		customButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
//				final Intent intent = new Intent(v.getContext(), TestApi.class);
//				startActivity(intent);
				setContentView(R.layout.custom_history_layout);
				Spinner spinner = (Spinner) findViewById(R.id.spinner);
				
				ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getBaseContext(), android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
				spinner.setAdapter(adapter);

				adapter.add("By day");
				adapter.add("By week");
				adapter.add("By month");
				adapter.add("By year");
			}
		});

	}
}
