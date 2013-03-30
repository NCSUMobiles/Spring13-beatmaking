package com.example.beatmakingapp;

import java.util.Comparator;
import java.util.PriorityQueue;

import android.app.Activity;
import android.content.Context;
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
	int beatInBar = 0;
	int soundIndex = 0;
	Comparator<Sound> comp = new LongComparator();
	PriorityQueue<Sound> backQueue = new PriorityQueue<Sound>(10, comp);
	PriorityQueue<Sound> frontQueue = new PriorityQueue<Sound>(1, comp);
	private static Handler mainHandler = new Handler();
	private static SoundPool sp00;
	private static Thread playbackThread1;
	private static Looper l1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pattern_layout);
		final Context context = this;
		sp00 = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
		final int id00 = sp00.load(this, raw.closedhat, 1);
		final int id01 = sp00.load(this, raw.cymbal, 1);
		final int id02 = sp00.load(this, raw.halfopenhat, 1);
		final int id03 = sp00.load(this, raw.hitom, 1);
		final int id10 = sp00.load(this, raw.kick, 1);
		final int id11 = sp00.load(this, raw.lowtom, 1);
		final int id12 = sp00.load(this, raw.openhat, 1);
		final int id13 = sp00.load(this, raw.snare, 1);

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
									sp00.play(s.getId(), volume, volume, 1, 0,
											(float) 1.0);
									// }
									// });
								}
							}
							if (timeSinceLastBeat >= 60000 / bpm) {
								mainHandler.post(new Runnable() {
									public void run() {
										// sp00.play(id00, volume, volume, 1, 0,
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

		final Button trackButton = (Button) findViewById(R.id.track_button);
		trackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent trackIntent = new Intent().setClass(context,
						TrackActivity.class);
				startActivity(trackIntent);
			}
		});
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
		final Button pad_00 = (Button) findViewById(R.id.pad_00);
		pad_00.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						mainHandler.post(new Runnable() {
							public void run() {
								if (state_recording == true)
									backQueue.add(new Sound(id00, SystemClock
											.elapsedRealtime() - timeAtStart));
								sp00.play(id00, volume, volume, 1, 0,
										(float) 1.0);
							}
						});
					}
				}).start();
			}
		});
		final Button pad_01 = (Button) findViewById(R.id.pad_01);
		pad_01.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						mainHandler.post(new Runnable() {
							public void run() {
								if (state_recording == true)
									backQueue.add(new Sound(id01, SystemClock
											.elapsedRealtime() - timeAtStart));
								sp00.play(id01, volume, volume, 1, 0,
										(float) 1.0);
							}
						});
					}
				}).start();
			}
		});
		final Button pad_02 = (Button) findViewById(R.id.pad_02);
		pad_02.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						mainHandler.post(new Runnable() {
							public void run() {
								if (state_recording == true)
									backQueue.add(new Sound(id02, SystemClock
											.elapsedRealtime() - timeAtStart));
								sp00.play(id02, volume, volume, 1, 0,
										(float) 1.0);
							}
						});
					}
				}).start();
			}
		});
		final Button pad_03 = (Button) findViewById(R.id.pad_03);
		pad_03.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						mainHandler.post(new Runnable() {
							public void run() {
								if (state_recording == true)
									backQueue.add(new Sound(id03, SystemClock
											.elapsedRealtime() - timeAtStart));
								sp00.play(id03, volume, volume, 1, 0,
										(float) 1.0);
							}
						});
					}
				}).start();
			}
		});
		final Button pad_10 = (Button) findViewById(R.id.pad_10);
		pad_10.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						mainHandler.post(new Runnable() {
							public void run() {
								if (state_recording == true)
									backQueue.add(new Sound(id10, SystemClock
											.elapsedRealtime() - timeAtStart));
								sp00.play(id10, volume, volume, 1, 0,
										(float) 1.0);
							}
						});
					}
				}).start();
			}
		});
		final Button pad_11 = (Button) findViewById(R.id.pad_11);
		pad_11.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						mainHandler.post(new Runnable() {
							public void run() {
								if (state_recording == true)
									backQueue.add(new Sound(id11, SystemClock
											.elapsedRealtime() - timeAtStart));
								sp00.play(id11, volume, volume, 1, 0,
										(float) 1.0);
							}
						});
					}
				}).start();
			}
		});
		final Button pad_12 = (Button) findViewById(R.id.pad_12);
		pad_12.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						mainHandler.post(new Runnable() {
							public void run() {
								if (state_recording == true)
									backQueue.add(new Sound(id12, SystemClock
											.elapsedRealtime() - timeAtStart));
								sp00.play(id12, volume, volume, 1, 0,
										(float) 1.0);
							}
						});
					}
				}).start();
			}
		});
		final Button pad_13 = (Button) findViewById(R.id.pad_13);
		pad_13.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						mainHandler.post(new Runnable() {
							public void run() {
								if (state_recording == true)
									backQueue.add(new Sound(id13, SystemClock
											.elapsedRealtime() - timeAtStart));
								sp00.play(id13, volume, volume, 1, 0,
										(float) 1.0);
							}
						});
					}
				}).start();
			}
		});
	}

	public void onDestroy() {
		super.onDestroy();
		l1.quit();
		if (sp00 != null)
			sp00.release();
	}
}
