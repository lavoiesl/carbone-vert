package ca.umontreal.ift2905.carbonevert;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.AbstractData;
import ca.umontreal.ift2905.carbonevert.model.CategoryData;
import ca.umontreal.ift2905.carbonevert.model.ProductData;

//Fortement Inspirer des exemple du cours
public class WebApi {

	private final DatabaseHelper database;
	private final HttpClient httpClient;
	
	private final static String BASE_URL = "http://ask.amee.com/detail.json?category=CLM_food_lifecycle_emissions"; 

	WebApi(DatabaseHelper database) {
		this.database = database;

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setContentCharset(params, "utf-8");
		httpClient = new DefaultHttpClient(params);

	}
	
	public ProductData loadDetails(final String terms, final double quantity, final String unit) throws IOException, JSONException, SQLException {
		Log.d("WebApi", "Searching for " + terms + " " + quantity + " " + unit);
		
		final String url = BASE_URL + "&terms=" + terms + "&quantities=" + quantity + "+" + unit;
		final JSONObject json = fetchJson(url);
		
		String items[] = json.getString("item").split(", ");

		if(items.length > 1) {
			CategoryData category = findCategory(items[0]);
			ProductData product = findProduct(items[1], category);
			return product;
		} else {
			throw new JSONException("No results");
		}
	}
	
	private CategoryData findCategory(String name) throws SQLException {
		CategoryData category = findByName(CategoryData.class, name);
		if (category == null) {
			category = new CategoryData();
			category.setName(name);
			database.getDao(CategoryData.class).create(category);
		}
		return category;
	}

	private ProductData findProduct(String name, CategoryData category) throws SQLException {
		ProductData product = findByName(ProductData.class, name);
		if (product == null) {
			product = new ProductData();
			product.setName(name);
			product.setCategory(category);
			database.getDao(ProductData.class).create(product);
		}
		return product;
	}
	
	private <T extends AbstractData> T findByName(Class<T> clazz, String name) {
		name = AbstractData.toCamelCase(name);
		try {
			return database.getDao(clazz).queryForEq("name", name).get(0);
		} catch (SQLException e) {
			return null;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	private JSONObject fetchJson(String url) throws IOException, JSONException  {
		HttpEntity response = getHttp(url);
		JSONObject json = new JSONObject(EntityUtils.toString(response, HTTP.UTF_8));
		
		return json;			
	}
	
	private HttpEntity getHttp(String url) throws IOException {
		HttpGet get = new HttpGet(url);
		HttpResponse response = httpClient.execute(get);
		return response.getEntity();
	}
	
	public abstract static class GetProductTask extends AsyncTask<String, Integer, ProductData> {
		final private WebApi web;
		
		public GetProductTask(DatabaseHelper helper) {
			web = new WebApi(helper);
		}
		
		public void loadDetails(final String terms, final double d, final String unit) {
			if (getStatus().equals(AsyncTask.Status.RUNNING)) {
				cancel(true);
			}
			execute(terms, "" + d, unit);
		}
		
		@Override
		protected ProductData doInBackground(String... params) {
			try {
				ProductData product = web.loadDetails(params[0], Float.parseFloat(params[1]), params[2]);

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
				
				return product;
			} catch (Exception e) {
//				Log.e("Load product", e.getMessage());
//				return null;
				throw new RuntimeException(e);
			}
		}

		/*
		@Override
		protected void onProgressUpdate(Integer... s) {

		}
		*/

		@Override
		abstract protected void onPostExecute(ProductData product);
	}
}