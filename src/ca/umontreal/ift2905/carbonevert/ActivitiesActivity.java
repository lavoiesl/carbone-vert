package ca.umontreal.ift2905.carbonevert;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ActivitiesActivity extends ListActivity {// OrmLiteBaseActivity<DatabaseHelper>
														// {
	// private final String LOG_TAG = getClass().getSimpleName();
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
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_layout,
				tableau));

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
		listview.setAdapter(new MyCustomAdapter(tableau));
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
					.findViewById(R.id.textView01);
			textview.setText(data_text[position]);
			return row;
		}
	}
}

/**
 * Do our sample database stuff.
 */
// private String getDemoText() {
// // get our dao
// final RuntimeExceptionDao<ActivityData, Integer> activityDao = getHelper()
// .getActivityDao();
// // query for all of the data objects in the database
// final List<ActivityData> list = activityDao.queryForAll();
// // our string builder for building the content-view
// final StringBuilder sb = new StringBuilder();
// sb.append("got ").append(list.size()).append(" entries in ")
// .append("activities").append("\n");
//
// // if we already have items in the database
// int simpleC = 0;
// for (final ActivityData simple : list) {
// sb.append("------------------------------------------\n");
// sb.append("[").append(simpleC).append("] = ").append(simple)
// .append("\n");
// simpleC++;
// }
// sb.append("------------------------------------------\n");
// for (final ActivityData activity : list) {
// activityDao.delete(activity);
// sb.append("deleted id ").append(activity.getId()).append("\n");
// Log.i(LOG_TAG, "deleting activity(" + activity.getId() + ")");
// simpleC++;
// }
//
// int createNum;
// do {
// createNum = new Random().nextInt(3) + 1;
// } while (createNum == list.size());
// for (int i = 0; i < createNum; i++) {
// // create a new simple object
// final ActivityData activity = new ActivityData();
// activity.setDate(new Date());
// // store it in the database
// activityDao.create(activity);
// Log.i(LOG_TAG, "created activity(" + activity.getDate() + ")");
// // output it
// sb.append("------------------------------------------\n");
// sb.append("created new entry #").append(i + 1).append(":\n");
// sb.append(activity).append("\n");
// try {
// Thread.sleep(5);
// } catch (final InterruptedException e) {
// // ignore
// }
// }
//
// Log.i(LOG_TAG, "Done with page at " + System.currentTimeMillis());
// return sb.toString();
// }
