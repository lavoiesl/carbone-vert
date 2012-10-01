package ca.umontreal.ift2905.carbonevert.activity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import ca.umontreal.ift2905.carbonevert.EntityArrayAdapter;
import ca.umontreal.ift2905.carbonevert.R;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ProductData;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.Dao;

public class FavoritesActivity extends OrmLiteBaseListActivity<DatabaseHelper> {
	private ArrayAdapter<ProductData> adapter = null;
	private ListView listView;
	private EditText filterText = null;

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
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorites_layout);

		filterText = (EditText) findViewById(R.id.favoritesSearchBox);
		filterText.addTextChangedListener(filterTextWatcher);
		
		listView = getListView();
		fillView();

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {

				@SuppressWarnings("unchecked")
				final ProductData item = ((EntityArrayAdapter<ProductData>.ViewHolder) ((TextView) view)
						.getTag()).obj;
				
				showProduct(item);
			}
		});		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		fillView();
	}

	private void fillView() {
		try {
			final Dao<ProductData, Integer> dao = getHelper().getDao(ProductData.class);
			final List<ProductData> list = dao.queryForEq("favorite", true);
			adapter = new EntityArrayAdapter<ProductData>(this, list);
			listView.setAdapter(adapter);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void showProduct(ProductData product) {
		final HashMap<String, Integer> options = new HashMap<String, Integer>();
		options.put("product_id", product.getId());

		startActivity(ProductViewActivity.class, options);
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