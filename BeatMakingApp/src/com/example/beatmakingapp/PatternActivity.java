package com.example.beatmakingapp;

import java.util.ArrayList;
import java.util.PriorityQueue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PatternActivity extends Activity {
	boolean state_playing = false;
	boolean state_recording = false;
	long timeAtStart = 0;
	long timeSinceStart = 0;
	long timeAtLastBeat = 0;
	long timeSinceLastBeat = 0;
	boolean currentPlaybackThread = true;
	int bpm = 120;
	int bars = 4;

	int check = 0;
	int beatInBar = 0;
	int soundIndex = 0;

	PriorityQueue<Sound> frontQueue = new PriorityQueue<Sound>(1, Global.comp);
	private static Handler mainHandler = new Handler();

	private static Thread playbackThread1;
	private static Looper l1;

	private int patternId = -1;

	public int[][] padIds = {
			{ R.id.pad_00, R.id.pad_01, R.id.pad_02, R.id.pad_03 },
			{ R.id.pad_10, R.id.pad_11, R.id.pad_12, R.id.pad_13 },
			{ R.id.pad_20, R.id.pad_21, R.id.pad_22, R.id.pad_23 },
			{ R.id.pad_30, R.id.pad_31, R.id.pad_32, R.id.pad_33 } };

	public static final String BUTTON_NAMES = "ButtonNames";
	public static SharedPreferences buttonNames;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.pattern_layout);
		final Context context = this;
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		 // Restore preferences
	       buttonNames = getSharedPreferences(BUTTON_NAMES, 0);
		
	       Global.patternContext = this;
		if(Global.initialized == false) {
			Global.initialize();
			Global.initialized = true;
		}
		
		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		switch( audio.getRingerMode() ){
		case AudioManager.RINGER_MODE_NORMAL:
		   break;
		case AudioManager.RINGER_MODE_SILENT:

		case AudioManager.RINGER_MODE_VIBRATE:
			new AlertDialog.Builder(this)
			.setTitle("Phone in Silent Mode!")
			.setPositiveButton(android.R.string.yes,null).create().show();
		   break;
		}
		

		
		String message = "";
		Intent intent = getIntent();
		message = intent.getStringExtra("msgFromParent");
		if (message == null)
			message = "default";
		
		final Button button = (Button) findViewById(R.id.pattern_number_button);
		if(message.equals("first") || message.equals("default") ||  message.equals("default : first")) {
			patternId = 0;
			bars = Global.pattern1Bars;
			button.setText("Pat-1");
			updateGradient("yellow");
		}
		else if(message.equals("second")) {
			patternId = 1;
			bars = Global.pattern1Bars;
			button.setText("Pat-2");
			updateGradient("green");
		}
		else if(message.equals("third")) {
			patternId = 2;
			bars = Global.pattern1Bars;
			button.setText("Pat-3");
			updateGradient("blue");
		}
		else if(message.equals("fourth")) {
			patternId = 3;
			button.setText("Pat-4");
			updateGradient("purple");
		}
		

