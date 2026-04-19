package com.example.catastrophewar.itemget;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catastrophewar.SessionController;
import com.example.catastrophewar.SessionState;
import com.example.commonclass.ImageLink;
import com.example.commonclass.Messaging;
import com.example.defaultdata.GachaCount;
import com.example.defaultdata.other.OtherData;
import com.example.savedata.InitializeSQL;
import com.example.savedata.ItemRepository;
import com.example.savedata.ItemSQL;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemGetPage extends Messaging{
	private final SimpMessagingTemplate messaging;
	private final SessionController sessions;
	private final ItemRepository itemRepository;
	
	@GetMapping("/api/gacha/data")
	public DefaultGachaData sendGachaData() {
		return createData();
	}
	
	DefaultGachaData createData(){
		return new DefaultGachaData(medalNumber(), createGachaCount(), createImageLink());
	}
	
	record DefaultGachaData(int medal, List<String> gachaCount, GachaImageLink links) {};
	
	@Transactional(readOnly = true)
	public int medalNumber() {
		return getItemSQL().getNumber();
	}
	
	ItemSQL getItemSQL() {
		return itemRepository.findById(InitializeSQL.MEDAL_INDEX).orElseThrow(() -> new RuntimeException("メダル数を取り込めない"));
	}
	
	List<String> createGachaCount(){
		return Stream.of(GachaCount.values()).map(this::gachaCountComment).toList();
	}
	
	String gachaCountComment(GachaCount gachaCount) {
		return String.format("%d連ガチャ\n%d枚", gachaCount.getLabel(), gachaCount.getUsedMedal());
	}
	
	GachaImageLink createImageLink() {
		OtherData otherData = createOtherData();
		return new GachaImageLink(ImageLink.normalCoreLinkList(),
				ImageLink.normalWeaponLinkList(),
				otherData.getHalfBall(),
				otherData.getHandle(),
				otherData.getMachine(),
				otherData.getTurn(),
				otherData.getEffect());
	}
	
	OtherData createOtherData() {
		return new OtherData();
	}
	
	record GachaImageLink(List<String> coreImageLink, 
			List<String> weaponImageLink, 
			List<String> halfBallImageLink, 
			String handleImageLink, 
			List<String> machineImageLink, 
			String turnImageLink, 
			String effectImageLink) {}
	
	@MessageMapping("/gacha/timer/start")
	public void timerStart(SimpMessageHeaderAccessor accessor) {
		String sessionId = accessor.getSessionId();
		SessionState state = sessions.getState(sessionId);
		state.setItemGetPageState();
		state.getItemGetPageState().drawTimerStart(() -> repaint(sessionId), medalNumber(), this);
		sendMessage(sessions, sessionId, _ -> messaging.convertAndSendToUser(sessionId, "/queue/gacha/list", state.getItemGetPageState().gachaData(), headers(sessionId)));
	}
	
	void repaint(String sessionId) {
		sendMessage(sessions, sessionId, state -> messaging.convertAndSendToUser(sessionId, "/queue/gacha/repaint", state.getItemGetPageState().createState(), headers(sessionId)));
	}
	
	void playGacha(String sessionId) {
		sendMessage(sessions, sessionId, _ -> messaging.convertAndSendToUser(sessionId, "/queue/gacha/play", "", headers(sessionId)));
	}
	
	@Transactional
	public int endGacha(int useMedal, String sessionId) {
		ItemSQL itemSQL = getItemSQL();
		int newMedal = itemSQL.getNumber() - useMedal;
		itemSQL.setNumber(newMedal);
		itemRepository.save(itemSQL);
		sendMessage(sessions, sessionId, _ -> messaging.convertAndSendToUser(sessionId, "/queue/gacha/end", newMedal, headers(sessionId)));
		return newMedal;
	}
	
	@MessageMapping("/gacha/mouse/pressed")
	public void mousePressed(@Payload ClickPoint clickPoint, SimpMessageHeaderAccessor accessor) {
		getItemGetPageState(accessor).mousePressed(clickPoint.x, clickPoint.y);
	}
	
	@MessageMapping("/gacha/mouse/dragged")
	public void mouseDragged(@Payload ClickPoint clickPoint, SimpMessageHeaderAccessor accessor) {
		getItemGetPageState(accessor).mouseDragged(clickPoint.x, clickPoint.y);
	}
	
	record ClickPoint(int x, int y) {}
	
	@MessageMapping("/gacha/mouse/released")
	public void mouseReleased(SimpMessageHeaderAccessor accessor) {
		getItemGetPageState(accessor).mouseReleased();
	}
	
	@MessageMapping("/gacha/select")
	public void changeSelected(SelectId selectId, SimpMessageHeaderAccessor accessor) {
		getItemGetPageState(accessor).changeSelected(selectId.selectId);
	}
	
	record SelectId(int selectId) {}
	
	@MessageMapping("/gacha/detail")
	public void sendDetail(SimpMessageHeaderAccessor accessor) {
		String sessionId = accessor.getSessionId();
		SessionState state = sessions.getState(sessionId);
		sendMessage(sessions, sessionId, _ -> messaging.convertAndSendToUser(sessionId, "/queue/gacha/detail/data", state.getItemGetPageState().detail(), headers(sessionId)));
	}
	
	@MessageMapping("/gacha/count/change")
	public void changeCount(@Payload ChangeId changeId, SimpMessageHeaderAccessor accessor) {
		getItemGetPageState(accessor).changeCount(changeId.id);
	}
	
	record ChangeId(int id) {}
	
	@MessageMapping("/gacha/timer/stop")
	public void endTimer(SimpMessageHeaderAccessor accessor) {
		getItemGetPageState(accessor).timerStop();
	}
	
	ItemGetPageState getItemGetPageState(SimpMessageHeaderAccessor accessor) {
		return sessions.getState(accessor).getItemGetPageState();
	}
}