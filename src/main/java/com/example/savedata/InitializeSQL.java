package com.example.savedata;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.example.defaultdata.Core;
import com.example.defaultdata.Stage;
import com.example.defaultdata.Weapon;

@Component
public class InitializeSQL implements CommandLineRunner{
	public static final int MEDAL_INDEX = 1;
	
	private final int COMPOSITION_NUMBER = 20;
	private final int SELECT_NUMBER = 2;
	private final List<Integer> DEFAULT_WEAPON_NUMBER = List.of(2, 2);
	private final List<Integer> DEFAULT_CORE_NUMBER = List.of(8);
	private final int DEFAULT_MEDAL = 1000;
	private final int NO_DATA = 0;

	@Autowired
	private CompositionRepository compositionRepository;
	
	@Autowired
	private SelectRepository selectRepository;
	
	@Autowired
	private WeaponRepository weaponRepository;
	
	@Autowired
	private CoreRepository coreRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ProgressRepository progressRepository;
	
	@Override
	public void run(String... args) throws Exception {
		initializeProgressSQL();
		if (0 < compositionRepository.count()) {
			addNewUnit();
			return;
		}
		initializeCompositionSQL();
		initializeSelectSQL();
		initializeUnitSQL();
		initializeItemSQL();
	}
	
	void initializeCompositionSQL() {
		for(int i = 0; i < COMPOSITION_NUMBER; i++) {
			CompositionSQL sql = createCompositionSQL();
			sql.setName(String.format("編成%d", i + 1));
			sql.initialize();
			compositionRepository.save(sql);
		}
	}
	
	CompositionSQL createCompositionSQL() {
		return new CompositionSQL();
	}
	
	void initializeSelectSQL() {
		for(int i = 0; i < SELECT_NUMBER; i++) {
			SelectSQL sql = createSelectSQL();
			sql.setSelectCode(NO_DATA);
			selectRepository.save(sql);
		}
	}
	
	SelectSQL createSelectSQL() {
		return new SelectSQL();
	}
	
	void initializeUnitSQL() {
		DEFAULT_WEAPON_NUMBER.stream().forEach(i -> addUnitSQL(weaponRepository, this::createWeaponSQL, i));
		DEFAULT_CORE_NUMBER.stream().forEach(i -> addUnitSQL(coreRepository, this::createCoreSQL, i));
		addNewUnit();
	}
	
	void addNewUnit() {
		addNewUnitSQL(weaponRepository, this::createWeaponSQL, Weapon.values().length);
		addNewUnitSQL(coreRepository, this::createCoreSQL, Core.values().length);
	}
	
	<T extends JpaRepository<U, Integer>, U extends BaseSQL> void addNewUnitSQL(T repository, Supplier<U> supplier, int count) {
		LongStream.range(0, count - repository.count()).forEach(_ -> addUnitSQL(repository, supplier, NO_DATA));
	}
	
	void initializeItemSQL(){
		addUnitSQL(itemRepository, this::createItemSQL, DEFAULT_MEDAL);
	}
	
	<T extends JpaRepository<U, Integer>, U extends BaseSQL> void addUnitSQL(T repository, Supplier<U> supplier, int number) {
		U sql = supplier.get();
		sql.setNumber(number);
		repository.save(sql);
	}
	
	WeaponSQL createWeaponSQL() {
		return new WeaponSQL();
	}
	
	CoreSQL createCoreSQL() {
		return new CoreSQL();
	}
	
	ItemSQL createItemSQL() {
		return new ItemSQL();
	}
	
	void initializeProgressSQL(){
		LongStream.range(progressRepository.count(), Stage.values().length).forEach(i -> {
			ProgressSQL sql = createProgressSQL();
			sql.initialize((int) i);
			progressRepository.save(sql);
		});
	}
	
	ProgressSQL createProgressSQL() {
		return new ProgressSQL();
	}
}