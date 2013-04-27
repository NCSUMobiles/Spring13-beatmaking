package com.example.beatmakingapp;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TrackActivity extends Activity {

	private Button pattern1Button, pattern2Button, pattern3Button,
			pattern4Button;
	private ImageButton addPattern1Button, addPattern2Button,
			addPattern3Button, addPattern4Button;
	private Context context = this;

	/*
	 * private ArrayList<Integer> Global.pattern1SegmentPositions = new
	 * ArrayList<Integer>(); private ArrayList<Integer>
	 * Global.pattern2SegmentPositions = new ArrayList<Integer>(); private
	 * ArrayList<Integer> Global.pattern3SegmentPositions = new
	 * ArrayList<Integer>(); private ArrayList<Integer>
	 * Global.pattern4SegmentPositions = new ArrayList<Integer>();
	 */
	private int maxBars = 0;
	private int num = 1; // reusable number to update new pattern segment
							// positions

	boolean state_playing = false;
	long timeAtStart = 0;
	long timeSinceStart = 0;
	private static Thread playbackThread1;
	private static Looper l1;
	private ImageButton playButton;
	private ImageButton stopButton;
	private static Handler mainHandler = new Handler();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.track_layout2);
		AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
		final float volume = (float) am
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		playbackThread1 = new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				l1 = Looper.myLooper();
				final Handler handler = new Handler(Looper.myLooper());
				handler.post(new Runnable() {
					public void run() {
						if (state_playing == true) {
							// Toast.makeText(context,
							// Integer.toString(Global.patternSoundQueues.get(0).size()),
							// Toast.LENGTH_SHORT).show();
							long currentTime = SystemClock.elapsedRealtime();
							timeSinceStart = currentTime - timeAtStart;
							if (Global.trackSoundQueue.size() > 0) {
								while (Global.trackSoundQueue.size() > 0
										&& Global.trackSoundQueue.peek()
												.getOffset() <= timeSinceStart) {
									final Sound s = Global.trackSoundQueue
											.remove();
									// mainHandler.post(new Runnable() {
									// public void run() {
									// Toast.makeText(
									// context,
									// Long.toString(s.getOffset())
									// + " "
									// + Long.toString(timeSinceStart),
									// Toast.LENGTH_SHORT).show();
									Global.arrSoundPool.get(s.getPatternId())
											.play(s.getSoundPoolId(), volume,
													volume, 1, 0, (float) 1.0);
									// }
									// });
								}
							} else {
								createTrackQueue();
								state_playing = false;
								mainHandler.post(new Runnable() {
									public void run() {
										playButton.setImageResource(R.drawable.play_button_normal);
									}
								});
							}

						}
						handler.postDelayed(this, 1);
						// handler.post(this);
					}

				});
				Looper.loop();
			}
		}, "PlaybackThread");
		playbackThread1.start();
		addButtons();

	}

	public void addButtons() {
		playButton = (ImageButton) findViewById(R.id.play_button_track);
		playButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				state_playing = true;
				playButton.setImageResource(R.drawable.play_button_pressed);
				timeAtStart = SystemClock.elapsedRealtime();
				timeSinceStart = 0;
			}
		});
		stopButton = (ImageButton) findViewById(R.id.stop_button_track);
		stopButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				state_playing = false;
				timeAtStart = SystemClock.elapsedRealtime();
				timeSinceStart = 0;
				playButton.setImageResource(R.drawable.play_button_normal);
			}
		});
		pattern1Button = (Button) findViewById(R.id.pattern1Button);
		pattern1Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent().setClass(context,
						PatternActivity.class);
				String message = "first";
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("msgFromParent", message);
				startActivity(intent);

			}
		});

		pattern2Button = (Button) findViewById(R.id.pattern2Button);
		pattern2Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent().setClass(context,
						PatternActivity.class);
				String message = "second";
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("msgFromParent", message);
				startActivity(intent);

			}
		});

		pattern3Button = (Button) findViewById(R.id.pattern3Button);
		pattern3Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent().setClass(context,
						PatternActivity.class);
				String message = "third";
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("msgFromParent", message);
				startActivity(intent);

			}
		});

		pattern4Button = (Button) findViewById(R.id.pattern4Button);
		pattern4Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent().setClass(context,
						PatternActivity.class);
				String message = "fourth";
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("msgFromParent", message);
				startActivity(intent);

			}
		});
		addPattern1Button = (ImageButton) findViewById(R.id.addPattern1Button);

		addPattern1Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = 1;
				/*
				 * LinearLayout p1TrackLayout = (LinearLayout)
				 * findViewById(R.id.pattern1TrackRow); Button p1B = new
				 * Button(context); LinearLayout.LayoutParams lp = new
				 * LinearLayout.LayoutParams( 300, LayoutParams.WRAP_CONTENT);
				 * lp.gravity = 17; p1B.setLayoutParams(lp);
				 * p1B.setBackgroundResource(R.drawable.rounded_button_red);
				 * p1TrackLayout.addView(p1B);
				 */

				final Dialog addPattern1Dialog = new Dialog(context);
				addPattern1Dialog.setContentView(R.layout.add_pattern_dialog);
				addPattern1Dialog.setTitle("Add Pattern 1 at which bar?");
				final EditText pickerEdit = (EditText) addPattern1Dialog
						.findViewById(R.id.add_pattern_edit);
				pickerEdit.setText(String.valueOf(num),
						TextView.BufferType.EDITABLE);
				Button pickerDown = (Button) addPattern1Dialog
						.findViewById(R.id.add_pattern_down);
				Button pickerUp = (Button) addPattern1Dialog
						.findViewById(R.id.add_pattern_up);
				Button okButton = (Button) addPattern1Dialog
						.findViewById(R.id.add_pattern_ok);
				Button cancelButton = (Button) addPattern1Dialog
						.findViewById(R.id.add_pattern_cancel);
				pickerUp.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						num += 1;
						pickerEdit.setText(String.valueOf(num),
								TextView.BufferType.EDITABLE);
					}
				});
				pickerDown.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (num > 1) {
							num = num - 1;
							pickerEdit.setText(String.valueOf(num),
									TextView.BufferType.EDITABLE);
						}
					}
				});
				okButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (isValidPosition(Global.pattern1Bars,
								Global.pattern1SegmentPositions, num)) {
							Global.pattern1SegmentPositions.add(num);
							Collections.sort(Global.pattern1SegmentPositions);
							LinearLayout p1TrackLayout = (LinearLayout) findViewById(R.id.pattern1TrackRow);
							p1TrackLayout.removeAllViews();
							int lastP = 1;
							for (int p : Global.pattern1SegmentPositions) {
								if (p - (lastP) > 0) {
									View buffer = new View(context);
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
											300 * (p - (lastP)),
											LayoutParams.WRAP_CONTENT);
									buffer.setLayoutParams(lp);
									p1TrackLayout.addView(buffer);
								}
								Button p1B = new Button(context);
								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										Global.pattern1Bars * 300,
										LayoutParams.WRAP_CONTENT);
								lp.gravity = 17;
								p1B.setLayoutParams(lp);
								p1B.setBackgroundResource(R.drawable.rounded_button_yellow);
								p1TrackLayout.addView(p1B);
								lastP = p + Global.pattern1Bars;
								createTrackQueue();
							}
						} else
							System.out.println("INVALID");
						addPattern1Dialog.dismiss();
					}
				});
				cancelButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						LinearLayout p1TrackLayout = (LinearLayout) findViewById(R.id.pattern1TrackRow);
						p1TrackLayout.removeAllViews();
						addPattern1Dialog.dismiss();
					}
				});
				addPattern1Dialog.show();
			}
		});
		addPattern2Button = (ImageButton) findViewById(R.id.addPattern2Button);

		addPattern2Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = 1;
				/*
				 * LinearLayout p1TrackLayout = (LinearLayout)
				 * findViewById(R.id.pattern1TrackRow); Button p1B = new
				 * Button(context); LinearLayout.LayoutParams lp = new
				 * LinearLayout.LayoutParams( 300, LayoutParams.WRAP_CONTENT);
				 * lp.gravity = 17; p1B.setLayoutParams(lp);
				 * p1B.setBackgroundResource(R.drawable.rounded_button_red);
				 * p1TrackLayout.addView(p1B);
				 */

				final Dialog addPattern2Dialog = new Dialog(context);
				addPattern2Dialog.setContentView(R.layout.add_pattern_dialog);
				addPattern2Dialog.setTitle("Add Pattern 2 at which bar?");
				final EditText pickerEdit = (EditText) addPattern2Dialog
						.findViewById(R.id.add_pattern_edit);
				pickerEdit.setText(String.valueOf(num),
						TextView.BufferType.EDITABLE);
				Button pickerDown = (Button) addPattern2Dialog
						.findViewById(R.id.add_pattern_down);
				Button pickerUp = (Button) addPattern2Dialog
						.findViewById(R.id.add_pattern_up);
				Button okButton = (Button) addPattern2Dialog
						.findViewById(R.id.add_pattern_ok);
				Button cancelButton = (Button) addPattern2Dialog
						.findViewById(R.id.add_pattern_cancel);
				pickerUp.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						num += 1;
						pickerEdit.setText(String.valueOf(num),
								TextView.BufferType.EDITABLE);
					}
				});
				pickerDown.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (num > 1) {
							num = num - 1;
							pickerEdit.setText(String.valueOf(num),
									TextView.BufferType.EDITABLE);
						}
					}
				});
				okButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (isValidPosition(Global.pattern2Bars,
								Global.pattern2SegmentPositions, num)) {
							Global.pattern2SegmentPositions.add(num);
							Collections.sort(Global.pattern2SegmentPositions);
							LinearLayout p2TrackLayout = (LinearLayout) findViewById(R.id.pattern2TrackRow);
							p2TrackLayout.removeAllViews();
							int lastP = 1;
							for (int p : Global.pattern2SegmentPositions) {
								if (p - (lastP) > 0) {
									View buffer = new View(context);
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
											300 * (p - (lastP)),
											LayoutParams.WRAP_CONTENT);
									buffer.setLayoutParams(lp);
									p2TrackLayout.addView(buffer);
								}
								Button p2B = new Button(context);
								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										Global.pattern2Bars * 300,
										LayoutParams.WRAP_CONTENT);
								lp.gravity = 17;
								p2B.setLayoutParams(lp);
								p2B.setBackgroundResource(R.drawable.rounded_button_green);
								p2TrackLayout.addView(p2B);
								lastP = p + Global.pattern2Bars;
								createTrackQueue();
							}
						} else
							System.out.println("INVALID");
						addPattern2Dialog.dismiss();
					}
				});
				cancelButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						addPattern2Dialog.dismiss();
					}
				});
				addPattern2Dialog.show();
			}
		});
		addPattern3Button = (ImageButton) findViewById(R.id.addPattern3Button);

		addPattern3Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = 1;
				/*
				 * LinearLayout p1TrackLayout = (LinearLayout)
				 * findViewById(R.id.pattern1TrackRow); Button p1B = new
				 * Button(context); LinearLayout.LayoutParams lp = new
				 * LinearLayout.LayoutParams( 300, LayoutParams.WRAP_CONTENT);
				 * lp.gravity = 17; p1B.setLayoutParams(lp);
				 * p1B.setBackgroundResource(R.drawable.rounded_button_red);
				 * p1TrackLayout.addView(p1B);
				 */

				final Dialog addPattern3Dialog = new Dialog(context);
				addPattern3Dialog.setContentView(R.layout.add_pattern_dialog);
				addPattern3Dialog.setTitle("Add Pattern 3 at which bar?");
				final EditText pickerEdit = (EditText) addPattern3Dialog
						.findViewById(R.id.add_pattern_edit);
				pickerEdit.setText(String.valueOf(num),
						TextView.BufferType.EDITABLE);
				Button pickerDown = (Button) addPattern3Dialog
						.findViewById(R.id.add_pattern_down);
				Button pickerUp = (Button) addPattern3Dialog
						.findViewById(R.id.add_pattern_up);
				Button okButton = (Button) addPattern3Dialog
						.findViewById(R.id.add_pattern_ok);
				Button cancelButton = (Button) addPattern3Dialog
						.findViewById(R.id.add_pattern_cancel);
				pickerUp.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						num += 1;
						pickerEdit.setText(String.valueOf(num),
								TextView.BufferType.EDITABLE);
					}
				});
				pickerDown.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (num > 1) {
							num = num - 1;
							pickerEdit.setText(String.valueOf(num),
									TextView.BufferType.EDITABLE);
						}
					}
				});
				okButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (isValidPosition(Global.pattern3Bars,
								Global.pattern3SegmentPositions, num)) {
							Global.pattern3SegmentPositions.add(num);
							Collections.sort(Global.pattern3SegmentPositions);
							LinearLayout p3TrackLayout = (LinearLayout) findViewById(R.id.pattern3TrackRow);
							p3TrackLayout.removeAllViews();
							int lastP = 1;
							Global.buttonPositions3.clear();
							for (int p : Global.pattern3SegmentPositions) {
								if (p - (lastP) > 0) {
									View buffer = new View(context);
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
											300 * (p - (lastP)),
											LayoutParams.WRAP_CONTENT);
									buffer.setLayoutParams(lp);
									p3TrackLayout.addView(buffer);
								}
								Button p3B = new Button(context);
								Global.buttonPositions3.put(p3B, p);
								p3B.setOnLongClickListener(new View.OnLongClickListener(){

									@Override
									public boolean onLongClick(View v) {
										// TODO Auto-generated method stub
										LinearLayout p3TrackLayout = (LinearLayout) findViewById(R.id.pattern3TrackRow);
										//Global.pattern3SegmentPositions.remove(0);
										Button btn = (Button)v;
										Integer position = Global.buttonPositions3.get(v);
										Global.buttonPositions3.remove(v);
										boolean result = Global.pattern3SegmentPositions.remove(position);
										/*Collections.sort(Global.pattern3SegmentPositions);
										p3TrackLayout.removeAllViews();
										int lastP = 1;
										for (int p : Global.pattern3SegmentPositions) {
											if (p - (lastP) > 0) {
												View buffer = new View(context);
												LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
														300 * (p - (lastP)),
														LayoutParams.WRAP_CONTENT);
												buffer.setLayoutParams(lp);
												p3TrackLayout.addView(buffer);
											}
											//Button btn = 
										}*/
										//Toast.makeText(this, new Boolean(result).toString(), Toast.LENGTH_SHORT).show();
										//int lastP = 1;
										/*p3TrackLayout.removeAllViews();
										for (int p : Global.pattern3SegmentPositions) {
											if (p - (lastP) > 0) {
												View buffer = new View(context);
												LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
														300 * (p - (lastP)),
														LayoutParams.WRAP_CONTENT);
												buffer.setLayoutParams(lp);
												p3TrackLayout.addView(buffer);
											}
										}*/
										btn.setVisibility(View.INVISIBLE);
										createTrackQueue();
										//remove from track queue
										//p3TrackLayout.removeView(v);
										
										return true;
									}});
								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										Global.pattern3Bars * 300,
										LayoutParams.WRAP_CONTENT);
								lp.gravity = 17;
								p3B.setLayoutParams(lp);
								p3B.setBackgroundResource(R.drawable.rounded_button_blue);
								p3TrackLayout.addView(p3B);
								lastP = p + Global.pattern3Bars;
								createTrackQueue();
							}
						} else
							System.out.println("INVALID");
						addPattern3Dialog.dismiss();
					}
				});
				cancelButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						addPattern3Dialog.dismiss();
					}
				});
				addPattern3Dialog.show();
			}
		});
		addPattern4Button = (ImageButton) findViewById(R.id.addPattern4Button);

		addPattern4Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = 1;
				/*
				 * LinearLayout p1TrackLayout = (LinearLayout)
				 * findViewById(R.id.pattern1TrackRow); Button p1B = new
				 * Button(context); LinearLayout.LayoutParams lp = new
				 * LinearLayout.LayoutParams( 300, LayoutParams.WRAP_CONTENT);
				 * lp.gravity = 17; p1B.setLayoutParams(lp);
				 * p1B.setBackgroundResource(R.drawable.rounded_button_red);
				 * p1TrackLayout.addView(p1B);
				 */

				final Dialog addPattern4Dialog = new Dialog(context);
				addPattern4Dialog.setContentView(R.layout.add_pattern_dialog);
				addPattern4Dialog.setTitle("Add Pattern 4 at which bar?");
				final EditText pickerEdit = (EditText) addPattern4Dialog
						.findViewById(R.id.add_pattern_edit);
				pickerEdit.setText(String.valueOf(num),
						TextView.BufferType.EDITABLE);
				Button pickerDown = (Button) addPattern4Dialog
						.findViewById(R.id.add_pattern_down);
				Button pickerUp = (Button) addPattern4Dialog
						.findViewById(R.id.add_pattern_up);
				Button okButton = (Button) addPattern4Dialog
						.findViewById(R.id.add_pattern_ok);
				Button cancelButton = (Button) addPattern4Dialog
						.findViewById(R.id.add_pattern_cancel);
				pickerUp.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						num += 1;
						pickerEdit.setText(String.valueOf(num),
								TextView.BufferType.EDITABLE);
					}
				});
				pickerDown.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (num > 1) {
							num = num - 1;
							pickerEdit.setText(String.valueOf(num),
									TextView.BufferType.EDITABLE);
						}
					}
				});
				okButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (isValidPosition(Global.pattern4Bars,
								Global.pattern4SegmentPositions, num)) {
							Global.pattern4SegmentPositions.add(num);
							Collections.sort(Global.pattern4SegmentPositions);
							LinearLayout p4TrackLayout = (LinearLayout) findViewById(R.id.pattern4TrackRow);
							p4TrackLayout.removeAllViews();
							Global.buttonPositions4.clear();
							int lastP = 1;
							for (int p : Global.pattern4SegmentPositions) {
								if (p - (lastP) > 0) {
									View buffer = new View(context);
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
											300 * (p - (lastP)),
											LayoutParams.WRAP_CONTENT);
									buffer.setLayoutParams(lp);
									p4TrackLayout.addView(buffer);
								}
								Button p1B = new Button(context);
								Global.buttonPositions4.put(p1B, p);
								p1B.setOnLongClickListener(new View.OnLongClickListener(){

									@Override
									public boolean onLongClick(View v) {
										// TODO Auto-generated method stub
										LinearLayout p4TrackLayout = (LinearLayout) findViewById(R.id.pattern4TrackRow);
										//Global.pattern3SegmentPositions.remove(0);
										Button btn = (Button)v;
										Integer position = Global.buttonPositions4.get(v);
										Global.buttonPositions4.remove(v);
										boolean result = Global.pattern4SegmentPositions.remove(position);
										/*Collections.sort(Global.pattern3SegmentPositions);
										p3TrackLayout.removeAllViews();
										int lastP = 1;
										for (int p : Global.pattern3SegmentPositions) {
											if (p - (lastP) > 0) {
												View buffer = new View(context);
												LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
														300 * (p - (lastP)),
														LayoutParams.WRAP_CONTENT);
												buffer.setLayoutParams(lp);
												p3TrackLayout.addView(buffer);
											}
											//Button btn = 
										}*/
										//Toast.makeText(this, new Boolean(result).toString(), Toast.LENGTH_SHORT).show();
										//int lastP = 1;
										/*p3TrackLayout.removeAllViews();
										for (int p : Global.pattern3SegmentPositions) {
											if (p - (lastP) > 0) {
												View buffer = new View(context);
												LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
														300 * (p - (lastP)),
														LayoutParams.WRAP_CONTENT);
												buffer.setLayoutParams(lp);
												p3TrackLayout.addView(buffer);
											}
										}*/
										btn.setVisibility(View.INVISIBLE);
										createTrackQueue();
										//remove from track queue
										//p3TrackLayout.removeView(v);
										
										return true;
									}});

								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										Global.pattern4Bars * 300,
										LayoutParams.WRAP_CONTENT);
								lp.gravity = 17;
								p1B.setLayoutParams(lp);
								p1B.setBackgroundResource(R.drawable.rounded_button_red);
								p4TrackLayout.addView(p1B);
								lastP = p + Global.pattern4Bars;
								createTrackQueue();
							}
						} else
							System.out.println("INVALID");
						addPattern4Dialog.dismiss();
					}
				});
				cancelButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						addPattern4Dialog.dismiss();
					}
				});
				addPattern4Dialog.show();
			}
		});
	}

	public boolean isValidPosition(int p,
			ArrayList<Integer> patternSegmentPositions, int startBar) {
		// int patSize = p.numBars;
		int patSize = p;
		for (int n : patternSegmentPositions) {
			if (startBar >= n && startBar < n + patSize)
				return false;
		}
		return true;
	}

	public void createTrackQueue() {
		Global.trackSoundQueue.clear();
		for (int n : Global.pattern1SegmentPositions) {
			if (n + Global.pattern1Bars > maxBars) {
				maxBars = n + +Global.pattern1Bars;
			}
			for (Sound s : Global.patternSoundQueues.get(0)) {
				s.setOffset(s.getOffset() + ((n - 1) * Global.bpm) / (240000));
				Global.trackSoundQueue.add(s);
			}
		}
		for (int n : Global.pattern2SegmentPositions) {
			if (n + Global.pattern2Bars > maxBars) {
				maxBars = n + +Global.pattern2Bars;
			}
			for (Sound s : Global.patternSoundQueues.get(1)) {
				s.setOffset(s.getOffset() + ((n - 1) * 24000)/Global.bpm);
				Global.trackSoundQueue.add(s);
			}
		}
		for (int n : Global.pattern3SegmentPositions) {
			if (n + Global.pattern3Bars > maxBars) {
				maxBars = n + +Global.pattern3Bars;
			}
			for (Sound s : Global.patternSoundQueues.get(2)) {
				s.setOffset(s.getOffset() + ((n - 1) * Global.bpm) / (240000));
				Global.trackSoundQueue.add(s);
			}
		}
		for (int n : Global.pattern4SegmentPositions) {
			if (n + Global.pattern4Bars > maxBars) {
				maxBars = n + +Global.pattern4Bars;
			}
			for (Sound s : Global.patternSoundQueues.get(3)) {
				s.setOffset(s.getOffset() + ((n - 1) * Global.bpm) / (240000));
				Global.trackSoundQueue.add(s);
			}
		}
		mainHandler.post(new Runnable() {
			public void run() {
				LinearLayout barNumbersLayout = (LinearLayout) findViewById(R.id.barNumbersLayout);
				barNumbersLayout.removeAllViews();
				for(int n= 1; n < maxBars; n ++) {
					TextView view = new TextView(context);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							300, LayoutParams.FILL_PARENT);
					view.setLayoutParams(lp);
					view.setText(Integer.toString(n));
					barNumbersLayout.addView(view);
				}		
				
			}
		});


	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setTitle("Really Exit?")
				.setMessage("Are you sure you want to exit?")
				.setNegativeButton(android.R.string.no, null)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								
								
								Global.trackSoundQueue.clear();
								for (int i=0;i<4;i++)
								{
									if (Global.arrSoundPool.get(i) != null)
									{
										Global.patternSoundQueues.get(i).clear();	
										//Global.arrSoundPool.get(i).release();
										
									}
								}
								TrackActivity.super.onDestroy();
								finish();
								TrackActivity.super.onBackPressed();
							}
						}).create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
