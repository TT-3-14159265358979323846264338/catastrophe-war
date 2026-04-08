package com.example.catastrophewar.editdata;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.commonclass.ImageLink;
import com.example.defaultdata.Core;
import com.example.defaultdata.DefaultData;
import com.example.defaultdata.DefaultEnum;
import com.example.defaultdata.Weapon;
import com.example.savedata.BaseSQL;
import com.example.savedata.CoreRepository;
import com.example.savedata.WeaponRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EditUnit {
	private final SimpMessagingTemplate messaging;
	private final CoreRepository coreRepository;
	private final WeaponRepository weaponRepository;
	
	@GetMapping("/api/edit/unit/data")
	@Transactional(readOnly = true)
	public DefaultEditUnitData sendEditUnitData() {
		return new DefaultEditUnitData(
				nameList(Core.values()), 
				nameList(Weapon.values()), 
				ImageLink.normalCoreLinkList(), 
				ImageLink.normalWeaponLinkList(), 
				numberList(coreRepository), 
				numberList(weaponRepository));
	}
	
	record DefaultEditUnitData(
			List<String> coreName, 
			List<String> weaponName, 
			List<String> coreImages, 
			List<String> weaponImages, 
			List<Integer> coreNumber, 
			List<Integer> weaponNumber) {}
	
	<T extends DefaultEnum<U>, U extends DefaultData> List<String> nameList(T[] data){
		return DefaultEnum.getLabelStream(data).map(DefaultData::getName).toList();
	}
	
	<T extends JpaRepository<? extends BaseSQL, Integer>> List<Integer> numberList(T repository){
		return repository.findAll().stream().map(BaseSQL::getNumber).toList();
	}
}