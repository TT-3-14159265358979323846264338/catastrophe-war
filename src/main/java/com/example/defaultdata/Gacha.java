package com.example.defaultdata;

import com.example.defaultdata.gacha.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Gacha implements DefaultEnum<GachaData>{
	ALL_RARITY1(0, new No0001AllGachaRarity1()),
	CORE_RARITY1(1, new No0002CoreGachaRarity1()),
	WEAPON_RARITY1(2, new No0003WeaponGachaRarity1());
	
	private final int id;
	private final GachaData gachaData;
	
	Gacha(int id, GachaData gachaData) {
		this.id = id;
		this.gachaData = gachaData;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public GachaData getLabel() {
		return gachaData;
	}
}