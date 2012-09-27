package ca.umontreal.ift2905.carbonevert;

import java.io.IOException;
import java.io.InputStream;

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

//Fortement Inspirer des exemple du cours
public class WebApi {

	private String erreur;
	private HttpEntity response;
	private JSONObject obs;

	WebApi(String keyword) {
		erreur=null;
		Log.d("webAPI","loading "+keyword);
		
		try {
			response=getHttp(keyword);
			
			// JSON format
			JSONObject js = new JSONObject(EntityUtils.toString(response,HTTP.UTF_8));

			// extraire la future requete
			obs = js.getJSONObject("subqueries");
		} catch (ClientProtocolException e) {
			erreur="erreur http(protocol):"+e.getMessage();
		} catch (IOException e) {
			erreur="erreur http(IO):"+e.getMessage();
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("CarboneVert","WebApi Done");
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
	
	public JSONObject getObs() {
		return obs;
	}
	
}