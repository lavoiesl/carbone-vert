package ca.umontreal.ift2905.carbonevert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.AbstractData;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.dao.Dao;

public class HistoryActivity extends OrmLiteBaseListActivity<DatabaseHelper> {
	
	private Bundle instance = null;
	private ArrayAdapter<? extends AbstractData> adapter = null;
	private ListView listView;

	//void
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.history_layout);
		instance = savedInstanceState;
		
		//Test initial
		listView = getListView();
		final StatHistory sh = new StatHistory(getHelper());
		sh.setFromDate(GregorianCalendar.getInstance());
		
		String[] values = new String[] { "Last week", "Last Month", "Last year", "Custom" };
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        R.layout.list_layout, values);
		    setListAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				switch (arg2) {
				
				case 0: sh.setListing(StatHistory.BY_WEEK);
						//setContentView(R.layout.graph_layout);
						
						listView = getListView();
						
						List<Double> values = new ArrayList<Double>();
						
						values.add(0.5);
						values.add(1.0);
						values.add(1.5);
						values.add(2.0);
						values.add(2.5);
						values.add(3.0);
						
						StatAdapter adapter22 = new StatAdapter(getBaseContext(), values);
						    setListAdapter(adapter22);
						    
						break;
						
				case 1: sh.setListing(StatHistory.BY_WEEK);
						setContentView(R.layout.graph_layout);
						break;
						
				case 2: sh.setListing(StatHistory.BY_WEEK);
						setContentView(R.layout.graph_layout);
						break;
						
				case 3: 
					setContentView(R.layout.custom_history_layout);
					setListAdapter(null);
					
					final Spinner spinner = (Spinner) findViewById(R.id.spinner);
					
					final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
							getBaseContext(), android.R.layout.simple_spinner_item);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner.setAdapter(adapter);

					adapter.add("By day");
					adapter.add("By week");
					adapter.add("By month");
					adapter.add("By year");

					final Button showButton = (Button) findViewById(R.id.showButton);
					showButton.setOnClickListener(new View.OnClickListener() {

						DatePicker fromDatePicker = (DatePicker) findViewById(R.id.fromDatePicker);
						DatePicker toDatePicker = (DatePicker) findViewById(R.id.toDatePicker);

						public void onClick(final View v) {
							final int fromDay = fromDatePicker.getDayOfMonth();
							final int fromMonth = fromDatePicker.getMonth();
							final int fromYear = fromDatePicker.getYear();
							final GregorianCalendar fromDate = new GregorianCalendar(
									fromYear, fromMonth, fromDay);

							final int toDay = toDatePicker.getDayOfMonth();
							final int toMonth = toDatePicker.getMonth();
							final int toYear = toDatePicker.getYear();
							final GregorianCalendar toDate = new GregorianCalendar(
									toYear, toMonth, toDay);

							final int result = toDate.compareTo(fromDate);

							if (fromDate.before(toDate) || fromDate.equals(toDate)) {
								// the program runs normally
								Toast.makeText(
										getBaseContext(),
										"GOOD YOU ARE READY DO ADVANCE TO THE NEXT LEVEL : "
												+ result, Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(getBaseContext(),
										"Wrong Date selection", Toast.LENGTH_SHORT)
										.show();
							}

						}
					});
				    break;
						
				default:
					final Intent intent = new Intent(getBaseContext(), TestApi.class);
					startActivity(intent);
					break;
						
						
				}
				
				//final Intent intent = new Intent(v.getContext(), TestApi.class);
				//startActivity(intent);
				
			}
			});

