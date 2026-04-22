package com.example.defaultdata.core;

import java.util.Arrays;
import java.util.List;

public class No0000NormalCore extends CoreData{
	private static final String LINK = "No0000-normal-core/normal-core";
	
	@Override
	public String getName() {
		return "ノーマルコア";
	}

	@Override
	public String getExplanation() {
		return "初期コア。ゲーム開始時に8体獲得可能。リサイクル不可。";
	}
	
	@Override
	public String getImageName() {
		return imageLink(LINK);
	}
	
	@Override
	public String getActionImageName() {
		return actionImageLink(LINK);
	}
	
	@Override
	public int getRarity() {
		return 1;
	}
	
	@Override
	public List<Double> getWeaponStatus(){
		return Arrays.asList(1.0, 1.0, 1.0, 1.0);
	}
	
	@Override
	public List<Double> getUnitStatus(){
		return Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
	}
	
	@Override
	public List<Integer> getCutStatus(){
		return Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	@Override
	public List<List<Double>> getBuff(){
		return Arrays.asList();
	}

	@Override
	public String getSkillImageName() {
		return null;
	}
}