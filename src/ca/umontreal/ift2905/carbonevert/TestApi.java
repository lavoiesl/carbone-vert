package ca.umontreal.ift2905.carbonevert;

import java.sql.SQLException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ProductData;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

public class TestApi extends OrmLiteBaseActivity<DatabaseHelper> {

	GetDataTask testData;
	private Dao<ProductData, Integer> dao = null;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_api);

		try {
			dao = getHelper().getDao(ProductData.class);
		} catch (final SQLException e) {
			Toast.makeText(getBaseContext(), "Error while listing activities",
					Toast.LENGTH_SHORT).show();
			return;
		}

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
				Toast.makeText(TestApi.this, "J'aime le bleu",
						Toast.LENGTH_LONG).show();
				testData = new GetDataTask();
				testData.execute();
			}
		});

	}

	private class GetDataTask extends AsyncTask<String, Integer, WebApi> {
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(TestApi.this, "Cancel!!!!!", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected WebApi doInBackground(final String... params) {
			try {
				Thread.sleep(5000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			final WebApi web = new WebApi(
					"http://ask.amee.com/detail.json?category=CLM_food_lifecycle_emissions&terms=chicken&quantities=1.0;kg");

			try {
				Thread.sleep(5000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			return web;
		}

		@Override
		protected void onProgressUpdate(final Integer... s) {

		}

		@Override
		protected void onPostExecute(final WebApi web) {
			if (web == null) {
				return;
			}
			// Toast.makeText(TestApi.this, "Execute",
			// Toast.LENGTH_SHORT).show();
			Toast.makeText(TestApi.this, web.getObs(), Toast.LENGTH_LONG)
					.show();
		}
	}
}
