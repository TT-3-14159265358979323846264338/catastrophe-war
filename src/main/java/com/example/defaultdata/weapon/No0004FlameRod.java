package com.example.defaultdata.weapon;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.example.defaultdata.AtackPattern;
import com.example.defaultdata.Distance;
import com.example.defaultdata.Element;
import com.example.defaultdata.Handle;

public class No0004FlameRod extends WeaponData{
	private static final String LINK = "No0004-flame-rod/flame-rod";
	
	@Override
	public String getName() {
		return "炎ロッド";
	}

	@Override
	public String getExplanation() {
		return "生み出した炎で敵を攻撃する遠隔武器。弱った敵を優先的に狙う。";
	}
	
	@Override
	public String getImageName() {
		return imageLink(LINK);
	}

	@Override
	public List<String> getRightActionImageName() {
		return IntStream.range(0, 6).mapToObj(i -> rightImageLink(LINK, i)).toList();
	}

	@Override
	public List<String> getLeftActionImageName() {
		return IntStream.range(0, 6).mapToObj(i -> leftImageLink(LINK, i)).toList();
	}

	@Override
	public String getBulletImageName() {
		return bulletImageLink(LINK);
	}

	@Override
	public List<String> getHitImageName() {
		return IntStream.range(1, 4).mapToObj(i -> hitImageLink(LINK, i)).toList();
	}
	
	@Override
	public int getRarity() {
		return 1;
	}

	@Override
	public Distance getDistance() {
		return Distance.FAR;
	}

	@Override
	public Handle getHandle() {
		return Handle.ONE;
	}

	@Override
	public List<Element> getElement() {
		return Arrays.asList(Element.FLAME);
	}

	@Override
	public int getAtackPattern() {
		return AtackPattern.LOW_HP;
	}

	@Override
	public List<Integer> getWeaponStatus() {
		return Arrays.asList(75, 100, 1000, 1);
	}

	@Override
	public List<Integer> getUnitStatus() {
		return Arrays.asList(300, 300, 10, 10, 0, 5);
	}

	@Override
	public List<Integer> getCutStatus() {
		return Arrays.asList(0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0);
	}
	
	@Override
	public List<List<Double>> getBuff(){
		return Arrays.asList();
	}
}