package ca.umontreal.ift2905.carbonevert;

import java.sql.SQLException;

import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ActivityData;
import ca.umontreal.ift2905.carbonevert.model.CategoryData;
import ca.umontreal.ift2905.carbonevert.model.ProductData;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
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
		
		category = new CategoryData();
		product = new ProductData();
		
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
				
				try {
					daoC.create(category);
					daoP.create(product);
				} catch (final SQLException e) {
					Toast.makeText(getBaseContext(), "Error while inserting", Toast.LENGTH_SHORT)
						.show();
					return;
				}
			}
		});

	}
	
	private class GetDataTask extends AsyncTask<String,Integer, WebApi> {
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(TestApi.this, "Cancel!!!!!", Toast.LENGTH_SHORT).show();
		}
		
		protected void onPreExecute() {
		}
		
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
		protected void onProgressUpdate(Integer... s) {

		}
		protected void onPostExecute(WebApi web) {
			if( web==null ) {
				Toast.makeText(TestApi.this, "Probleme avec le Web", Toast.LENGTH_SHORT).show();
				return;
			}
			if (web.getAns().equalsIgnoreCase("null")) {
				Toast.makeText(TestApi.this, "Aucun terme de ce genre", Toast.LENGTH_LONG).show();
				return;
			}
						
			category.setName(web.getCat());
			
			//product.setId(web.getUid());
			product.setCategory(category);
			product.setName(web.getItem());
		}
	}
}
