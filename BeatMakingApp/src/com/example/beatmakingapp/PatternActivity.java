package com.example.beatmakingapp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.beatmakingapp.R.raw;

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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.pattern_layout);
		final Context context = this;
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		Global.patternContext = this;
		if(Global.initialized == false) {
			Global.initialize();
			Global.initialized = true;
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
		}
		else if(message.equals("second")) {
			patternId = 1;
			bars = Global.pattern1Bars;
			button.setText("Pat-2");
		}
		else if(message.equals("third")) {
			patternId = 2;
			bars = Global.pattern1Bars;
			button.setText("Pat-3");
		}
		else if(message.equals("fourth")) {
			patternId = 3;
			button.setText("Pat-4");
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
							if (frontQueue.size() > 0) {
								while (frontQueue.size() > 0
										&& frontQueue.peek().getOffset() <= timeSinceStart) {
									final Sound s = frontQueue.remove();
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
		playButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				state_playing = true;
				playButton.setImageResource(R.drawable.play_button_pressed);
				timeAtStart = SystemClock.elapsedRealtime();
				timeAtLastBeat = SystemClock.elapsedRealtime();
			}
		});

		final ImageButton recordButton = (ImageButton) findViewById(R.id.record_button);
		recordButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				state_recording = true;
				recordButton.setImageResource(R.drawable.record_button_pressed);
			}
		});
		final ImageButton stopButton = (ImageButton) findViewById(R.id.stop_button);
		stopButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				state_playing = false;
				state_recording = false;
				playButton.setImageResource(R.drawable.play_button_normal);
				recordButton.setImageResource(R.drawable.record_button_normal);

			}
		});

		ArrayList<ArrayList<Button>> pad = new ArrayList<ArrayList<Button>>();
		for (int i = 0; i < 4; i++)
			pad.add(new ArrayList<Button>());

		Button btn;

		int[][] padIds = {
				{ R.id.pad_00, R.id.pad_01, R.id.pad_02, R.id.pad_03 },
				{ R.id.pad_10, R.id.pad_11, R.id.pad_12, R.id.pad_13 },
				{ R.id.pad_20, R.id.pad_21, R.id.pad_22, R.id.pad_23 },
				{ R.id.pad_30, R.id.pad_31, R.id.pad_32, R.id.pad_33 } };

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
	public void onResume() {
		// Always call the superclass so it can restore the view hierarchy
		super.onResume();

	}

	public void callTrackActivity(View v) {
		Intent trackIntent = new Intent(this, TrackActivity.class);
		trackIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(trackIntent);
		finish();
	}

	public void onDestroy() {
		super.onDestroy();
		l1.quit();
		//if (Global.arrSoundPool.get(patternId) != null)
			//Global.arrSoundPool.get(patternId).release();
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
