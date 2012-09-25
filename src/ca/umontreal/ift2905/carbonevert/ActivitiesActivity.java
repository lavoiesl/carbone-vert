package ca.umontreal.ift2905.carbonevert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitiesActivity extends OrmLiteBaseListActivity<DatabaseHelper> {

	final String[] tableau = new String[] { "aaa", "bbb", "ccc", "ddd", "eee",
			"fff", "allo", "bonjour", "toto", "blub" };

	EditText edittext;
	ListView listview;

	int textlength = 0;
	ArrayList<String> text_sort = new ArrayList<String>();

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitie_layout);
		setListAdapter(getActivitiesAdapter());

		final ListView listView = getListView();
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();

				final Intent i = new Intent(view.getContext(), TestApi.class);
				startActivity(i);
			}
		});

		edittext = (EditText) findViewById(R.id.editText01);
		listview = (ListView) findViewById(android.R.id.list);
		listview.setAdapter(getActivitiesAdapter());
		edittext.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(final Editable s) {

			}

			public void beforeTextChanged(final CharSequence s,
					final int start, final int count, final int after) {

			}

			public void onTextChanged(final CharSequence s, final int start,
					final int before, final int count) {

				textlength = edittext.getText().length();
				text_sort.clear();

				for (final String element : tableau) {
					if (textlength <= element.length()) {
						if (edittext
								.getText()
								.toString()
								.equalsIgnoreCase(
										(String) element.subSequence(0,
												textlength))) {
							text_sort.add(element);
						}
					}
				}

				listview.setAdapter(new MyCustomAdapter(text_sort));

			}
		});

		// getDemoText();
	}

	class MyCustomAdapter extends BaseAdapter {
		String[] data_text;

		MyCustomAdapter() {
		}

		MyCustomAdapter(final String[] text) {
			data_text = text;
		}

		MyCustomAdapter(final ArrayList<String> text) {
			data_text = new String[text.size()];

			for (int i = 0; i < text.size(); i++) {
				data_text[i] = text.get(i);
			}

		}

		public int getCount() {
			return data_text.length;
		}

		public String getItem(final int position) {
			return null;
		}

		public long getItemId(final int position) {
			return position;
		}

		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			final LayoutInflater inflater = getLayoutInflater();
			View row;

			row = inflater.inflate(R.layout.list_layout, parent, false);

			final TextView textview = (TextView) row
					.findViewById(R.id.list_layout_view);
			textview.setText(data_text[position]);
			return row;
		}
	}

	 private ActivityArrayAdapter getActivitiesAdapter() {
		 // get our dao
		 
		 final RuntimeExceptionDao<ActivityData, Integer> activityDao = getHelper().getActivityDao();
		 // query for all of the data objects in the database
		 final List<ActivityData> list = activityDao.queryForAll();
		 return new ActivityArrayAdapter(this, list);
		 }
}

/**
 * Do our sample database stuff.
 */
