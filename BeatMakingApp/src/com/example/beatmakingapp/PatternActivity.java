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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

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
	Comparator<Sound> comp = new LongComparator();
	PriorityQueue<Sound> backQueue = new PriorityQueue<Sound>(10, comp);
	PriorityQueue<Sound> frontQueue = new PriorityQueue<Sound>(1, comp);
	private static Handler mainHandler = new Handler();

	private static Thread playbackThread1;
	private static Looper l1;

	private final int[][] soundIds = new int[4][4];
	private ArrayList<SoundPool> arrSoundPool = new ArrayList<SoundPool>();


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pattern_layout);
		final Context context = this;
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		String message = "";		
		Intent intent = getIntent();
		message = intent.getStringExtra("msgFromParent");
		if (message == null)
			message = "default";

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(message);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
			
		
		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));

		for (int i=0;i<4;i++)
		{
			soundIds[0][0] = arrSoundPool.get(i).load(this, raw.closedhat, 1);
			soundIds[0][1] = arrSoundPool.get(i).load(this, raw.cymbal, 1);
			soundIds[0][2] = arrSoundPool.get(i).load(this, raw.halfopenhat, 1);
			soundIds[0][3] = arrSoundPool.get(i).load(this, raw.hitom, 1);
			soundIds[1][0] = arrSoundPool.get(i).load(this, raw.kick, 1);
			soundIds[1][1] = arrSoundPool.get(i).load(this, raw.lowtom, 1);
			soundIds[1][2] = arrSoundPool.get(i).load(this, raw.openhat, 1);
			soundIds[1][3] = arrSoundPool.get(i).load(this, raw.snare, 1);
			soundIds[2][0] = arrSoundPool.get(i).load(this, raw.closedhat, 1);
			soundIds[2][1] = arrSoundPool.get(i).load(this, raw.cymbal, 1);
			soundIds[2][2] = arrSoundPool.get(i).load(this, raw.halfopenhat, 1);
			soundIds[2][3] = arrSoundPool.get(i).load(this, raw.hitom, 1);
			soundIds[3][0] = arrSoundPool.get(i).load(this, raw.kick, 1);
			soundIds[3][1] = arrSoundPool.get(i).load(this, raw.lowtom, 1);
			soundIds[3][2] = arrSoundPool.get(i).load(this, raw.openhat, 1);
			soundIds[3][3] = arrSoundPool.get(i).load(this, raw.snare, 1);	
		}


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
									arrSoundPool.get(0).play(s.getId(), volume, volume, 1, 0,
											(float) 1.0);
									// }
									// });
								}
							}
							if (timeSinceLastBeat >= 60000 / bpm) {
								mainHandler.post(new Runnable() {
									public void run() {
										// arrSoundPool.get(0).play(id00, volume, volume, 1, 0,
										// (float) 1.0);
									}
								});

								timeAtLastBeat = currentTime;
								//
							}
							if (timeSinceStart >= ((240000 * bars) / bpm)) {
								timeSinceStart = 0;
								timeAtStart = currentTime;
								frontQueue.clear();
								frontQueue.addAll(backQueue);
							}

							mainHandler.post(new Runnable() {
								public void run() {
									progBar.setProgress((int) (timeSinceStart * 25 * bpm)
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


//		final Button trackButton = (Button) findViewById(R.id.track_button);
//		trackButton.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//
//				
//			}
//		});

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
		for(int i = 0; i < 4; i++)
			pad.add(new ArrayList<Button>());


		Button btn;

		int[][] padIds = {{R.id.pad_00,R.id.pad_01,R.id.pad_02,R.id.pad_03},{R.id.pad_10,R.id.pad_11,R.id.pad_12,R.id.pad_13},{R.id.pad_20,R.id.pad_21,R.id.pad_22,R.id.pad_23},{R.id.pad_30,R.id.pad_31,R.id.pad_32,R.id.pad_33}};

		for(int i=0;i<4;i++)
		{
			for(int j=0;j<4;j++)
			{
				final int ii = i;
				final int jj = j;

				btn = (Button) findViewById(padIds[i][j]);
				pad.get(i).add(btn);


				pad.get(i).get(j).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						new Thread(new Runnable() {
							public void run() {
								mainHandler.post(new Runnable() {
									public void run() {


										if (state_recording == true)
											backQueue.add(new Sound(soundIds[ii][jj], SystemClock
													.elapsedRealtime() - timeAtStart));
										arrSoundPool.get(0).play(soundIds[ii][jj], volume, volume, 1, 0,
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
	
	public void callTrackActivity(View v)
	{
		Intent trackIntent = new Intent(this,TrackActivity.class);
		trackIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(trackIntent);
		finish();
	}
	

	
	
	public void onDestroy() {
		super.onDestroy();
		l1.quit();
		if (arrSoundPool.get(0) != null)
			arrSoundPool.get(0).release();
	}
}
