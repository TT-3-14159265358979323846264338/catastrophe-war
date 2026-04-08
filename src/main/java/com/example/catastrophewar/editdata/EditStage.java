package com.example.catastrophewar.editdata;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.commonclass.ImageLink;
import com.example.savedata.InitializeSQL;
import com.example.savedata.ItemRepository;
import com.example.savedata.ProgressRepository;
import com.example.savedata.ProgressSQL;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EditStage {
	private final SimpMessagingTemplate messaging;
	private final ItemRepository itemRepository;
	private final ProgressRepository progressRepository;
	
	@GetMapping("/api/edit/stage/data")
	@Transactional(readOnly = true)
	public DefaultEditStageData sendEditStageData() {
		List<ProgressSQL> progressSQL = progressRepository.findAll();
		return new DefaultEditStageData(
				medal(), 
				ImageLink.stageNameList(), 
				ImageLink.stageImageList(), 
				progressSQL.stream().map(ProgressSQL::getStageClear).toList(), 
				progressSQL.stream().map(ProgressSQL::getMerit).toList());
	}
	
	record DefaultEditStageData(
			int medal, 
			List<String> stageName, 
			List<String> stageImages, 
			List<Boolean> stageClear, 
			List<List<Boolean>> meritClear) {}
	
	int medal() {
		return itemRepository.findById(InitializeSQL.MEDAL_INDEX).orElseThrow(() -> new RuntimeException("メダル数を取り込めない")).getNumber();
	}
}