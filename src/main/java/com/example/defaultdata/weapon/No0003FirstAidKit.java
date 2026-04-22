package com.example.defaultdata.weapon;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.example.defaultdata.AtackPattern;
import com.example.defaultdata.Distance;
import com.example.defaultdata.Element;
import com.example.defaultdata.Handle;

public class No0003FirstAidKit extends WeaponData{
	private static final String LINK = "No0003-first-aid-kit/first-aid-kit";
	
	@Override
	public String getName() {
		return "救急箱";
	}

	@Override
	public String getExplanation() {
		return "味方1体のHPを少し回復させる武器種。";
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
		return null;
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
		return Distance.ALL;
	}

	@Override
	public Handle getHandle() {
		return Handle.ONE;
	}

	@Override
	public List<Element> getElement() {
		return Arrays.asList(Element.SUPPORT);
	}

	@Override
	public int getAtackPattern() {
		return AtackPattern.LOW_HP;
	}

	@Override
	public List<Integer> getWeaponStatus() {
		return Arrays.asList(50, 100, 1000, 1);
	}

	@Override
	public List<Integer> getUnitStatus() {
		return Arrays.asList(200, 200, 10, 20, 0, 5);
	}

	@Override
	public List<Integer> getCutStatus() {
		return Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10);
	}
	
	@Override
	public List<List<Double>> getBuff(){
		return Arrays.asList();
	}
}