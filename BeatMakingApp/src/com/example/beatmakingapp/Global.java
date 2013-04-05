package com.example.beatmakingapp;

import java.util.ArrayList;

import android.media.AudioManager;
import android.media.SoundPool;

public class Global {

	public static ArrayList<SoundPool> arrSoundPool = new ArrayList<SoundPool>();
	
	public static int test = 0;
	
	public static void initialize()
	{
		test = test+1;
		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
		arrSoundPool.add(new SoundPool(16, AudioManager.STREAM_MUSIC, 0));
		
	}
}
