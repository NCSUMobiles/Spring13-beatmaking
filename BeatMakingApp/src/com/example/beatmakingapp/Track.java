package com.example.beatmakingapp;

import java.util.ArrayList;

public class Track {
	private String name= null;

	private long length;
	private ArrayList<Pattern> patterns = null;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}


	ArrayList<Pattern> getPatterns() {
		return patterns;
	}

	void setPatterns(ArrayList<Pattern> patterns) {
		this.patterns = patterns;
	}
}
