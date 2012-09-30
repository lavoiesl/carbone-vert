package ca.umontreal.ift2905.carbonevert.activity;

import java.util.HashMap;

import ca.umontreal.ift2905.carbonevert.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ProductViewActivity extends AbstractProductActivity {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final Button addButton = (Button) findViewById(R.id.productViewAddButton);
		addButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final HashMap<String, Integer> options = new HashMap<String, Integer>();
				options.put("product_id", product.getId());

				startActivity(ActivityEditActivity.class, options);
			}
		});
	}
	
	protected void setContentView() {
		setContentView(R.layout.product_view_layout);
	}
}
