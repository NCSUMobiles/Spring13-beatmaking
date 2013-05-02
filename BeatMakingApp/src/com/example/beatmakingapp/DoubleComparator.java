package com.example.beatmakingapp;

import java.util.Comparator;

public class DoubleComparator  implements Comparator<Sound> {

	@Override
	public int compare(Sound a, Sound b) {
		if(a.getOffset() < b.getOffset())
			return -1;
		if(b.getOffset() < a.getOffset())
			return 1;
		return 0;
	}
}
