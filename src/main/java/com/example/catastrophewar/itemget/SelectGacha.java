package com.example.catastrophewar.itemget;

import java.util.List;

import com.example.defaultdata.Core;
import com.example.defaultdata.DefaultEnum;
import com.example.defaultdata.Gacha;
import com.example.defaultdata.GachaCount;
import com.example.defaultdata.Weapon;
import com.example.defaultdata.gacha.GachaData;

class SelectGacha {
	private Gacha gacha;
	private GachaCount gachaCount;
	private int medal;
	
	SelectGacha(){
		gachaCount = GachaCount.TEN;
	}
	
	List<Core> getCoreLineup(){
		return selectGachaData().getCoreLineup();
	}
	
	List<Double> getCoreRatio(){
		return selectGachaData().getCoreRatio();
	}
	
	List<Weapon> getWeaponLineup(){
		return selectGachaData().getWeaponLineup();
	}
	
	List<Double> getWeaponRatio(){
		return selectGachaData().getWeaponRatio();
	}
	
	GachaData selectGachaData() {
		return gacha.getLabel();
	}
	
	void setSelectId(int id) {
		gacha = DefaultEnum.getEnum(Gacha.values(), id);
	}
	
	int getGachaCountId() {
		return gachaCount.getId();
	}
	
	int getGachaCountLabel() {
		return gachaCount.getLabel();
	}
	
	int getUsedMedal() {
		return gachaCount.getUsedMedal();
	}
	
	boolean canPlayGacha() {
		return getUsedMedal() <= medal;
	}
	
	void setGachaCount(int id) {
		gachaCount = DefaultEnum.getEnum(GachaCount.values(), id);
	}
	
	int getMedal() {
		return medal;
	}
	
	void setMedal(int medal) {
		this.medal = medal;
	}
}