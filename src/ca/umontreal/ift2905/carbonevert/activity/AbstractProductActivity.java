package ca.umontreal.ift2905.carbonevert.activity;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ca.umontreal.ift2905.carbonevert.R;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ProductData;
import ca.umontreal.ift2905.carbonevert.model.ProductUnitData;

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
		unitSpinner = (Spinner) findNestedView(R.id.productCalculatorUnitSpinner);

		final ArrayAdapter<ProductUnitData> adapter = createArrayAdapter(product.getUnits());
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

		quantityPicker.setCurrent(1);
		setQuantity(1);
		categoryButton.setText(product.getCategory().toString());
		favoriteButton.setText(product.toString());
		setFavorite(product.isFavorite());
	}
	
	protected void setQuantity(int quantity) {
		final ProductUnitData unit = (ProductUnitData) unitSpinner.getSelectedItem();
		if (unit != null) {
			final double co2 = unit.getCarbonRatio() * quantity;
			totalTextView.setText(String.format("Total : %.2f kg of CO2", co2));
		} else {
			totalTextView.setText("Total : 0 kg of CO2");
		}
	}
	
	protected void setFavorite(boolean favorite) {
		if (product.isFavorite() != favorite) {
			product.setFavorite(favorite);
			try {
				getHelper().getDao(ProductData.class).update(product);
			} catch (SQLException e) {
				// Ignore
			}
		}

		final int id;
		if (favorite) {
			id = android.R.drawable.btn_star_big_on;
		} else {
			id = android.R.drawable.btn_star_big_off;
		}
		favoriteButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0);
	}
	
	protected void setUnit(ProductUnitData unit) {
		if (unit != null) {
			setQuantity(quantityPicker.getCurrent());
			ratioTextView.setText(String.format("%.2f kg CO2/%s", unit.getCarbonRatio(), unit.getUnit().getCode()));
		} else {
			ratioTextView.setText("0 kg CO2/kg");
		}
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
				setQuantity(newVal);
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
				setFavorite(!product.isFavorite());
			}
		});
		
		unitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent,
			        View view, int pos, long id) {
				setUnit((ProductUnitData) unitSpinner.getSelectedItem());
			}

			public void onNothingSelected(AdapterView<?> arg0) {
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
