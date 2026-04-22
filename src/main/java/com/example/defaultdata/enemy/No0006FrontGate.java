package com.example.defaultdata.enemy;

import java.util.Arrays;
import java.util.List;

import com.example.defaultdata.AtackPattern;
import com.example.defaultdata.Element;
import com.example.defaultdata.Move;
import com.example.defaultdata.Type;

public class No0006FrontGate extends EnemyData{
	private static final String LINK = "/image/facility/front-gate";
	private static final String BULLET_LINK = "/image/weapon/No0001-bow/bow-";
	
	@Override
	public String getName() {
		return "敵城門";
	}

	@Override
	public String getExplanation() {
		return "敵の進軍を防ぐ攻撃用門。破壊するとその先の配置マスを解放することがある。";
	}
	
	@Override
	public String getImageName() {
		return LINK + PNG;
	}

	@Override
	public List<String> getActionImageName() {
		return Arrays.asList(getImageName());
	}

	@Override
	public String getBulletImageName() {
		return BULLET_LINK + "bullet" + PNG;
	}

	@Override
	public List<String> getHitImageName() {
		return Arrays.asList(BULLET_LINK + "hit 1" + PNG,
				BULLET_LINK + "hit 2" + PNG,
				BULLET_LINK + "hit 3" + PNG);
	}

	@Override
	public Move getMove() {
		return Move.NO_MOVE;
	}

	@Override
	public Type getType() {
		return Type.FACILITY;
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
		return Arrays.asList(30, 150, 500, 2);
	}

	@Override
	public List<Integer> getUnitStatus() {
		return Arrays.asList(3000, 3000, 5, 0, 0, 10);
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