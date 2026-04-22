package com.example.defaultdata.weapon;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.example.defaultdata.AtackPattern;
import com.example.defaultdata.Distance;
import com.example.defaultdata.Element;
import com.example.defaultdata.Handle;

public class No0001Bow extends WeaponData{
	private static final String LINK = "No0001-bow/bow";
	
	@Override
	public String getName() {
		return "弓";
	}

	@Override
	public String getExplanation() {
		return "一般的な遠距離武器。長い射程で1体を攻撃する。";
	}
	
	@Override
	public String getImageName() {
		return imageLink(LINK);
	}

	@Override
	public List<String> getRightActionImageName() {
		return Arrays.asList();
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
		return Handle.BOTH;
	}

	@Override
	public List<Element> getElement() {
		return Arrays.asList(Element.PIERCE);
	}

	@Override
	public int getAtackPattern() {
		return AtackPattern.NEAR;
	}

	@Override
	public List<Integer> getWeaponStatus() {
		return Arrays.asList(100, 120, 1000, 1);
	}

	@Override
	public List<Integer> getUnitStatus() {
		return Arrays.asList(500, 500, 20, 0, 0, 10);
	}

	@Override
	public List<Integer> getCutStatus() {
		return Arrays.asList(0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	@Override
	public List<List<Double>> getBuff(){
		return Arrays.asList();
	}
}