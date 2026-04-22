package com.example.defaultdata.core;

import java.util.Arrays;
import java.util.List;

public class No0002NormalDefenseCore extends CoreData{
	private static final String LINK = "No0002-normal-defense-core/normal-defense-core";
	
	@Override
	public String getName() {
		return "ノーマルブラックコア";
	}
	
	@Override
	public String getExplanation() {
		return "防御力が少し高い通常コア。";
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
		return Arrays.asList(1.0, 1.0, 1.1, 1.0, 1.0, 1.0);
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