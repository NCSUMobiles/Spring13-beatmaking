package com.example.beatmakingapp;

import java.util.ArrayList;

import com.example.beatmakingapp.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EditSoundsActivity extends FragmentActivity implements RenameDialogFragment.RenameDialogListener {

	private int callingPattern = 0;
	private int sid, x, y, longPressedButton;
	private ArrayList<ArrayList<Button>> pad = new ArrayList<ArrayList<Button>>();
	private int[][] padIds = {{R.id.pad_00,R.id.pad_01,R.id.pad_02,R.id.pad_03},{R.id.pad_10,R.id.pad_11,R.id.pad_12,R.id.pad_13},{R.id.pad_20,R.id.pad_21,R.id.pad_22,R.id.pad_23},{R.id.pad_30,R.id.pad_31,R.id.pad_32,R.id.pad_33}};
	private RenameDialogFragment rdf;
	private SharedPreferences buttonNames;
	private SharedPreferences.Editor editor;
	public int soundId[][] = new int[4][4];
	private SoundPool sp ;
	private Bundle b;
	private Button editDrumMachine;
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pattern_layout);

		for(int i = 0; i < 4; i++)
			pad.add(new ArrayList<Button>());
		
		rdf = new RenameDialogFragment();

		buttonNames = PatternActivity.buttonNames;
		editor = buttonNames.edit();
		b = getIntent().getExtras();
		callingPattern = b.getInt("PatternNo");
		sp = Global.arrSoundPool.get(callingPattern);
		
		editDrumMachine = (Button)findViewById(R.id.edit_drum_machine_button);
		editDrumMachine.setText("Finish Editting");
		editDrumMachine.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		Button btn;
		
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
				final int ii = i;
				final int jj = j;
					
				btn = (Button) findViewById(padIds[i][j]);
				btn.setText(buttonNames.getString("p_"+i+j, "p_"+i+j));
				pad.get(i).add(btn);
				
				pad.get(i).get(j).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						x=ii;
						y=jj;
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				        intent.setType("audio/*");
				        startActivityForResult(Intent.createChooser(intent,"Select Audio "), 1);
					}
				});
				
				pad.get(i).get(j).setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						x=ii;
						y=jj;
						// TODO Auto-generated method stub
						longPressedButton = v.getId();
						rdf.bidx = x;
						rdf.bidy = y;
						rdf.show(getSupportFragmentManager(), "rename");
						return false;
					}
				});
			}
		}
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK && requestCode == 1) {
	        // Perform a query to the contact's content provider for the contact's name
			Uri uri = data.getData();
			String path = uri.toString();
			String[] proj = {MediaStore.Audio.Media.DATA};
			Cursor c = getContentResolver().query(uri,proj,null,null,null);
			int ci = 0;
			if(c.moveToFirst())
			{
				ci = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
				path = c.getString(ci);
			}
			c.close();
			final String temppath = path;
			sid = sp.load(path, 1);
			soundId[x][y] = sid;
			pad.get(x).get(y).setText(""+sid);
			final int temp = sid;
			sp.setOnLoadCompleteListener(new OnLoadCompleteListener() {

				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId,
						int status) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Loaded\nsid: "+temp+"\nPath: "+temppath+"\n on: "+x+","+y, Toast.LENGTH_LONG).show();
				}

			});
	    }

	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		((Button)findViewById(longPressedButton)).setText(rdf.rename);
		editor.putString(""+callingPattern+"p_"+rdf.bidx+rdf.bidy, rdf.rename);
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		//editor.commit();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		editor.commit();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		editor.commit();
	}
	

}
