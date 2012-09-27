package ca.umontreal.ift2905.carbonevert;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

public class HistoryActivity extends Activity {
	private Bundle instance = null;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_layout);
		instance = savedInstanceState;

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

				setContentView(R.layout.custom_history_layout);
				final Spinner spinner = (Spinner) findViewById(R.id.spinner);

				final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
						getBaseContext(), android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);

				adapter.add("By day");
				adapter.add("By week");
				adapter.add("By month");
				adapter.add("By year");

				final Button showButton = (Button) findViewById(R.id.showButton);
				showButton.setOnClickListener(new View.OnClickListener() {

					DatePicker fromDatePicker = (DatePicker) findViewById(R.id.fromDatePicker);
					int fromDay = fromDatePicker.getDayOfMonth();
					int fromMonth = fromDatePicker.getMonth();
					int fromYear = fromDatePicker.getYear();
					GregorianCalendar fromDate = new GregorianCalendar(
							fromDatePicker.getYear(), fromMonth, fromDay);

					DatePicker toDatePicker = (DatePicker) findViewById(R.id.toDatePicker);
					int toDay = toDatePicker.getDayOfMonth();
					int toMonth = toDatePicker.getMonth();
					int toYear = toDatePicker.getYear();
					GregorianCalendar toDate = new GregorianCalendar(toYear,
							toMonth, toDay);

					Calendar c = Calendar.getInstance();
					int cYear = c.get(Calendar.YEAR);
					int cMonth = c.get(Calendar.MONTH);
					int cDay = c.get(Calendar.DAY_OF_MONTH);
					GregorianCalendar currentDate = new GregorianCalendar(
							cYear, cMonth, cDay);

					// boolean good = (fromDate.before(currentDate) ||
					// fromDate.equals(toDate));

					boolean toEarly = fromDate.before(currentDate)
							& toDate.before(currentDate)
							|| fromDate.equals(toDate);
					int result = fromDate.compareTo(currentDate);

					public void onClick(final View v) {
						if (toEarly) {
							// the program runs normally
							Toast.makeText(
									getBaseContext(),
									"FROM : "
											+ fromYear
											+ " TO : "
											+ toYear
											+ " CURRENT "
											+ cYear
											+ " GOOD YOU ARE READY DO ADVANCE TO THE NEXT LEVEL : "
											+ result, Toast.LENGTH_SHORT)
									.show();
						} else {
							// new AlertDialog.Builder(null)
							// .setTitle("Wrong Data Input!")
							// .setMessage("The end Date must be Before the start Date, please insert new Date values")
							// .setNeutralButton("Ok",new
							// DialogInterface.OnClickListener() {
							//
							// public void onClick(DialogInterface dialog,
							//
							// int which) {
							//
							// }
							//
							// }).show();
							Toast.makeText(getBaseContext(),
									"Wrong Date selection", Toast.LENGTH_SHORT)
									.show();
						}

					}
				});

			}
		});

	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
				&& keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			onBackPressed();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		onCreate(instance);
	}
}
