package com.example.beatmakingapp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Button;

import com.example.beatmakingapp.R.raw;

public class Global {

	
	public static boolean initialized = false;
	public static SoundPool soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
	public static SoundPool metroPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	public static Comparator<Sound> comp = new DoubleComparator();
	public static ArrayList<PriorityQueue<Sound>> patternSoundQueues = new ArrayList<PriorityQueue<Sound>>();
	public static PriorityQueue<Sound> trackSoundQueue = new PriorityQueue<Sound>(10, Global.comp);
	public static PriorityQueue<Sound> trackSoundQueueMS = new PriorityQueue<Sound>(10, Global.comp);
	public static ArrayList<Integer> pattern1SegmentPositions = new ArrayList<Integer>();
	public static ArrayList<Integer> pattern2SegmentPositions = new ArrayList<Integer>();
	public static ArrayList<Integer> pattern3SegmentPositions = new ArrayList<Integer>();
	public static ArrayList<Integer> pattern4SegmentPositions = new ArrayList<Integer>();
	
	public static int pattern1Bars = 4;
	public static int pattern2Bars = 4;
	public static int pattern3Bars = 4;
	public static int pattern4Bars = 4;
	
	public static HashMap<Button, Integer> buttonPositions1 = new HashMap<Button,Integer>();
	public static HashMap<Button, Integer> buttonPositions2 = new HashMap<Button,Integer>();
	public static HashMap<Button, Integer> buttonPositions3 = new HashMap<Button,Integer>();
	public static HashMap<Button, Integer> buttonPositions4 = new HashMap<Button,Integer>();
	
	public static int bpm = 120;
	public static int[][] soundIds = new int[4][4];
	public static int metroId;
	public static String[][] filenames = new String[4][4];
	public static Context patternContext;
	public static boolean metronome;
	
	public static double p1SnapValue = 0.5;
	public static double p2SnapValue = 0.5;
	public static double p3SnapValue = 0.5;
	public static double p4SnapValue = 0.5;
	
	public static void initialize()
	{
		metronome = false;
		
		Global.patternSoundQueues.add(new PriorityQueue<Sound>(10, Global.comp));
		Global.patternSoundQueues.add(new PriorityQueue<Sound>(10, Global.comp));
		Global.patternSoundQueues.add(new PriorityQueue<Sound>(10, Global.comp));
		Global.patternSoundQueues.add(new PriorityQueue<Sound>(10, Global.comp));
		
		Global.metroId = Global.metroPool.load(Global.patternContext, raw.closedhat, 1);
		
		Global.soundIds[0][0] = Global.soundPool.load(Global.patternContext, raw.sabar_kick, 1);
		Global.soundIds[0][1] = Global.soundPool.load(Global.patternContext, raw.sabar_kick_2, 1);
		Global.soundIds[0][2] = Global.soundPool.load(Global.patternContext, raw.sabar_kick_cool, 1);
		Global.soundIds[0][3] = Global.soundPool.load(Global.patternContext, raw.sabar_snare, 1);
		Global.soundIds[1][0] = Global.soundPool.load(Global.patternContext, raw.sabar_snare_2, 1);
		Global.soundIds[1][1] = Global.soundPool.load(Global.patternContext, raw.sabar_snare_3, 1);
		Global.soundIds[1][2] = Global.soundPool.load(Global.patternContext, raw.sabar_snare_cool_2, 1);
		Global.soundIds[1][3] = Global.soundPool.load(Global.patternContext, raw.sabar_snare_flam, 1);
		Global.soundIds[2][0] = Global.soundPool.load(Global.patternContext, raw.sabar_snare_reverb, 1);
		Global.soundIds[2][1] = Global.soundPool.load(Global.patternContext, raw.sabar_snare_reverb_3, 1);
		Global.soundIds[2][2] = Global.soundPool.load(Global.patternContext, raw.sabar_snare_roll, 1);
		Global.soundIds[2][3] = Global.soundPool.load(Global.patternContext, raw.sabar_tom, 1);
		Global.soundIds[3][0] = Global.soundPool.load(Global.patternContext, raw.z_africanhat, 1);
		Global.soundIds[3][1] = Global.soundPool.load(Global.patternContext, raw.z_doughat, 1);
		Global.soundIds[3][2] = Global.soundPool.load(Global.patternContext, raw.z_gavgoodclosed, 1);
		Global.soundIds[3][3] = Global.soundPool.load(Global.patternContext, raw.z_gavgoodopen, 1);
		
		Global.filenames[0][0] = "sabar_kick.wav";
		Global.filenames[0][1] = "sabar_kick_2.wav";
		Global.filenames[0][2] = "sabar_kick_cool.wav";
		Global.filenames[0][3] = "sabar_snare.wav";
		Global.filenames[1][0] = "sabar_snare_2.wav";
		Global.filenames[1][1] = "sabar_snare_3.wav";
		Global.filenames[1][2] = "sabar_snare_cool_2.wav";
		Global.filenames[1][3] = "sabar_snare_flam.wav";
		Global.filenames[2][0] = "sabar_snare_reverb.wav";
		Global.filenames[2][1] = "sabar_snare_reverb_3.wav";
		Global.filenames[2][2] = "sabar_snare_roll.wav";
		Global.filenames[2][3] = "sabar_tom.wav";
		Global.filenames[3][0] = "z_africanhat.wav";
		Global.filenames[3][1] = "z_doughat.wav";
		Global.filenames[3][2] = "z_gavgoodclosed.wav";
		Global.filenames[3][3] = "z_gavgoodopen.wav";
		
		
	}
	
}
