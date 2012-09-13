package ca.umontreal.ift2905.carbonevert;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BrowseActivity extends Activity {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_layout);

		final Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
				// Intent intent = new Intent("this");
				// start the second Activity
				// startActivity(intent);
			}
		});

		final Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
			}
		});

		final Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
			}
		});

		final Button button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
			}
		});

		final Button button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
			}
		});

	}
}
