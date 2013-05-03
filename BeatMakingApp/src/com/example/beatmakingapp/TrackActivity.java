package com.example.beatmakingapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TrackActivity extends Activity {

	private Button pattern1Button, pattern2Button, pattern3Button,
			pattern4Button;
	private ImageButton addPattern1Button, addPattern2Button,
			addPattern3Button, addPattern4Button;
	private Context context = this;
	private ProgressBar progBar;

	private int maxBars = 0;
	private int num = 1; 

	boolean state_playing = false;
	long timeAtStart = 0;
	long timeSinceStart = 0;
	private static Thread playbackThread1;
	private static Looper l1;
	private ImageButton playButton;
	private Button exportButton;
	private static Handler mainHandler = new Handler();
	private double timeOfLastBeat;

	// Called when activity is created
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.track_layout2);
		progBar = (ProgressBar) findViewById(R.id.progBar);
		progBar.setProgress(0);
		AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
		final float volume = (float) am
				.getStreamVolume(AudioManager.STREAM_MUSIC);

		//used to play the sounds in track queue
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

							if (Global.trackSoundQueue.size() > 0) {
								while (Global.trackSoundQueue.size() > 0
										&& Global.trackSoundQueue.peek()
												.getOffset() <= (timeSinceStart
												* (double) (Global.bpm) / 60000 + 1)) {
									final Sound s = Global.trackSoundQueue
											.remove();

									System.out
											.println((timeSinceStart
													* (double) (Global.bpm)
													/ 60000 + 1));
									System.out.println(s.getOffset());
									Global.soundPool.play(s.getSoundPoolId(),
											volume, volume, 1, 0, (float) 1.0);

								}
							} else {
								createTrackQueue();
								state_playing = false;
								mainHandler.post(new Runnable() {
									public void run() {

										enableAllButtons();
										playButton
												.setImageResource(R.drawable.play_button_pressed);
										playButton
												.setBackgroundResource(android.R.drawable.btn_default);
									}
								});
							}

							mainHandler.post(new Runnable() {
								public void run() {
									progBar.setProgress((int) ((4 * timeSinceStart * 25 * Global.bpm) / ((timeOfLastBeat - 1) * 60000)));
								}
							});

						}
						handler.postDelayed(this, 1);
					}

				});
				Looper.loop();
			}
		}, "PlaybackThread");
		playbackThread1.start();
		addButtons();

	}

	
	//describe the functionality of play button and export button
	public void addButtons() {
		
		//play button functionality 
		playButton = (ImageButton) findViewById(R.id.play_button_track);
		playButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (state_playing == false) {
					createTrackQueue();
					Iterator it = Global.trackSoundQueue.iterator();
					while (it.hasNext()) {
						Sound s = (Sound) it.next();
						if (timeOfLastBeat < s.getOffset())
							timeOfLastBeat = s.getOffset();
					}
					disableAllButtons();

					

					playButton.setImageResource(R.drawable.stop_button_normal);
					playButton
							.setBackgroundResource(R.drawable.stop_border_play);
					timeAtStart = SystemClock.elapsedRealtime();
					timeSinceStart = 0;
					state_playing = true;
				} else {
					

					enableAllButtons();
					Global.trackSoundQueue.clear();
					createTrackQueue();
					timeAtStart = SystemClock.elapsedRealtime();
					timeSinceStart = 0;
					state_playing = false;
					playButton.setImageResource(R.drawable.play_button_pressed);
					playButton
							.setBackgroundResource(android.R.drawable.btn_default);
					
				}
			}
		});
		
		//export button functionality 
		exportButton = (Button) findViewById(R.id.export_button_track);
		exportButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);

				LayoutInflater inflater = getLayoutInflater();
				View dialoglayout = inflater.inflate(R.layout.custom_dialog,
						(ViewGroup) getCurrentFocus());
				final EditText text = (EditText) dialoglayout
						.findViewById(R.id.file);
				builder.setView(dialoglayout);
				builder.setTitle("Save File");
				builder.setMessage("Exporting to SD card")
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										boolean mExternalStorageAvailable = false;
										boolean mExternalStorageWriteable = false;
										WavIO io = new WavIO();
										File sdcard = Environment
												.getExternalStorageDirectory();
										String state = Environment
												.getExternalStorageState();


										if (Environment.MEDIA_MOUNTED
												.equals(state)) {
											// We can read and write the media
											mExternalStorageAvailable = mExternalStorageWriteable = true;
										} else if (Environment.MEDIA_MOUNTED_READ_ONLY
												.equals(state)) {
											// We can only read the media
											mExternalStorageAvailable = true;
											mExternalStorageWriteable = false;
										} else {

											mExternalStorageAvailable = mExternalStorageWriteable = false;
										}

										if (mExternalStorageAvailable) {
											if (mExternalStorageWriteable) {
												String exportFileName = text
														.getText().toString()
														+ ".wav";
												Toast.makeText(
														context,
														"Exporting To : "
																+ sdcard.getPath()
																+ "/Music/Beats/exported/"
																+ exportFileName,
														Toast.LENGTH_LONG)
														.show();
												synchronized (Global.patternSoundQueues) {

													boolean result = io
															.exportSound(
																	exportFileName,
																	Global.trackSoundQueue,
																	context);

													if (result) {
														Toast.makeText(
																context,
																"Done!!",
																Toast.LENGTH_LONG)
																.show();

													} else {

														Toast.makeText(
																context,
																"Not Done!!",
																Toast.LENGTH_LONG)
																.show();
													}
												}
											} else {
												Toast.makeText(
														context,
														"ERROR : External Storage is available but not writable. Please check Permissions.",
														Toast.LENGTH_LONG)
														.show();

											}

										} else {
											Toast.makeText(
													context,
													"ERROR : External Storage is not available.",
													Toast.LENGTH_LONG).show();

										}

									}
								})
						.setNegativeButton(R.string.Cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// User cancelled the dialog
									}
								});
				// Create the AlertDialog object and return it
				builder.create().show();
			}
		});
		
		//buttons to navigate the pattern
		//eg pattern1Button navigates to Pattern-1
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
		
		//add instances of Pattern-1
		addPattern1Button = (ImageButton) findViewById(R.id.addPattern1Button);

		addPattern1Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = 1;

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
						num = Integer.valueOf(pickerEdit.getText().toString());

						if (num > 50) {
							addPattern1Dialog.dismiss();
							new AlertDialog.Builder(context)
									.setTitle(
											"Limit Exceeded!\nValue cannot be greater than 50!")
									.setPositiveButton(android.R.string.yes,
											null).create().show();
						} else {
							if (isValidPosition(Global.pattern1Bars,
									Global.pattern1SegmentPositions, num)) {
								Global.pattern1SegmentPositions.add(num);
								Collections
										.sort(Global.pattern1SegmentPositions);
								LinearLayout p1TrackLayout = (LinearLayout) findViewById(R.id.pattern1TrackRow);
								p1TrackLayout.removeAllViews();
								Global.buttonPositions1.clear();
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
									Global.buttonPositions1.put(p1B, p);
									p1B.setOnLongClickListener(new View.OnLongClickListener() {

										@Override
										public boolean onLongClick(View v) {
											// TODO Auto-generated method stub

											final Button btn = (Button) v;
											AlertDialog.Builder builder = new AlertDialog.Builder(
													context);
											builder.setTitle(R.string.removePattern);
											builder.setMessage(
													R.string.confirmation)
													.setPositiveButton(
															R.string.ok,
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int id) {

																	Integer position = Global.buttonPositions1
																			.get(btn);
																	Global.buttonPositions1
																			.remove(btn);
																	boolean result = Global.pattern1SegmentPositions
																			.remove(position);

																	btn.setVisibility(View.INVISIBLE);
																	createTrackQueue();

																}
															})
													.setNegativeButton(
															R.string.Cancel,
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int id) {
																	// User
																	// cancelled
																	// the
																	// dialog
																}
															});
											// Create the AlertDialog object and
											// return it
											builder.create().show();

											return true;
										}
									});

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
							} else {
								new AlertDialog.Builder(context)
										.setTitle(
												"Pattern already exists at this bar!")
										.setPositiveButton(
												android.R.string.yes, null)
										.create().show();
							}

							addPattern1Dialog.dismiss();
						}
					}
				});

				cancelButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						addPattern1Dialog.dismiss();
					}
				});
				addPattern1Dialog.show();
			}
		});

		//add instances of Pattern-2
		addPattern2Button = (ImageButton) findViewById(R.id.addPattern2Button);

		addPattern2Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = 1;

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
						num = Integer.valueOf(pickerEdit.getText().toString());
						if (isValidPosition(Global.pattern2Bars,
								Global.pattern2SegmentPositions, num)) {
							Global.pattern2SegmentPositions.add(num);
							Collections.sort(Global.pattern2SegmentPositions);
							LinearLayout p2TrackLayout = (LinearLayout) findViewById(R.id.pattern2TrackRow);
							p2TrackLayout.removeAllViews();

							Global.buttonPositions2.clear();
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
								Global.buttonPositions2.put(p2B, p);
								p2B.setOnLongClickListener(new View.OnLongClickListener() {

									@Override
									public boolean onLongClick(View v) {
										// TODO Auto-generated method stub

										final Button btn = (Button) v;
										AlertDialog.Builder builder = new AlertDialog.Builder(
												context);
										builder.setTitle(R.string.removePattern);
										builder.setMessage(
												R.string.confirmation)
												.setPositiveButton(
														R.string.ok,
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int id) {

																Integer position = Global.buttonPositions2
																		.get(btn);
																Global.buttonPositions2
																		.remove(btn);
																boolean result = Global.pattern2SegmentPositions
																		.remove(position);

																btn.setVisibility(View.INVISIBLE);
																createTrackQueue();

															}
														})
												.setNegativeButton(
														R.string.Cancel,
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int id) {
																// User
																// cancelled the
																// dialog
															}
														});
										// Create the AlertDialog object and
										// return it
										builder.create().show();

										return true;
									}
								});

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
						} else {
							new AlertDialog.Builder(context)
									.setTitle(
											"Pattern already exists at this bar!")
									.setPositiveButton(android.R.string.yes,
											null).create().show();
						}
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

		//add instances of Pattern-3
		addPattern3Button = (ImageButton) findViewById(R.id.addPattern3Button);

		addPattern3Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = 1;

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
						num = Integer.valueOf(pickerEdit.getText().toString());
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
								p3B.setOnLongClickListener(new View.OnLongClickListener() {

									@Override
									public boolean onLongClick(View v) {
										// TODO Auto-generated method stub
										final Button btn = (Button) v;
										AlertDialog.Builder builder = new AlertDialog.Builder(
												context);
										builder.setTitle(R.string.removePattern);
										builder.setMessage(
												R.string.confirmation)
												.setPositiveButton(
														R.string.ok,
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int id) {

																Integer position = Global.buttonPositions3
																		.get(btn);
																Global.buttonPositions3
																		.remove(btn);
																boolean result = Global.pattern3SegmentPositions
																		.remove(position);

																btn.setVisibility(View.INVISIBLE);
																createTrackQueue();

															}
														})
												.setNegativeButton(
														R.string.Cancel,
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int id) {
																// User
																// cancelled the
																// dialog
															}
														});
										// Create the AlertDialog object and
										// return it
										builder.create().show();

										return true;
									}
								});
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
						} else {
							new AlertDialog.Builder(context)
									.setTitle(
											"Pattern already exists at this bar!")
									.setPositiveButton(android.R.string.yes,
											null).create().show();
						}
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

		//add instances of Pattern-4
		addPattern4Button = (ImageButton) findViewById(R.id.addPattern4Button);

		addPattern4Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				num = 1;

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
						num = Integer.valueOf(pickerEdit.getText().toString());
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
								Button p4B = new Button(context);
								Global.buttonPositions4.put(p4B, p);
								p4B.setOnLongClickListener(new View.OnLongClickListener() {

									@Override
									public boolean onLongClick(View v) {
										// TODO Auto-generated method stub

										final Button btn = (Button) v;
										AlertDialog.Builder builder = new AlertDialog.Builder(
												context);
										builder.setTitle(R.string.removePattern);
										builder.setMessage(
												R.string.confirmation)
												.setPositiveButton(
														R.string.ok,
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int id) {

																Integer position = Global.buttonPositions4
																		.get(btn);
																Global.buttonPositions4
																		.remove(btn);
																boolean result = Global.pattern4SegmentPositions
																		.remove(position);

																btn.setVisibility(View.INVISIBLE);
																createTrackQueue();

															}
														})
												.setNegativeButton(
														R.string.Cancel,
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int id) {
																// User
																// cancelled the
																// dialog
															}
														});
										// Create the AlertDialog object and
										// return it
										builder.create().show();

										return true;
									}
								});

								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										Global.pattern4Bars * 300,
										LayoutParams.WRAP_CONTENT);
								lp.gravity = 17;
								p4B.setLayoutParams(lp);
								p4B.setBackgroundResource(R.drawable.rounded_button_red);
								p4TrackLayout.addView(p4B);
								lastP = p + Global.pattern4Bars;
								createTrackQueue();
							}
						} else {
							new AlertDialog.Builder(context)
									.setTitle(
											"Pattern already exists at this bar!")
									.setPositiveButton(android.R.string.yes,
											null).create().show();
						}

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

	//disables the buttons while playback
	protected void disableAllButtons() {
		// TODO Auto-generated method stub
		ImageButton btn1 = (ImageButton) findViewById(R.id.addPattern1Button);
		ImageButton btn2 = (ImageButton) findViewById(R.id.addPattern2Button);
		ImageButton btn3 = (ImageButton) findViewById(R.id.addPattern3Button);
		ImageButton btn4 = (ImageButton) findViewById(R.id.addPattern4Button);

		Button patBtn1 = (Button) findViewById(R.id.pattern1Button);
		Button patBtn2 = (Button) findViewById(R.id.pattern2Button);
		Button patBtn3 = (Button) findViewById(R.id.pattern3Button);
		Button patBtn4 = (Button) findViewById(R.id.pattern4Button);

		btn1.setEnabled(false);
		btn2.setEnabled(false);
		btn3.setEnabled(false);
		btn4.setEnabled(false);

		patBtn1.setEnabled(false);
		patBtn2.setEnabled(false);
		patBtn3.setEnabled(false);
		patBtn4.setEnabled(false);

	}

	//enables the buttons after playback
	protected void enableAllButtons() {
		// TODO Auto-generated method stub
		ImageButton btn1 = (ImageButton) findViewById(R.id.addPattern1Button);
		ImageButton btn2 = (ImageButton) findViewById(R.id.addPattern2Button);
		ImageButton btn3 = (ImageButton) findViewById(R.id.addPattern3Button);
		ImageButton btn4 = (ImageButton) findViewById(R.id.addPattern4Button);

		Button patBtn1 = (Button) findViewById(R.id.pattern1Button);
		Button patBtn2 = (Button) findViewById(R.id.pattern2Button);
		Button patBtn3 = (Button) findViewById(R.id.pattern3Button);
		Button patBtn4 = (Button) findViewById(R.id.pattern4Button);

		btn1.setEnabled(true);
		btn2.setEnabled(true);
		btn3.setEnabled(true);
		btn4.setEnabled(true);

		patBtn1.setEnabled(true);
		patBtn2.setEnabled(true);
		patBtn3.setEnabled(true);
		patBtn4.setEnabled(true);

	}

	//checks if there is already instance of patterns added at that position
	public boolean isValidPosition(int p,
			ArrayList<Integer> patternSegmentPositions, int startBar) {

		int patSize = p;
		int endBar = startBar + patSize;
		for (int n : patternSegmentPositions) {

			if ((startBar >= n && startBar < n + patSize)
					|| (endBar <= n + patSize))
				return false;
		}
		return true;
	}

	//creates track queue from pattern instances (all 4) and forms a new queue which can be played by pressing play
	public void createTrackQueue() {
		Global.trackSoundQueue.clear();
		Global.trackSoundQueueMS.clear();
		for (int n : Global.pattern1SegmentPositions) {
			if (n + Global.pattern1Bars > maxBars) {
				maxBars = n + Global.pattern1Bars;
			}
			for (Sound s : Global.patternSoundQueues.get(0)) {
				double offset = s.getOffset() + (double) ((n - 1) * 4);
				Global.trackSoundQueue.add(new Sound(s.getSoundPoolId(), s
						.getButtonId_i(), s.getButtonId_j(), offset, s
						.getPatternId()));
			}
		}
		for (int n : Global.pattern2SegmentPositions) {
			if (n + Global.pattern2Bars > maxBars) {
				maxBars = n + +Global.pattern2Bars;
			}
			for (Sound s : Global.patternSoundQueues.get(1)) {
				double offset = s.getOffset() + (double) ((n - 1) * 4);
				Global.trackSoundQueue.add(new Sound(s.getSoundPoolId(), s
						.getButtonId_i(), s.getButtonId_j(), offset, s
						.getPatternId()));
			}
		}
		for (int n : Global.pattern3SegmentPositions) {
			if (n + Global.pattern3Bars > maxBars) {
				maxBars = n + +Global.pattern3Bars;
			}
			for (Sound s : Global.patternSoundQueues.get(2)) {
				double offset = s.getOffset() + (double) ((n - 1) * 4);
				Global.trackSoundQueue.add(new Sound(s.getSoundPoolId(), s
						.getButtonId_i(), s.getButtonId_j(), offset, s
						.getPatternId()));
			}
		}
		for (int n : Global.pattern4SegmentPositions) {
			if (n + Global.pattern4Bars > maxBars) {
				maxBars = n + +Global.pattern4Bars;
			}
			for (Sound s : Global.patternSoundQueues.get(3)) {
				double offset = s.getOffset() + (double) ((n - 1) * 4);
				Global.trackSoundQueue.add(new Sound(s.getSoundPoolId(), s
						.getButtonId_i(), s.getButtonId_j(), offset, s
						.getPatternId()));
			}
		}
		for (Sound s : Global.trackSoundQueue) {
			double offset = s.getOffset() * 60000 / ((double) (Global.bpm)) - 1;
			Global.trackSoundQueueMS.add(new Sound(s.getSoundPoolId(), s
					.getButtonId_i(), s.getButtonId_j(), offset, s
					.getPatternId()));
		}
		mainHandler.post(new Runnable() {
			public void run() {
				LinearLayout barNumbersLayout = (LinearLayout) findViewById(R.id.barNumbersLayout);
				barNumbersLayout.removeAllViews();
				for (int n = 1; n < maxBars; n++) {
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

	//asks the user if it wants to exit from the app
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

								Global.buttonPositions1.clear();
								Global.buttonPositions2.clear();
								Global.buttonPositions3.clear();
								Global.buttonPositions4.clear();
								Global.pattern1SegmentPositions.clear();
								Global.pattern2SegmentPositions.clear();
								Global.pattern3SegmentPositions.clear();
								Global.pattern4SegmentPositions.clear();


								for (int i = 0; i < 4; i++) {
									if (Global.soundPool != null) {
										Global.patternSoundQueues.get(i)
												.clear();
		

									}
								}

								TrackActivity.super.onBackPressed();
								TrackActivity.super.onDestroy();
								finish();
							}
						}).create().show();
	}
}
