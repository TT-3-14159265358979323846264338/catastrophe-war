package com.example.catastrophewar.itemget;

import java.awt.Point;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.commonclass.ImageLink;
import com.example.commonclass.Timer;
import com.example.defaultdata.Core;
import com.example.defaultdata.Gacha;
import com.example.defaultdata.GachaCount;
import com.example.defaultdata.Weapon;
import com.example.defaultdata.other.OtherData;
import com.example.savedata.InitializeSQL;
import com.example.savedata.ItemRepository;
import com.example.savedata.ItemSQL;

@RestController
public class ItemGetPage extends Timer{
	private final SelectGacha selectGacha;
	private final AutoRotate autoRotate;
	private final HandleMotion handleMotion;
	private final FallBallMotion fallBallMotion;
	private final OpenBallMotion openBallMotion;
	
	@Autowired
	private SimpMessagingTemplate messaging;
	
	@Autowired
	private ItemRepository itemRepository;
	
	ItemGetPage(ScheduledExecutorService scheduler){
		super(scheduler);
		selectGacha = createSelectGacha();
		autoRotate = createAutoRotate(scheduler);
		openBallMotion = createOpenBallMotion(scheduler);
		fallBallMotion = createfallBallMotion(scheduler);
		handleMotion = createHandleMotion(scheduler);
	}
	
	SelectGacha createSelectGacha(){
		return new SelectGacha();
	}
	
	AutoRotate createAutoRotate(ScheduledExecutorService scheduler) {
		return new AutoRotate(scheduler);
	}
	
	OpenBallMotion createOpenBallMotion(ScheduledExecutorService scheduler) {
		return new OpenBallMotion(this, scheduler);
	}
	
	FallBallMotion createfallBallMotion(ScheduledExecutorService scheduler) {
		return new FallBallMotion(openBallMotion, scheduler);
	}
	
	HandleMotion createHandleMotion(ScheduledExecutorService scheduler) {
		return new HandleMotion(this, fallBallMotion, scheduler);
	}
	
	@GetMapping("/api/gacha/data")
	public DefaultGachaData sendGachaData() {
		return createData();
	}
	
	DefaultGachaData createData(){
		return new DefaultGachaData(medalNumber(), createGachaCount(), selectGacha.getGachaCountId(), createImageLink());
	}
	
	record DefaultGachaData(int medal, List<String> gachaCount, int id, GachaImageLink links) {};
	
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
	@SendTo("/topic/gacha/list")
	public Gacha[] timerStart() {
		selectGacha.setMedal(medalNumber());
		timerStart(this::repaint);
		autoRotate.timerStart();
		return createGachaList();
	}
	
	Gacha[] createGachaList(){
		return Gacha.values();
	}
	
	void repaint() {
		messaging.convertAndSend("/topic/gacha/repaint", createState());
	}
	
	State createState() {
		return new State(autoRotate.getAngle(),
				canPlayGacha(),
				handleMotion.getAngle(),
				getState(BallState::getTopPoint),
				getState(BallState::getBottomPoint),
				getState(BallState::getTopAngle),
				getState(BallState::getBottomAngle),
				openBallMotion.getColor(),
				openBallMotion.getExpansion());
	}
	
	boolean canPlayGacha() {
		return !isPlayingGacha() && selectGacha.canPlayGacha();
	}
	
	boolean isPlayingGacha() {
		return handleMotion.isRunning() || fallBallMotion.isRunning() || openBallMotion.isRunning();
	}
	
	<T> T getState(Function<BallState, T> task) {
		if(openBallMotion.isRunning()) {
			return task.apply(openBallMotion);
		}
		return task.apply(fallBallMotion);
	}
	
	record State(double turnAngle, 
			boolean canPlayGacha, 
			double handleAngle, 
			Point topPoint, 
			Point bottomPoint, 
			double topAngle, 
			double bottomAngle, 
			int color, 
			int expansion) {}
	
	void playGacha() {
		messaging.convertAndSend("/topic/gacha/play", "");
	}
	
	@Transactional
	public void endGacha() {
		ItemSQL itemSQL = getItemSQL();
		int newMedal = itemSQL.getNumber() - selectGacha.getUsedMedal();
		itemSQL.setNumber(newMedal);
		itemRepository.save(itemSQL);
		selectGacha.setMedal(newMedal);
		messaging.convertAndSend("/topic/gacha/end", newMedal);
	}
	
	@MessageMapping("/gacha/mouse/pressed")
	public void mousePressed(@Payload ClickPoint clickPoint) {
		if(selectGacha.canPlayGacha()) {
			handleMotion.mousePressed(clickPoint.x, clickPoint.y);
		}
	}
	
	@MessageMapping("/gacha/mouse/dragged")
	public void mouseDragged(@Payload ClickPoint clickPoint) {
		if(selectGacha.canPlayGacha()) {
			handleMotion.mouseDragged(clickPoint.x, clickPoint.y);
		}
	}
	
	@MessageMapping("/gacha/mouse/released")
	public void mouseReleased() {
		handleMotion.mouseReleased();
	}
	
	record ClickPoint(int x, int y) {}
	
	@MessageMapping("/gacha/select")
	public void changeSelected(SelectId selectId) {
		selectGacha.setSelectId(selectId.selectId);
	}
	
	record SelectId(int selectId) {}
	
	@GetMapping("/api/gacha/detail")
	public Detail sendDetail() {
		return new Detail(selectGacha.getCoreLineup(), selectGacha.getCoreRatio(), selectGacha.getWeaponLineup(), selectGacha.getWeaponRatio());
	}
	
	record Detail(List<Core> coreLineup, List<Double> coreRatio, List<Weapon> weaponLineup, List<Double> weaponRatio) {}
	
	@MessageMapping("/gacha/count/change")
	public void changeCount(@Payload ChangeId changeId) {
		selectGacha.setGachaCount(changeId.id);
	}
	
	record ChangeId(int id) {}
	
	@MessageMapping("/gacha/timer/stop")
	public void endTimer() {
		timerStop();
		autoRotate.timerStop();
	}
}