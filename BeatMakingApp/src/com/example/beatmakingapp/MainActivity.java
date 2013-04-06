package com.example.beatmakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;
        
		Intent intent2 = new Intent().setClass(context, TrackActivity.class);
	    intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    startActivity(intent2);
	    
		Intent intent = new Intent().setClass(context, PatternActivity.class);
	    String message = "default : first";
	    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    intent.putExtra("msgFromParent", message);
	    startActivity(intent);
	    
	    finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
}
