package ca.umontreal.ift2905.carbonevert;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ProductViewActivity extends Activity {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_view_layout);

		final Spinner spinner = (Spinner) findViewById(R.id.categorieVSpinner);

		final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				getBaseContext(), android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		adapter.add("kg");
		adapter.add("lbs");
		adapter.add("L");
		adapter.add("ml");
	}
	
}
