package ca.umontreal.ift2905.carbonevert;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import ca.umontreal.ift2905.carbonevert.db.DatabaseHelper;
import ca.umontreal.ift2905.carbonevert.model.ProductData;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class TestApi extends OrmLiteBaseActivity<DatabaseHelper> {
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_api);
		
		final WebApi.GetProductTask task = new WebApi.GetProductTask(getHelper()) {

			@Override
			protected void onPostExecute(ProductData product) {
				Toast.makeText(TestApi.this, "" + product, Toast.LENGTH_SHORT).show();
			}
			
		};

		final Button button = (Button) findViewById(R.id.button1);
		
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				task.loadDetails("shrimp", 1.0, "kg");
			}
		});

	}
}
