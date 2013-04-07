package com.example.beatmakingapp;

public class Sound {
	private int soundPoolId;
	private int buttonId_i;
	private int buttonId_j;
	private long offset;
	private int patternId;
	
	public Sound(int id, int buttonId_i, int buttonId_j, long l, int patternId) {
		this.soundPoolId = id;
		this.buttonId_i = buttonId_i;
		this.buttonId_j = buttonId_j;
		this.offset = l;
		this.patternId = patternId;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public void setSoundPoolId(int id) {
		soundPoolId = id;
	}
	public int getPatternId() {
		return patternId;
	}
	public int getButtonId_i() {
		return buttonId_i;
	}
	public int getButtonId_j() {
		return buttonId_j;
	}
	public int getSoundPoolId() {
		return soundPoolId;
	}
	public long getOffset() {
		return offset;
	}
}
