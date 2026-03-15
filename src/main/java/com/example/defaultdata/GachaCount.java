package com.example.defaultdata;

public enum GachaCount {
	ONE(1),
	FIVE(5),
	TEN(10);
	
	private final int count;
	
	GachaCount(int count) {
		this.count = count;
	}

	public int getGachaData() {
		return count;
	}
}