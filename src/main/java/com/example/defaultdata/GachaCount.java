package com.example.defaultdata;

public enum GachaCount implements DefaultEnum<Integer>{
	ONE(0, 1),
	FIVE(1, 5),
	TEN(2, 10);
	
	private final int id;
	private final int count;
	private final int usedMedal;
	private final int USE = 100;
	
	GachaCount(int id, int count) {
		this.id = id;
		this.count = count;
		usedMedal = USE * count;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Integer getLabel() {
		return count;
	}
	
	public int getUsedMedal() {
		return usedMedal;
	}
}