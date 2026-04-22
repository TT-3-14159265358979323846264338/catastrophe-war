package com.example.defaultdata.enemy;

import java.util.Arrays;
import java.util.List;

import com.example.defaultdata.AtackPattern;
import com.example.defaultdata.Element;
import com.example.defaultdata.Move;
import com.example.defaultdata.Type;

public class No0003YellowSlime extends EnemyData{
	private static final String LINK = "No0003-yellow-slime/yellow-slime";
	
	@Override
	public String getName() {
		return "イエロースライム";
	}

	@Override
	public String getExplanation() {
		return "ブルースライムよりも少し速度の速い敵。";
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
		return Arrays.asList(50, 30, 750, 1);
	}

	@Override
	public List<Integer> getUnitStatus() {
		return Arrays.asList(500, 500, 10, 0, 125, 1);
	}

	@Override
	public List<Integer> getCutStatus() {
		return Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	@Override
	public List<List<Double>> getBuff(){
		return Arrays.asList();
	}
}