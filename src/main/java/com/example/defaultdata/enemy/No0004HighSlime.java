package com.example.defaultdata.enemy;

import java.util.Arrays;
import java.util.List;

import com.example.defaultdata.AtackPattern;
import com.example.defaultdata.Element;
import com.example.defaultdata.Move;
import com.example.defaultdata.Type;

public class No0004HighSlime extends EnemyData{
	private static final String LINK = "No0004-high-slime/high-slime";
	
	@Override
	public String getName() {
		return "ハイスライム";
	}

	@Override
	public String getExplanation() {
		return "通常よりも能力の高いスライム。物理武器が有効。";
	}
	
	@Override
	public String getImageName() {
		return imageLink(LINK);
	}

	@Override
	public List<String> getActionImageName() {
		return Arrays.asList(imageLink(LINK + "-0"),
				imageLink(LINK + "-1"),
				imageLink(LINK + "-2"),
				imageLink(LINK + "-3"),
				imageLink(LINK + "-4"),
				imageLink(LINK + "-5"));
	}

	@Override
	public String getBulletImageName() {
		return null;
	}

	@Override
	public List<String> getHitImageName() {
		return Arrays.asList();
	}

	@Override
	public Move getMove() {
		return Move.GROUND;
	}

	@Override
	public Type getType() {
		return Type.NORMAL;
	}

	@Override
	public List<Element> getElement() {
		return Arrays.asList(Element.STRIKE);
	}

	@Override
	public int getAtackPattern() {
		return AtackPattern.NEAR;
	}

	@Override
	public List<Integer> getWeaponStatus() {
		return Arrays.asList(100, 30, 1000, 1);
	}

	@Override
	public List<Integer> getUnitStatus() {
		return Arrays.asList(1000, 1000, 30, 20, 100, 1);
	}

	@Override
	public List<Integer> getCutStatus() {
		return Arrays.asList(0, 0, 0, 0, 30, 30, 30, 30, 30, 30, 30, 0);
	}
	
	@Override
	public List<List<Double>> getBuff(){
		return Arrays.asList();
	}
}