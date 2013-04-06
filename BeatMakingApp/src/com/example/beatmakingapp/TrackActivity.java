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
							Button p1B = new Button(context);
							// TODO: change 300 value to 300 * pattern.numBars
							LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
									300, LayoutParams.WRAP_CONTENT);
							lp.gravity = 17;
							p1B.setLayoutParams(lp);
							p1B.setBackgroundResource(R.drawable.rounded_button_red);
							p1TrackLayout.addView(p1B);
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
}
