package com.example.beatmakingapp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.beatmakingapp.R.raw;

public class Global {

	
	public static boolean initialized = false;
	public static ArrayList<SoundPool> arrSoundPool = new ArrayList<SoundPool>();
	
	public static Comparator<Sound> comp = new LongComparator();

	
	public static ArrayList<PriorityQueue<Sound>> patternSoundQueues = new ArrayList<PriorityQueue<Sound>>();
	public static ArrayList<Integer> pattern1SegmentPositions = new ArrayList<Integer>();
	
	public static int[][] soundIds = new int[4][4];
	
	public static Context patternContext;
	
	public static void initialize()
	{
//		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
//		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
//		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
//		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
//		
		for (int i = 0; i < 4; i++) {
			/*soundIds[0][0] = arrSoundPool.get(i).load(patternContext, raw.closedhat, 1);
			soundIds[0][1] = arrSoundPool.get(i).load(patternContext, raw.cymbal, 1);
			soundIds[0][2] = arrSoundPool.get(i).load(patternContext, raw.halfopenhat, 1);
			soundIds[0][3] = arrSoundPool.get(i).load(patternContext, raw.hitom, 1);
			soundIds[1][0] = arrSoundPool.get(i).load(patternContext, raw.kick, 1);
			soundIds[1][1] = arrSoundPool.get(i).load(patternContext, raw.lowtom, 1);
			soundIds[1][2] = arrSoundPool.get(i).load(patternContext, raw.openhat, 1);
			soundIds[1][3] = arrSoundPool.get(i).load(patternContext, raw.snare, 1);
			soundIds[2][0] = arrSoundPool.get(i).load(patternContext, raw.closedhat, 1);
			soundIds[2][1] = arrSoundPool.get(i).load(patternContext, raw.cymbal, 1);
			soundIds[2][2] = arrSoundPool.get(i).load(patternContext, raw.halfopenhat, 1);
			soundIds[2][3] = arrSoundPool.get(i).load(patternContext, raw.hitom, 1);
			soundIds[3][0] = arrSoundPool.get(i).load(patternContext, raw.kick, 1);
			soundIds[3][1] = arrSoundPool.get(i).load(patternContext, raw.lowtom, 1);
			soundIds[3][2] = arrSoundPool.get(i).load(patternContext, raw.openhat, 1);
			soundIds[3][3] = arrSoundPool.get(i).load(patternContext, raw.snare, 1);*/
			patternSoundQueues.add(new PriorityQueue<Sound>(10, comp));
		}
	}
}
