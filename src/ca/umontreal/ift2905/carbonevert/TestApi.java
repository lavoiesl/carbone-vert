package ca.umontreal.ift2905.carbonevert;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TestApi extends Activity {

	
	GetDataTask testData;
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_api);

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				// Perform action on click
				Toast.makeText(TestApi.this, "J'aime le bleu",
						Toast.LENGTH_LONG).show();
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
		protected void onPreExecute() {
		}
		protected WebApi doInBackground(String... params) {
			for(int i=0;i<100;i+=10) {
				try {Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if( this.isCancelled() ) return null;
			}
			WebApi web = 
					new WebApi("http://ask.amee.com/answer.json?q=1+kg+of+beef");
			
			return web;
		}
		protected void onProgressUpdate(Integer... s) {

		}
		protected void onPostExecute(WebApi web) {
			if( web==null ) return;
			//Toast.makeText(TestApi.this, "Execute", Toast.LENGTH_SHORT).show();
			Toast.makeText(TestApi.this, web.getObs(),
					Toast.LENGTH_LONG).show();
		}
	}
}