//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//				context);
//		alertDialogBuilder.setTitle(((Integer)patternId).toString());
//		AlertDialog alertDialog = alertDialogBuilder.create();
//		alertDialog.show();

		AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
		final float volume = (float) am
				.getStreamVolume(AudioManager.STREAM_MUSIC);

		final ProgressBar progBar = (ProgressBar) findViewById(R.id.progBar);
		progBar.setProgress(0);

		playbackThread1 = new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				l1 = Looper.myLooper();
				final Handler handler = new Handler(Looper.myLooper());
				handler.post(new Runnable() {
					public void run() {
						if (state_playing == true) {
							
							long currentTime = SystemClock.elapsedRealtime();
							timeSinceStart = currentTime - timeAtStart;
							timeSinceLastBeat = currentTime - timeAtLastBeat;
							
					
							if (timeSinceLastBeat >= 60000 / bpm && Global.metronome==true) {
									Global.arrSoundPool.get(patternId).play(Global.soundIds[0][0], volume*(float)0.02, volume*(float)0.02, 1, 0, (float) 1.0);

							}
							if (frontQueue.size() > 0) {
								while (frontQueue.size() > 0
										&& frontQueue.peek().getOffset() <= timeSinceStart) {
									final Sound s = frontQueue.remove();
									mainHandler.post(new Runnable() {
										public void run() {
											Button button = (Button) findViewById(padIds[s.getButtonId_i()][s.getButtonId_j()]);

											Animation animation = new AlphaAnimation(1, 0);
		                                    animation.setDuration(100); // duration - half a second
		                                    animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
		                                    animation.setRepeatMode(Animation.REVERSE);
		                                    button.startAnimation(animation);
										}
										
									});
									// mainHandler.post(new Runnable() {
									// public void run() {
									Global.arrSoundPool.get(patternId).play(
											s.getSoundPoolId(), volume, volume,
											1, 0, (float) 1.0);
									// }
									// });
								}
							}
							if (timeSinceLastBeat >= 60000 / Global.bpm) {
								mainHandler.post(new Runnable() {
									public void run() {
										// arrSoundPool.get(0).play(id00,
										// volume, volume, 1, 0,
										// (float) 1.0);
									}
								});

								timeAtLastBeat = currentTime;
								//
							}
							if (timeSinceStart >= ((240000 * bars) / Global.bpm)) {
								timeSinceStart = 0;
								timeAtStart = currentTime;
								frontQueue.clear();
								frontQueue.addAll(Global.patternSoundQueues.get(patternId));
							}

							mainHandler.post(new Runnable() {
								public void run() {
									progBar.setProgress((int) (timeSinceStart * 25 * Global.bpm)
											/ (60000 * bars));
									if(state_playing == false)
										progBar.setProgress(0);
								}
							});

						}
						handler.postDelayed(this, 1);
						// handler.post(this);

					}

				});
				Looper.loop();
			}
		}, "PlaybackThread");
		playbackThread1.start();

		// final Button trackButton = (Button) findViewById(R.id.track_button);
		// trackButton.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		//
		//
		// }
		// });

		final ImageButton playButton = (ImageButton) findViewById(R.id.play_button);
		final ImageButton recordButton = (ImageButton) findViewById(R.id.record_button);
		final ImageButton stopButton = (ImageButton) findViewById(R.id.stop_button);
		
		final Button editDrumMachine = (Button) findViewById(R.id.edit_drum_machine_button);
		editDrumMachine.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent editDrumMachineIntent = new Intent().setClass(context,EditSoundsActivity.class)
						.putExtra("PatternNo", patternId);
				startActivity(editDrumMachineIntent);
			}
		});
		
		playButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				state_playing = true;
				playButton.setImageResource(R.drawable.play_button_pressed);
				timeAtStart = SystemClock.elapsedRealtime();
				timeAtLastBeat = SystemClock.elapsedRealtime();
				frontQueue.clear();
				frontQueue.addAll(Global.patternSoundQueues.get(patternId));
				
				recordButton.setEnabled(false);
			}
		});

		
		recordButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				state_recording = true;
				state_playing = true;
				timeAtStart = SystemClock.elapsedRealtime();
				timeAtLastBeat = SystemClock.elapsedRealtime();
				recordButton.setImageResource(R.drawable.record_button_pressed);
				frontQueue.clear();
				frontQueue.addAll(Global.patternSoundQueues.get(patternId));
				
				playButton.setEnabled(false);
			}
		});
		
		
		stopButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				state_playing = false;
				state_recording = false;
				
				playButton.setEnabled(true);
				recordButton.setEnabled(true);
				timeSinceStart = 0;
				progBar.setProgress(0);
				playButton.setImageResource(R.drawable.play_button_normal);
				recordButton.setImageResource(R.drawable.record_button_normal);
				

			}
		});

		ArrayList<ArrayList<Button>> pad = new ArrayList<ArrayList<Button>>();
		for (int i = 0; i < 4; i++)
			pad.add(new ArrayList<Button>());

		Button btn;



		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				final int ii = i;
				final int jj = j;

				btn = (Button) findViewById(padIds[i][j]);
				pad.get(i).add(btn);

				pad.get(i).get(j)
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								new Thread(new Runnable() {
									public void run() {
										mainHandler.post(new Runnable() {
											public void run() {
												if (state_recording == true)
													Global.patternSoundQueues.get(patternId).add(new Sound(
															Global.soundIds[ii][jj],
															ii,
															jj,
															(SystemClock
																	.elapsedRealtime()
																	- timeAtStart), patternId));
												Global.arrSoundPool.get(patternId).play(
														Global.soundIds[ii][jj],
														volume, volume, 1, 0,
														(float) 1.0);
											}
										});
									}
								}).start();
							}
						});
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Button btn;
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
				
				btn = (Button)(findViewById(padIds[i][j]));
				btn.setText(buttonNames.getString("p_"+i+j, "p_"+i+j));

			}
		}
		
	}

	public void callTrackActivity(View v) {
		Intent trackIntent = new Intent(this, TrackActivity.class);
		trackIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(trackIntent);
		finish();
	}

	public void callPatternOptions(View v) {
		final Dialog addPattern1Dialog = new Dialog(this);
		addPattern1Dialog.setContentView(R.layout.pattern_info_dialog);
		addPattern1Dialog.setTitle("Pattern Options");
		
		final Button done = (Button) addPattern1Dialog.findViewById(R.id.pattern_info_done_button);
		done.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addPattern1Dialog.dismiss();
			}
		});
		
		final Button clearPattern = (Button) addPattern1Dialog.findViewById(R.id.clear_pattern_button2);
		clearPattern.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				frontQueue.clear();
				Global.patternSoundQueues.get(patternId).clear();
				addPattern1Dialog.dismiss();
			}
		});
		addPattern1Dialog.show();
     	
	}
	

	public void updateGradient(String name) {
		Button btn;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				btn = (Button) findViewById(padIds[i][j]);
				if (name.compareTo("yellow") == 0)
					btn.setBackgroundResource(R.drawable.gradient_yellow);
				else if (name.compareTo("green") == 0)
					btn.setBackgroundResource(R.drawable.gradient_green);
				else if (name.compareTo("blue") == 0)
					btn.setBackgroundResource(R.drawable.gradient_blue);
				else if (name.compareTo("purple") == 0)
					btn.setBackgroundResource(R.drawable.gradient_purple);
			}
		}
	}

	
	public void onDestroy() {
		super.onDestroy();

		l1.quit();
		
		frontQueue.clear();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.metronome:
	            if (Global.metronome==true)
	            {
	            	Global.metronome=false;
	            	item.setTitle(R.string.metronomeOff);
	       
	            }
	            else
	            {
	            	Global.metronome=true;
	            	item.setTitle(R.string.metronomeOn);
	            }
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
