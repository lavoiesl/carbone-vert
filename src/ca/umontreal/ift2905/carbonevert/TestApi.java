package ca.umontreal.ift2905.carbonevert;

import java.sql.SQLException;

import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.CategoryData;
import ca.umontreal.ift2905.carbonevert.model.ProductData;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TestApi extends OrmLiteBaseActivity<DatabaseHelper> {
	
	GetDataTask testData;
	
	private Dao<ProductData, Integer> daoP = null;
	private ProductData product;
	
	private Dao<CategoryData, Integer> daoC = null;
	private CategoryData category;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_api);
		
		
		
		try {
			daoP = getHelper().getDao(ProductData.class);
			daoC = getHelper().getDao(CategoryData.class);
		} catch (final SQLException e) {
			Toast.makeText(getBaseContext(), "Error while listing activities", Toast.LENGTH_SHORT)
				.show();
			return;
		}

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				
				testData=new GetDataTask();
				testData.execute();
				
			}
		});

	}
	
	private class GetDataTask extends AsyncTask<String,Integer, WebApi> {
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(TestApi.this, "Cancel!!!!!", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onPreExecute() {
		}
		
		@Override
		protected WebApi doInBackground(String... params) {
			
			try {Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			WebApi web = 
					new WebApi("http://ask.amee.com/detail.json?category=CLM_food_lifecycle_emissions&terms=chicken&quantities=1.0+kg");
			try {Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return web;
		}
		@Override
		protected void onProgressUpdate(Integer... s) {

		}
		@Override
		protected void onPostExecute(WebApi web) {
			if( web==null ) {
				Toast.makeText(TestApi.this, "Probleme avec le Web", Toast.LENGTH_SHORT).show();
				return;
			}

			try {				
				daoC.create(web.getCategory());
				daoP.create(web.getProduct());
			} catch (Exception e) {
			
				Toast.makeText(TestApi.this, "Erreur d'insertion", Toast.LENGTH_SHORT).show();
				throw new RuntimeException(e);
			}

		}
	}
}
