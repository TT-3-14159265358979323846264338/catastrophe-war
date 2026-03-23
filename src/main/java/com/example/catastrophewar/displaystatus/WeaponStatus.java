package com.example.catastrophewar.displaystatus;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.defaultdata.Atack;
import com.example.defaultdata.AtackPattern;
import com.example.defaultdata.DefaultEnum;
import com.example.defaultdata.Weapon;
import com.example.defaultdata.WeaponUnit;
import com.example.defaultdata.weapon.WeaponData;

@RestController
public class WeaponStatus extends ElementStatus{
	@Override
	@GetMapping("/api/status/weapon/{id}")
	public Status sendStatus(@PathVariable int id) {
		WeaponData select = DefaultEnum.getLabel(Weapon.values(), id);
		return new Status(select.getName(),
				getLabel(),
				getWeaponStatus(select),
				null,
				defaultLabel(WeaponUnit.values()),
				select.getUnitStatus(),
				cutLabel(),
				addPercent(select.getCutStatus()));
	}
	
	List<String> getLabel(){
		return Stream.concat(defaultLabelStream(Atack.values()), weaponType()).toList();
	}
	
	Stream<String> weaponType(){
		return Stream.of("距離タイプ", "装備タイプ", "属性", "ターゲット");
	}
	
	List<String> getWeaponStatus(WeaponData weaponData){
		Stream<String> anotherData = Stream.of(weaponData.getDistance().getLabel(), 
				weaponData.getHandle().getLabel(), 
				getElement(weaponData.getElement()), 
				attackPattern(weaponData));
		return Stream.concat(weaponData.getWeaponStatus().stream().map(String::valueOf), anotherData).toList();
	}
	
	String attackPattern(WeaponData weaponData) {
		return new AtackPattern().getAtackPattern(weaponData.getAtackPattern()).getExplanation();
	}
}