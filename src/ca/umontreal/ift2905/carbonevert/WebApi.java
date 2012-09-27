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
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

//Fortement Inspirer des exemple du cours
public class WebApi {

	private String erreur;
	private HttpEntity response;
	private String obs;

	WebApi(final String keyword) {
		erreur = null;
		Log.d("WebApi", "loading " + keyword);

		try {
			response = getHttp(keyword);

			if (response == null) {
				Log.d("WebApi", "getHttp is null");
			}

			final JSONObject js = new JSONObject(EntityUtils.toString(response,
					HTTP.UTF_8));

			if (js.length() == 0) {
				Log.d("WebApi", "Json is null");
			}

			obs = js.getString("unit");

			if (obs == null) {
				Log.d("WebApi", "String is null");
			}

		} catch (final ClientProtocolException e) {
			erreur = "erreur http(protocol):" + e.getMessage();
		} catch (final IOException e) {
			erreur = "erreur http(IO):" + e.getMessage();
		} catch (final ParseException e) {
			erreur = "erreur de Parsing:" + e.getMessage();
		} catch (final JSONException e) {
			erreur = "erreur de Json:" + e.getMessage();
		}

		if (obs.length() == 0) {
			Log.d("WebApi", "what" + erreur);
		}

		Log.d("WebApi", "WebApi Done");
	}

	private HttpEntity getHttp(final String url)
			throws ClientProtocolException, IOException {
		final HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setContentCharset(params, "utf-8");

		final HttpClient httpClient = new DefaultHttpClient(params);
		final HttpGet http = new HttpGet(url);
		final HttpResponse response = httpClient.execute(http);
		return response.getEntity();
	}

	public String getObs() {

		return obs;
	}

}