package ca.umontreal.ift2905.carbonevert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.AbstractData;
import ca.umontreal.ift2905.carbonevert.model.CategoryData;
import ca.umontreal.ift2905.carbonevert.model.ProductData;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.Dao;

public class BrowseActivity extends OrmLiteBaseListActivity<DatabaseHelper> {
	private ArrayAdapter<? extends AbstractData> adapter = null;
	private ListView listView;
	private EditText filterText = null;
	private CategoryData currentCategory = null;

	/**
	 * Are we in add mode ? 
	 * While create activity instead of going to product view on select
	 */
	private boolean adding = false; 
	
	private final TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(final Editable s) {
		}

		public void beforeTextChanged(final CharSequence s, final int start,
				final int count, final int after) {
		}

		public void onTextChanged(final CharSequence s, final int start,
				final int before, final int count) {
			adapter.getFilter().filter(s);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		filterText.removeTextChangedListener(filterTextWatcher);
	}
	
	private void selectPassedCategory() {
		final Bundle b = this.getIntent().getExtras();
		
		if (b != null) {
			adding = b.getInt("adding") == 1 ? true : false;

			final int id = b.getInt("category_id");
			if (id != 0) {
				try {
					final Dao<CategoryData, Integer> categories = getHelper().getDao(CategoryData.class);
					final CategoryData category = categories.queryForId(id);
					selectCategory(category);
					return;
				} catch (SQLException e) {
				}
			}
		}
		
		selectCategory(null);
	}
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_layout);

		final Button searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(v.getContext(), "button search clicker",
						Toast.LENGTH_SHORT).show();

			}
		});
	
		filterText = (EditText) findViewById(R.id.browse_search_box);
		filterText.addTextChangedListener(filterTextWatcher);

		listView = getListView();
		selectPassedCategory();

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {

				final AbstractData item = ((EntityArrayAdapter<?>.ViewHolder) ((TextView) view)
						.getTag()).obj;
				
				clickItem(item);
			}
		});
	}
	
	private void clickItem(AbstractData item) {
		if (item instanceof ProductData) {
			final HashMap<String, Integer> options = new HashMap<String, Integer>();
			options.put("product_id", item.getId());

			if (adding) {
				startActivity(ActivityEditActivity.class, options);
				finish();
			} else {
				startActivity(ProductViewActivity.class, options);
			}
		} else if (item instanceof CategoryData) {
			selectCategory((CategoryData) item);
		} else {
			Log.e("Browse", "Unkown item: " + item);
		}
	}

	private void selectCategory(final CategoryData category) {
		currentCategory = category;
		Log.i("Browse", "Showing category "+ currentCategory);

		if (currentCategory == null) {
			Dao<CategoryData, Integer> dao;
			try {
				dao = getHelper().getDao(CategoryData.class);
				final List<CategoryData> list = dao.queryForAll();
				adapter = new EntityArrayAdapter<CategoryData>(this, list);
			} catch (final SQLException e) {
				throw new RuntimeException(e);
			}
		} else {
			final List<ProductData> list = new ArrayList<ProductData>(
					currentCategory.getProducts());
			adapter = new EntityArrayAdapter<ProductData>(this, list);
		}
		listView.setAdapter(adapter);
	}

	public void startActivity(Class<? extends Activity> clazz, Map<String, Integer> options) {
		final Intent intent = new Intent(getBaseContext(), clazz);
		for (Map.Entry<String, Integer> entry : options.entrySet()) {
			intent.putExtra(entry.getKey(), entry.getValue());
		}
		Log.i("Activity", "Starting " + clazz.getSimpleName() + " with options " + options);
		startActivity(intent);
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
		if (currentCategory == null) {
			super.onBackPressed();
		} else {
			selectCategory(null);
		}
	}

}
