package ca.umontreal.ift2905.carbonevert.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.umontreal.ift2905.carbonevert.EntityArrayAdapter;
import ca.umontreal.ift2905.carbonevert.R;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.AbstractData;
import ca.umontreal.ift2905.carbonevert.model.CategoryData;
import ca.umontreal.ift2905.carbonevert.model.ProductData;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.Dao;

public class FavoritesActivity extends OrmLiteBaseListActivity<DatabaseHelper> {
	private ArrayAdapter<? extends AbstractData> adapter = null;
	private ListView listView;
	private EditText filterText = null;
	private CategoryData currentCategory = null;

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
		setContentView(R.layout.browse_layout);

		final ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
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
		selectCategory(null);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {

				final AbstractData item = ((EntityArrayAdapter<?>.ViewHolder) ((TextView) view)
						.getTag()).obj;
				Toast.makeText(getBaseContext(), item.toString(),
						Toast.LENGTH_SHORT).show();

				if (item instanceof ProductData) {
					gotoProduct((ProductData) item);
				} else if (item instanceof CategoryData) {
					selectCategory((CategoryData) item);
				}
			}
		});
	}

	private void selectCategory(final CategoryData category) {
		currentCategory = category;
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
//		listView.setAdapter(adapter);
	}

	private void gotoProduct(final ProductData product) {
		final Intent intent = new Intent(getBaseContext(), ProductViewActivity.class);
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