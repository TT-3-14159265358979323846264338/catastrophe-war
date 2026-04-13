package com.example.catastrophewar.editdata;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
	private final CoreRepository coreRepository;
	private final WeaponRepository weaponRepository;
	
	@GetMapping("/api/edit/unit/data")
	@Transactional(readOnly = true)
	public DefaultEditUnitData sendEditUnitData() {
		return new DefaultEditUnitData(createDataList(Core.values(), coreRepository), createDataList(Weapon.values(), weaponRepository));
	}
	
	record DefaultEditUnitData(List<UnitData> coreData, List<UnitData> weaponData) {}
	
	record UnitData(String name, String image, Integer number) {}
	
	<T extends DefaultEnum<? extends DefaultData>, U extends JpaRepository<V, Integer>, V extends BaseSQL> List<UnitData> createDataList(T[] unit, U repository){
		List<V> sql = repository.findAll();
		return IntStream.range(0, unit.length).mapToObj(i -> createData(unit[i].getLabel(), sql.get(i))).toList();
	}
	
	<T extends DefaultData, U extends BaseSQL> UnitData createData(T data, U sql) {
		return new UnitData(data.getName(), data.getImageName(), sql.getNumber());
	}
	
	@PostMapping("/api/edit/save/unit")
	@Transactional
	public void saveUnitData(@RequestBody UnitNumber unitNumber) {
		saveData(unitNumber.coreNumber, coreRepository);
		saveData(unitNumber.weaponNumber, weaponRepository);
	}
	
	record UnitNumber(List<Integer> coreNumber, List<Integer> weaponNumber) {}
	
	<T extends JpaRepository<U, Integer>, U extends BaseSQL> void saveData(List<Integer> numberList, T repository) {
		List<U> allSQL = repository.findAll();
		IntStream.range(0, numberList.size()).forEach(i -> allSQL.get(i).setNumber(numberList.get(i)));
		repository.saveAll(allSQL);
	}
}