/*
		final Button weekButton = (Button) findViewById(R.id.weekButton);
		weekButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
				sh.setListing(StatHistory.BY_WEEK);
				
				setContentView(R.layout.graph_layout);
				
				//final Intent intent = new Intent(v.getContext(), TestApi.class);
				//startActivity(intent);
			}
		});

		final Button monthButton = (Button) findViewById(R.id.monthButton);
		monthButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
				sh.setListing(StatHistory.BY_MONTH);
				
				final Intent intent = new Intent(v.getContext(), TestApi.class);
				startActivity(intent);
			}
		});

		final Button yearButton = (Button) findViewById(R.id.yearButton);
		yearButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
				sh.setListing(StatHistory.BY_YEAR);
				
				 final Intent intent = new Intent(v.getContext(), TestApi.class);
				 startActivity(intent);
			}
		});

		final Button customButton = (Button) findViewById(R.id.customButton);
		customButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click

				setContentView(R.layout.custom_history_layout);
				final Spinner spinner = (Spinner) findViewById(R.id.spinner);

				final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
						getBaseContext(), android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);

				adapter.add("By day");
				adapter.add("By week");
				adapter.add("By month");
				adapter.add("By year");

				final Button showButton = (Button) findViewById(R.id.showButton);
				showButton.setOnClickListener(new View.OnClickListener() {

					DatePicker fromDatePicker = (DatePicker) findViewById(R.id.fromDatePicker);
					DatePicker toDatePicker = (DatePicker) findViewById(R.id.toDatePicker);

					public void onClick(final View v) {
						final int fromDay = fromDatePicker.getDayOfMonth();
						final int fromMonth = fromDatePicker.getMonth();
						final int fromYear = fromDatePicker.getYear();
						final GregorianCalendar fromDate = new GregorianCalendar(
								fromYear, fromMonth, fromDay);

						final int toDay = toDatePicker.getDayOfMonth();
						final int toMonth = toDatePicker.getMonth();
						final int toYear = toDatePicker.getYear();
						final GregorianCalendar toDate = new GregorianCalendar(
								toYear, toMonth, toDay);

						final int result = toDate.compareTo(fromDate);

						if (fromDate.before(toDate) || fromDate.equals(toDate)) {
							// the program runs normally
							Toast.makeText(
									getBaseContext(),
									"GOOD YOU ARE READY DO ADVANCE TO THE NEXT LEVEL : "
											+ result, Toast.LENGTH_SHORT)
									.show();
						} else {
							// new AlertDialog.Builder(null)
							// .setTitle("Wrong Data Input!")
							// .setMessage("The end Date must be Before the start Date, please insert new Date values")
							// .setNeutralButton("Ok",new
							// DialogInterface.OnClickListener() {
							//
							// public void onClick(DialogInterface dialog, int
							// which) {
							//
							// }
							//
							// }).show();
							Toast.makeText(getBaseContext(),
									"Wrong Date selection", Toast.LENGTH_SHORT)
									.show();
						}

					}
				});

			}
		});
		*/

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
		onCreate(instance);
	}
	
	public class StatAdapter extends BaseAdapter{
		
	    private Context context;
	    private List<Double> statList;

	    public StatAdapter(Context context, List<Double> statList) {
	        this.context = context;
	        this.statList = statList;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup viewGroup) {
	        double value = statList.get(position);
	        if (convertView == null) {
	            LayoutInflater inflater = (LayoutInflater) context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = inflater.inflate(R.layout.graphlist_layout, null);
	        }
	        TextView tvLeft = (TextView) convertView.findViewById(R.id.graph_left);
	        tvLeft.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, (float) value));

	        TextView tvRight = (TextView) convertView.findViewById(R.id.graph_right);
	        tvRight.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f));
    
	        return convertView;
	    }

		public int getCount() {
			return statList.size();
		}

		public Object getItem(int arg0) {
			return null;
		}
	}

	
	private class StatHistory {
		
		public static final int BY_DAY = 0;
		public static final int BY_WEEK = 1;
		public static final int BY_MONTH = 2;
		public static final int BY_YEAR = 3;
		
		private int listing;
		
		private List<ActivityData> activities;
		private List<Double> total_co2 = new ArrayList<Double>();
		
		private final DatabaseHelper database;
		
		private Calendar fromDate;
		private Calendar toDate; 
		
		public StatHistory(DatabaseHelper database) {
			this.database = database;
		}
		
		public void setListing (int by_list) {
			listing = by_list;
		}
		
		public void setFromDate(Calendar date) {
			fromDate = date;	
		}
		
		public void setToDate(Calendar date) {
			toDate = date;	
		}
		
		private List<ActivityData> findByDate(String date) {			
			try {
				final Dao<ActivityData, Integer> dao = database.getDao(ActivityData.class);
				return dao.query(dao.queryBuilder().where().gt("date", date).prepare());
			} catch (SQLException e) {
				return null;
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
		}
		
		public void fillList() {
			activities = findByDate(fromDate.toString());
			total_co2.set(1, 0.0);
			
			for (ActivityData temp : activities) {
				total_co2.set(1,total_co2.get(1)+temp.getCarbon());
			}
		}
		
	}
}
