package com.example.defaultdata.weapon;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.example.defaultdata.AtackPattern;
import com.example.defaultdata.Distance;
import com.example.defaultdata.Element;
import com.example.defaultdata.Handle;

public class No0000JapaneseSword extends WeaponData{
	private static final String LINK = "No0000-Japanese-sword/Japanese-sword";
	
	@Override
	public String getName() {
		return "日本刀";
	}

	@Override
	public String getExplanation() {
		return "一般的な近接武器。1体をブロックし、攻撃できる。";
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
		return Distance.NEAR;
	}

	@Override
	public Handle getHandle() {
		return Handle.ONE;
	}

	@Override
	public List<Element> getElement() {
		return Arrays.asList(Element.SLASH);
	}

	@Override
	public int getAtackPattern() {
		return AtackPattern.NEAR;
	}

	@Override
	public List<Integer> getWeaponStatus() {
		return Arrays.asList(100, 40, 1000, 1);
	}

	@Override
	public List<Integer> getUnitStatus() {
		return Arrays.asList(500, 500, 30, 30, 1, 5);
	}

	@Override
	public List<Integer> getCutStatus() {
		return Arrays.asList(10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	@Override
	public List<List<Double>> getBuff(){
		return Arrays.asList();
	}
}