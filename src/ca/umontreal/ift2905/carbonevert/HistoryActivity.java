package ca.umontreal.ift2905.carbonevert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HistoryActivity extends Activity {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_layout);
		
		final Button weekButton = (Button) findViewById(R.id.weekButton);
        weekButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent = new Intent(v.getContext(), TestApi.class);
        		startActivity(intent);
            }
        });
		
		final Button monthButton = (Button) findViewById(R.id.monthButton);
        monthButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent = new Intent(v.getContext(), TestApi.class);
        		startActivity(intent);
            }
        });
        
        
		final Button yearButton = (Button) findViewById(R.id.yearButton);
        yearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent = new Intent(v.getContext(), TestApi.class);
        		startActivity(intent);
            }
        });
        
        
		final Button customButton = (Button) findViewById(R.id.customButton);
        customButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	Intent intent = new Intent(v.getContext(), TestApi.class);
        		startActivity(intent);
            }
        });
        
        
	}
}
