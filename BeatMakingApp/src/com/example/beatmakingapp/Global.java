package com.example.beatmakingapp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Button;

import com.example.beatmakingapp.R.raw;

public class Global {

	
	public static boolean initialized = false;
	public static ArrayList<SoundPool> arrSoundPool = new ArrayList<SoundPool>();
	public static Comparator<Sound> comp = new LongComparator();
	public static ArrayList<PriorityQueue<Sound>> patternSoundQueues = new ArrayList<PriorityQueue<Sound>>();
	public static PriorityQueue<Sound> trackSoundQueue = new PriorityQueue<Sound>(10, Global.comp);
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
	public static Context patternContext;
	public static boolean metronome;
	
	public static void initialize()
	{
		metronome = false;
		Global.arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
		Global.arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
		Global.arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
		Global.arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
		
		Global.patternSoundQueues.add(new PriorityQueue<Sound>(10, Global.comp));
		Global.patternSoundQueues.add(new PriorityQueue<Sound>(10, Global.comp));
		Global.patternSoundQueues.add(new PriorityQueue<Sound>(10, Global.comp));
		Global.patternSoundQueues.add(new PriorityQueue<Sound>(10, Global.comp));
		
		for (int i = 0; i < 4; i++) {
			Global.soundIds[0][0] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.closedhat, 1);
			Global.soundIds[0][1] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.cymbal, 1);
			Global.soundIds[0][2] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.halfopenhat, 1);
			Global.soundIds[0][3] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.hitom, 1);
			Global.soundIds[1][0] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.kick, 1);
			Global.soundIds[1][1] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.lowtom, 1);
			Global.soundIds[1][2] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.openhat, 1);
			Global.soundIds[1][3] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.snare, 1);
			Global.soundIds[2][0] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.closedhat, 1);
			Global.soundIds[2][1] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.cymbal, 1);
			Global.soundIds[2][2] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.halfopenhat, 1);
			Global.soundIds[2][3] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.hitom, 1);
			Global.soundIds[3][0] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.kick, 1);
			Global.soundIds[3][1] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.whistle, 1);
			Global.soundIds[3][2] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.laugh, 1);
			Global.soundIds[3][3] = Global.arrSoundPool.get(i).load(Global.patternContext, raw.scream, 1);
		}
		
	}
	
}
