package com.example.beatmakingapp;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrackActivity extends Activity {
	
	private Button pattern1Button, pattern2Button, pattern3Button,
			pattern4Button;
	private ImageButton addPattern1Button, addPattern2Button,
			addPattern3Button, addPattern4Button;
	private Context context = this;
	
	private ArrayList<Integer> pattern1SegmentPositions = new ArrayList<Integer>();
	private ArrayList<Integer> pattern2SegmentPositions = new ArrayList<Integer>();
	private ArrayList<Integer> pattern3SegmentPositions = new ArrayList<Integer>();
	private ArrayList<Integer> pattern4SegmentPositions = new ArrayList<Integer>();
	private int num = 1;		// reusable number to update new pattern segment positions

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.track_layout2);
		addButtons();

	}

	public void addButtons() {
		pattern1Button = (Button) findViewById(R.id.pattern1Button);
		pattern1Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {


				Intent intent = new Intent().setClass(context, PatternActivity.class); 
			    String message = "first";
			    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			    intent.putExtra("msgFromParent", message);
			    startActivity(intent);
			    
			}
		});
     
		pattern2Button = (Button) findViewById(R.id.pattern2Button);
		pattern2Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {


				Intent intent = new Intent().setClass(context, PatternActivity.class); 
			    String message = "second";
			    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			    intent.putExtra("msgFromParent", message);
			    startActivity(intent);
			    
			}
		});
		
		pattern3Button = (Button) findViewById(R.id.pattern3Button);
		pattern3Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				Intent intent = new Intent().setClass(context, PatternActivity.class); 
			    String message = "third";
			    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			    intent.putExtra("msgFromParent", message);
			    startActivity(intent);
			    
			}
		});
		
		pattern4Button = (Button) findViewById(R.id.pattern4Button);
		pattern4Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent().setClass(context, PatternActivity.class); 
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
				/*LinearLayout p1TrackLayout = (LinearLayout) findViewById(R.id.pattern1TrackRow);
				Button p1B = new Button(context);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						300, LayoutParams.WRAP_CONTENT);
				lp.gravity = 17;
				p1B.setLayoutParams(lp);
				p1B.setBackgroundResource(R.drawable.rounded_button_red);
				p1TrackLayout.addView(p1B);*/

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
				Button okButton = (Button) addPattern1Dialog.findViewById(R.id.add_pattern_ok);
				Button cancelButton = (Button) addPattern1Dialog.findViewById(R.id.add_pattern_cancel);
				pickerUp.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						num += 1;
						pickerEdit.setText(String.valueOf(num),
								TextView.BufferType.EDITABLE);
					}
				});
				pickerDown.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if(num > 1) {
							num = num-1;
							pickerEdit.setText(String.valueOf(num),
									TextView.BufferType.EDITABLE);
						}
					}
				});
				okButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if(isValidPosition(1, pattern1SegmentPositions, num)) {
							pattern1SegmentPositions.add(num);
							Collections.sort(pattern1SegmentPositions);
							LinearLayout p1TrackLayout = (LinearLayout) findViewById(R.id.pattern1TrackRow);
							p1TrackLayout.removeAllViews();
							// TODO: change 300 value to 300 + ...
							int lastP = 0;
							for(int p: pattern1SegmentPositions) {
								if(p-(lastP + 1) > 0) {
									View buffer = new View(context);
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300*(p-(lastP+1)), LayoutParams.WRAP_CONTENT);
									buffer.setLayoutParams(lp);
									p1TrackLayout.addView(buffer);
								}
								Button p1B = new Button(context);
								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										300, LayoutParams.WRAP_CONTENT);
								lp.gravity = 17;
								p1B.setLayoutParams(lp);
								p1B.setBackgroundResource(R.drawable.rounded_button_red);
								p1TrackLayout.addView(p1B);
								lastP = p;
							}
						}
						else
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
				/*LinearLayout p1TrackLayout = (LinearLayout) findViewById(R.id.pattern1TrackRow);
				Button p1B = new Button(context);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						300, LayoutParams.WRAP_CONTENT);
				lp.gravity = 17;
				p1B.setLayoutParams(lp);
				p1B.setBackgroundResource(R.drawable.rounded_button_red);
				p1TrackLayout.addView(p1B);*/

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
				Button okButton = (Button) addPattern2Dialog.findViewById(R.id.add_pattern_ok);
				Button cancelButton = (Button) addPattern2Dialog.findViewById(R.id.add_pattern_cancel);
				pickerUp.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						num += 1;
						pickerEdit.setText(String.valueOf(num),
								TextView.BufferType.EDITABLE);
					}
				});
				pickerDown.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if(num > 1) {
							num = num-1;
							pickerEdit.setText(String.valueOf(num),
									TextView.BufferType.EDITABLE);
						}
					}
				});
				okButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if(isValidPosition(1, pattern2SegmentPositions, num)) {
							pattern2SegmentPositions.add(num);
							Collections.sort(pattern2SegmentPositions);
							LinearLayout p2TrackLayout = (LinearLayout) findViewById(R.id.pattern2TrackRow);
							p2TrackLayout.removeAllViews();
							// TODO: change 300 value to 300 + ...
							int lastP = 0;
							for(int p: pattern2SegmentPositions) {
								if(p-(lastP + 1) > 0) {
									View buffer = new View(context);
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300*(p-(lastP+1)), LayoutParams.WRAP_CONTENT);
									buffer.setLayoutParams(lp);
									p2TrackLayout.addView(buffer);
								}
								Button p2B = new Button(context);
								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										300, LayoutParams.WRAP_CONTENT);
								lp.gravity = 17;
								p2B.setLayoutParams(lp);
								p2B.setBackgroundResource(R.drawable.rounded_button_blue);
								p2TrackLayout.addView(p2B);
								lastP = p;
							}
						}
						else
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
				/*LinearLayout p1TrackLayout = (LinearLayout) findViewById(R.id.pattern1TrackRow);
				Button p1B = new Button(context);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						300, LayoutParams.WRAP_CONTENT);
				lp.gravity = 17;
				p1B.setLayoutParams(lp);
				p1B.setBackgroundResource(R.drawable.rounded_button_red);
				p1TrackLayout.addView(p1B);*/

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
				Button okButton = (Button) addPattern3Dialog.findViewById(R.id.add_pattern_ok);
				Button cancelButton = (Button) addPattern3Dialog.findViewById(R.id.add_pattern_cancel);
				pickerUp.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						num += 1;
						pickerEdit.setText(String.valueOf(num),
								TextView.BufferType.EDITABLE);
					}
				});
				pickerDown.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if(num > 1) {
							num = num-1;
							pickerEdit.setText(String.valueOf(num),
									TextView.BufferType.EDITABLE);
						}
					}
				});
				okButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if(isValidPosition(1, pattern3SegmentPositions, num)) {
							pattern3SegmentPositions.add(num);
							Collections.sort(pattern3SegmentPositions);
							LinearLayout p3TrackLayout = (LinearLayout) findViewById(R.id.pattern3TrackRow);
							p3TrackLayout.removeAllViews();
							// TODO: change 300 value to 300 + ...
							int lastP = 0;
							for(int p: pattern3SegmentPositions) {
								if(p-(lastP + 1) > 0) {
									View buffer = new View(context);
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300*(p-(lastP+1)), LayoutParams.WRAP_CONTENT);
									buffer.setLayoutParams(lp);
									p3TrackLayout.addView(buffer);
								}
								Button p3B = new Button(context);
								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										300, LayoutParams.WRAP_CONTENT);
								lp.gravity = 17;
								p3B.setLayoutParams(lp);
								p3B.setBackgroundResource(R.drawable.rounded_button_yellow);
								p3TrackLayout.addView(p3B);
								lastP = p;
							}
						}
						else
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
				/*LinearLayout p1TrackLayout = (LinearLayout) findViewById(R.id.pattern1TrackRow);
				Button p1B = new Button(context);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						300, LayoutParams.WRAP_CONTENT);
				lp.gravity = 17;
				p1B.setLayoutParams(lp);
				p1B.setBackgroundResource(R.drawable.rounded_button_red);
				p1TrackLayout.addView(p1B);*/

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
				Button okButton = (Button) addPattern4Dialog.findViewById(R.id.add_pattern_ok);
				Button cancelButton = (Button) addPattern4Dialog.findViewById(R.id.add_pattern_cancel);
				pickerUp.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						num += 1;
						pickerEdit.setText(String.valueOf(num),
								TextView.BufferType.EDITABLE);
					}
				});
				pickerDown.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if(num > 1) {
							num = num-1;
							pickerEdit.setText(String.valueOf(num),
									TextView.BufferType.EDITABLE);
						}
					}
				});
				okButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if(isValidPosition(1, pattern4SegmentPositions, num)) {
							pattern4SegmentPositions.add(num);
							Collections.sort(pattern4SegmentPositions);
							LinearLayout p4TrackLayout = (LinearLayout) findViewById(R.id.pattern4TrackRow);
							p4TrackLayout.removeAllViews();
							// TODO: change 300 value to 300 + ...
							int lastP = 0;
							for(int p: pattern4SegmentPositions) {
								if(p-(lastP + 1) > 0) {
									View buffer = new View(context);
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300*(p-(lastP+1)), LayoutParams.WRAP_CONTENT);
									buffer.setLayoutParams(lp);
									p4TrackLayout.addView(buffer);
								}
								Button p1B = new Button(context);
								LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
										300, LayoutParams.WRAP_CONTENT);
								lp.gravity = 17;
								p1B.setLayoutParams(lp);
								p1B.setBackgroundResource(R.drawable.rounded_button_green);
								p4TrackLayout.addView(p1B);
								lastP = p;
							}
						}
						else
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
	
	// TODO: change p from int to pattern
	public boolean isValidPosition(int p, ArrayList<Integer> patternSegmentPositions, int startBar) {
		//int patSize = p.numBars;
		int patSize = p;
		for(int n: patternSegmentPositions) {
			if(startBar >= n && startBar < n + patSize)
				return false;
		}
		return true;
	}
	
	public void addSoundsToQueue(int patternNum, int startBar) {
		if(patternNum == 1) {
			
		}
	}
	
	public void createTrackQueue() {
		Global.trackSoundQueue.clear();
		for(int n: Global.pattern1SegmentPositions) {
			for(Sound s: Global.patternSoundQueues.get(0)) {
				s.setOffset(s.getOffset() + (n*Global.bpm)/(240000));
				Global.trackSoundQueue.add(s);
			}
		}
	}
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setTitle("Really Exit?")
	        .setMessage("Are you sure you want to exit?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
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
