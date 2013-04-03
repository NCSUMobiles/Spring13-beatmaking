package com.example.beatmakingapp;

import com.example.beatmakingapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TrackActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.example.beatmakingapp.MESSAGE";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_layout);
        
        final Context context = this;

		final Button pattern1Button = (Button) findViewById(R.id.pattern1_button);
		pattern1Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent patternIntent = new Intent().setClass(context,
						PatternActivity.class);
				patternIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(patternIntent);
			}
		});
     
		final Button pattern2Button = (Button) findViewById(R.id.pattern2_button);
		pattern2Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent().setClass(context, PatternActivity.class);
			    
			    String message = "second";
			    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			    intent.putExtra(EXTRA_MESSAGE, message);
			    startActivity(intent);

				
			}
		});
		
		final Button pattern3Button = (Button) findViewById(R.id.pattern3_button);
		pattern3Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent patternIntent = new Intent().setClass(context,
						PatternActivity.class);
				patternIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(patternIntent);
			}
		});
		
		final Button pattern4Button = (Button) findViewById(R.id.pattern4_button);
		pattern4Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent patternIntent = new Intent().setClass(context,
						PatternActivity.class);
				startActivity(patternIntent);
			}
		});
    }
    
    
}
