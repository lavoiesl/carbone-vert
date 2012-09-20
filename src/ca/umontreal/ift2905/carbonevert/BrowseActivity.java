package ca.umontreal.ift2905.carbonevert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BrowseActivity extends Activity {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_layout);
		 
		
		final Button transportButton = (Button) findViewById(R.id.transportButton);
        transportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
//            	Intent intent = new Intent(v.getContext(), TestApi.class);
//        		startActivity(intent);
            	setContentView(R.layout.transport_layout);
            	final Button carButton = (Button) findViewById(R.id.carButton);
            	carButton.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						Toast.makeText(getBaseContext(), "CAR HERE", Toast.LENGTH_LONG).show();
					}
				});
            }
        });
		
        
        
        final Button foodButton = (Button) findViewById(R.id.foodButton);
        foodButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent = new Intent(v.getContext(), TestApi.class);
        		startActivity(intent);
            }
        });
        
        
		final Button vehiclesButton = (Button) findViewById(R.id.vehiclesButton);
        vehiclesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent = new Intent(v.getContext(), TestApi.class);
        		startActivity(intent);
            }
        });
        
	}
}
