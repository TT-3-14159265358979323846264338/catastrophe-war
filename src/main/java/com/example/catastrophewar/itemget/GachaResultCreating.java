package com.example.catastrophewar.itemget;

import java.util.List;
import java.util.stream.IntStream;

import com.example.commonclass.Code;
import com.example.defaultdata.DefaultData;
import com.example.defaultdata.DefaultEnum;
import com.example.defaultdata.gacha.GachaData;

class GachaResultCreating {
	private final List<GachaResult> result;
	
	<T extends GachaData> GachaResultCreating(T gachaData, int count) {
		result = IntStream.range(0, count).mapToObj(_ -> result(gachaData)).toList();
	}
	
	<T extends GachaData> GachaResult result(T gachaData){
		double value = createRandom();
		double total = 0;
		for(int i = 0; i < gachaData.getCoreRatio().size(); i++) {
			total += gachaData.getCoreRatio().get(i);
			if(value <= total) {
				return createGachaResult(Code.CORE, gachaData.getCoreLineup().get(i));
			}
		}
		if(gachaData.getWeaponLineup().size() == 0) {
			return createGachaResult(Code.CORE, gachaData.getCoreLineup().getLast());
		}
		for(int i = 0; i < gachaData.getWeaponRatio().size(); i++) {
			total += gachaData.getWeaponRatio().get(i);
			if(value <= total) {
				return createGachaResult(Code.WEAPON, gachaData.getWeaponLineup().get(i));
			}
		}
		return createGachaResult(Code.WEAPON, gachaData.getWeaponLineup().getLast());
	}
	
	double createRandom() {
		return Math.random() * 100;
	}
	
	<T extends DefaultEnum<? extends DefaultData>> GachaResult createGachaResult(String code, T target) {
		return new GachaResult(code, target.getId(), target.getLabel().getImageName());
	}
	
	List<GachaResult> getResult(){
		return result;
	}
}