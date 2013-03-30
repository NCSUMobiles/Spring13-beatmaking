package com.example.beatmakingapp;

public class Sound {
	private int id;
	private long offset;
	
	public Sound(int id, long l) {
		this.id = id;
		this.offset = l;
	}
	
	public int getId() {
		return id;
	}
	public long getOffset() {
		return offset;
	}
}
