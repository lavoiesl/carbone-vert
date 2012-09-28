package ca.umontreal.ift2905.carbonevert;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import ca.umontreal.ift2905.carbonevert.model.CategoryData;
import ca.umontreal.ift2905.carbonevert.model.ProductData;

//Fortement Inspirer des exemple du cours
public class WebApi {

	private String erreur;
	
	private HttpEntity response;
	
	private ProductData product;
	private CategoryData category;

	WebApi(String keyword) {
		erreur="";
		category = new CategoryData();
		product = new ProductData();
		Log.d("WebApi","loading "+keyword);
		String[] items;
		
		try {
			response=getHttp(keyword);

			JSONObject js = new JSONObject(EntityUtils.toString(response,HTTP.UTF_8));
			
			items = js.getString("item").split(", ");
			
			if(items.length > 1) {
				category.setName(items[0]);
				product.setName(items[1]);
			}

//			answer = js.getString("answer");
//			unit = js.getString("unit");
//			item = js.getString("item");
//			category = js.getString("category");
//			uid = js.getString("item_uid");
						
		} catch (ClientProtocolException e) {
			erreur=erreur+" erreur http(protocol):"+e.getMessage();
		} catch (IOException e) {
			erreur=erreur+"erreur http(IO):"+e.getMessage();
        } catch (ParseException e) {
        	erreur=erreur+"erreur de Parsing:"+e.getMessage();
		} catch (JSONException e) {
			erreur=erreur+"erreur de Json:"+e.getMessage();
		}
		
		if(erreur.length() != 0)
			Log.d("WebApi","Erreur:"+erreur);
		
		Log.d("WebApi","WebApi Done");
	}
	
	private HttpEntity getHttp(String url) throws ClientProtocolException, IOException
	{
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setContentCharset(params, "utf-8");

		HttpClient httpClient = new DefaultHttpClient(params);
		HttpGet http = new HttpGet(url);
		HttpResponse response = httpClient.execute(http);
		return response.getEntity();
	}
	
	public ProductData getProduct() {	
		return product;
	}
	
	public CategoryData getCategory() {	
		return category;
	}
	

	
}