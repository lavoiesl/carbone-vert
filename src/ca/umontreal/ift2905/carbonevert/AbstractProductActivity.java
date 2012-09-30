package ca.umontreal.ift2905.carbonevert;

import java.sql.SQLException;
import java.util.HashMap;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ProductData;
import ca.umontreal.ift2905.carbonevert.model.UnitData;

import com.example.novak.picker.NumberPicker;
import com.example.novak.picker.NumberPicker.OnChangedListener;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

public abstract class AbstractProductActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	protected Button categoryButton = null;
	protected Button favoriteButton = null;
	protected TextView ratioTextView = null;
	protected TextView totalTextView = null;
	protected NumberPicker quantityPicker = null;
	protected Spinner unitSpinner = null;
		
	protected ProductData product = null;

	protected <T> ArrayAdapter<T> createArrayAdapter(Iterable<T> items) {
		final int spinner = android.R.layout.simple_spinner_item; 
		final int dropdown = android.R.layout.simple_spinner_dropdown_item; 
		final ArrayAdapter<T> adapter = new ArrayAdapter<T>(getBaseContext(), spinner);
		adapter.setDropDownViewResource(dropdown);

		for (T item : items) {
			adapter.add(item);
		}
		
		return adapter;
	}

	private void fillUnitSpinner() throws SQLException {
		final Dao<UnitData, Integer> units = getHelper().getDao(UnitData.class);
		unitSpinner = (Spinner) findNestedView(R.id.productCalculatorUnitSpinner);

		final ArrayAdapter<UnitData> adapter = createArrayAdapter(units);
		unitSpinner.setAdapter(adapter);
	}
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		
		try {
			loadEntity();
			fillUnitSpinner();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (NotFoundException e) {
			// No activity to show, quit
			onBackPressed();
		}

		bindButtons();
		fillFields();
	}

	protected void setContentView() {
		setContentView(R.layout._product_layout);
	}

	protected void loadEntity() throws NotFoundException, SQLException {
		final Bundle b = this.getIntent().getExtras();
		
		final int id = b.getInt("product_id");
		if (id != 0) {
			final Dao<ProductData, Integer> products = getHelper().getDao(ProductData.class);
			product = products.queryForId(id);
			if (product != null) {
				return;
			}
		}			

		throw new NotFoundException();
	}
	
	protected void fillFields() {
		Log.d("ProductView", "fillActivityFields");

		categoryButton.setText(product.getCategory().toString());
		favoriteButton.setText(product.toString());		
	}
	
	protected void setTotal(double co2) {
		totalTextView.setText("Total : "+ co2 +" kg CO2");
	}
	
	private View findNestedView(final int id) {
		return findViewById(R.id.productView).findViewById(id);
	}

	protected void bindButtons() {
		categoryButton = (Button) findNestedView(R.id.productCategoryButton);
		favoriteButton = (Button) findNestedView(R.id.productFavoriteButton);
		ratioTextView = (TextView) findNestedView(R.id.productPropertiesRatioTextView);
		totalTextView = (TextView) findNestedView(R.id.productCalculatorTotalTextView);
		quantityPicker = (NumberPicker) findNestedView(R.id.productCalculatorNumberPicker);
		
		quantityPicker.setOnChangeListener(new OnChangedListener() {
			public void onChanged(NumberPicker picker, int oldVal, int newVal) {
				// TODO: real ratio
				setTotal(4 * newVal);
			}
		});

		categoryButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final HashMap<String, Integer> options = new HashMap<String, Integer>();
				options.put("category_id", product.getCategory().getId());

				startActivity(BrowseActivity.class, options);
			}
		});
		
		favoriteButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(v.getContext(), "ADDED to FAVORITES", Toast.LENGTH_LONG).show();
			}
		});
	}

	public void startActivity(Class<? extends Activity> clazz, Map<String, Integer> options) {
		final Intent intent = new Intent(getBaseContext(), clazz);
		for (Map.Entry<String, Integer> entry : options.entrySet()) {
			intent.putExtra(entry.getKey(), entry.getValue());
		}
		Log.i("Activity", "Starting " + clazz.getSimpleName() + " with options " + options);
		startActivity(intent);
	}
}
