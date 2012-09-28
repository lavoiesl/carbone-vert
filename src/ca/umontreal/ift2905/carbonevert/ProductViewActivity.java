package ca.umontreal.ift2905.carbonevert;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ca.umontreal.ift2905.carbonevert.model.ProductData;

import com.example.novak.picker.NumberPicker;
import com.example.novak.picker.NumberPicker.OnChangedListener;

public class ProductViewActivity extends Activity {

	Button categoryButton = null;
	Button productButton = null;
	TextView quantityTextView = null;
	Spinner unitSpinner = null;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_view_layout);
		
		categoryButton = (Button) findViewById(R.id.cathegorieVButton);
		productButton = (Button) findViewById(R.id.productVButton);
		quantityTextView = (TextView) findViewById(R.id.quantityVTextView);
		unitSpinner = (Spinner) findViewById(R.id.categorieVSpinner);
		
		
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
		
		NumberPicker picker = (NumberPicker) findViewById(R.id.pref_num_pickerV);
		picker.setOnChangeListener(new OnChangedListener() {
			public void onChanged(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				TextView totalTextView = (TextView) findViewById(R.id.totalVTextView);
				totalTextView.setText("Total : "+4*newVal+" kg CO2");
			}
		});
		
		
		final Button addToAcButton = (Button) findViewById(R.id.addToAcVButton);
		addToAcButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(v.getContext(), "ADDED", Toast.LENGTH_LONG).show();
			}
		});
		
		
	}
	
	public void seter(ProductData data){
		
		String category = data.getCategory().toString();
		String product = data.getName().toString();
		int quantity = 4;
		String [] unit = {"kg", "lbs", "L", "ml"};
		
		
		categoryButton.setText("Category : "+category);
		productButton.setText(product);
		quantityTextView.setText(quantity+" "+unit[0]+" CO2/"+unit[0]);
		
		final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				getBaseContext(), android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		unitSpinner.setAdapter(adapter);
		for(String i : unit){
			adapter.add(i);
		}
	}
}
