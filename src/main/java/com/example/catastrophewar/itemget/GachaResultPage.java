package com.example.catastrophewar.itemget;

import java.util.List;
import java.util.stream.IntStream;

import com.example.defaultdata.Core;
import com.example.defaultdata.gacha.GachaData;
import com.example.defaultdata.Weapon;

class GachaResultPage {
	private final String CORE = "core";
	private final String WEAPON = "weapon";
	private List<UnitData> result;
	
	record UnitData(String unitCode, int id, String imageLink) {}
	
	<T extends GachaData> GachaResultPage(T gachaData, int count) {
		try {
			result = IntStream.range(0, count).mapToObj(_ -> result(gachaData)).toList();
		} catch (RuntimeException e) {
			result = List.of();
		}
	}
	
	<T extends GachaData> UnitData result(T gachaData){
		double value = createRandom();
		double total = 0;
		for(int i = 0; i < gachaData.getCoreRatio().size(); i++) {
			total += gachaData.getCoreRatio().get(i);
			if(value <= total) {
				Core core = gachaData.getCoreLineup().get(i);
				return new UnitData(CORE, core.getId(), core.getLabel().getImageName());
			}
		}
		for(int i = 0; i < gachaData.getWeaponRatio().size(); i++) {
			total += gachaData.getWeaponRatio().get(i);
			if(value <= total) {
				Weapon weapon = gachaData.getWeaponLineup().get(i);
				return new UnitData(WEAPON, weapon.getId(), weapon.getLabel().getImageName());
			}
		}
		throw new RuntimeException("ガチャに失敗しました");
	}
	
	double createRandom() {
		return Math.random() * 100;
	}
	
	List<UnitData> getResult(){
		return result;
	}
}