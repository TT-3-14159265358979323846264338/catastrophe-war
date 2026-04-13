package com.example.catastrophewar.editdata;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.defaultdata.Stage;
import com.example.savedata.InitializeSQL;
import com.example.savedata.ItemRepository;
import com.example.savedata.ItemSQL;
import com.example.savedata.ProgressRepository;
import com.example.savedata.ProgressSQL;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EditStage {
	private final ItemRepository itemRepository;
	private final ProgressRepository progressRepository;
	
	@GetMapping("/api/edit/stage/data")
	@Transactional(readOnly = true)
	public DefaultEditStageData sendEditStageData() {
		return new DefaultEditStageData(medalSQL().getNumber(), createStageData());
	}
	
	record DefaultEditStageData(int medal, List<StageData> stageData) {}
	
	record StageData(String name, String image, boolean stageClear, List<Boolean> meritClear) {}
	
	ItemSQL medalSQL() {
		return itemRepository.findById(InitializeSQL.MEDAL_INDEX).orElseThrow(() -> new RuntimeException("メダル数を取り込めない"));
	}
	
	List<StageData> createStageData(){
		Stage[] stage = Stage.values();
		List<ProgressSQL> sql = progressRepository.findAll();
		return IntStream.range(0, stage.length)
				.mapToObj(i -> new StageData(stage[i].getLabel().getName(), 
											stage[i].getLabel().getImageName(), 
											sql.get(i).getStageClear(), 
											sql.get(i).getMerit()))
				.toList();
	}
	
	@PostMapping("/api/edit/save/stage")
	@Transactional
	public void saveStageData(@RequestBody StageCondition stageCondition) {
		saveMedal(stageCondition.medal);
		saveClear(stageCondition.clear, stageCondition.merit);
	}
	
	record StageCondition(int medal, List<Boolean> clear, List<List<Boolean>> merit) {}
	
	void saveMedal(int number) {
		ItemSQL itemSQL = medalSQL();
		itemSQL.setNumber(number);
		itemRepository.save(itemSQL);
	}
	
	void saveClear(List<Boolean> clear, List<List<Boolean>> merit) {
		List<ProgressSQL> allSQL = progressRepository.findAll();
		IntStream.range(0, clear.size()).forEach(i -> {
			ProgressSQL sql = allSQL.get(i);
			sql.setStageClear(clear.get(i));
			sql.setData(merit.get(i));
		});
		progressRepository.saveAll(allSQL);
	}
}