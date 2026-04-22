package com.example.defaultdata.facility;

import java.util.Arrays;
import java.util.List;

import com.example.defaultdata.AtackPattern;
import com.example.defaultdata.Element;

public class No0001FrontGate extends FacilityData{
	private static final String GATE_LINK = "front-gate";
	private static final String BREAK_LINK = "break-gate";
	
	@Override
	public String getName() {
		return "城門";
	}

	@Override
	public String getExplanation() {
		return "敵の進軍を防ぐ門。破壊されるまで全ての敵をブロックする。";
	}

	@Override
	public String getImageName() {
		return imageLink(GATE_LINK);
	}
	
	@Override
	public List<String> getActionImageName() {
		return Arrays.asList(getImageName());
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
	public String getBreakImageName() {
		return imageLink(BREAK_LINK);
	}

	@Override
	public List<Element> getElement() {
		return Arrays.asList();
	}

	@Override
	public int getAtackPattern() {
		return AtackPattern.NO_ATACK;
	}

	@Override
	public List<Integer> getWeaponStatus() {
		return Arrays.asList();
	}

	@Override
	public List<Integer> getUnitStatus() {
		return Arrays.asList(10000, 10000, 0, 0, -1, 0);
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