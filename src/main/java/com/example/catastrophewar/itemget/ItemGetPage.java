package com.example.catastrophewar.itemget;

import java.awt.Point;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.commonclass.ImageLink;
import com.example.commonclass.Timer;
import com.example.defaultdata.DefaultEnum;
import com.example.defaultdata.GachaCount;
import com.example.defaultdata.other.OtherData;

@Controller
public class ItemGetPage extends Timer{
	@Autowired
	private SimpMessagingTemplate messaging;
	
	private final AutoRotate autoRotate;
	private final HandleMotion handleMotion;
	private final FallBallMotion fallBallMotion;
	private final OpenBallMotion openBallMotion;
	private GachaCount gachaCount;
	
	ItemGetPage(ScheduledExecutorService scheduler){
		super(scheduler);
		autoRotate = createAutoRotate(scheduler);
		openBallMotion = createOpenBallMotion(scheduler);
		fallBallMotion = createfallBallMotion(scheduler);
		handleMotion = createHandleMotion(scheduler);
		gachaCount = GachaCount.TEN;
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
	
	@MessageMapping("/gacha/data")
	void sendImage() {
		messaging.convertAndSend("/topic/gacha/data", createData());
	}
	
	GachaData createData(){
		return new GachaData(createGachaCount(), gachaCount.getId(), createImageLink());
	}
	
	record GachaData(List<String> gachaCount, int id, GachaImageLink links) {};
	
	List<String> createGachaCount(){
		return Stream.of(GachaCount.values()).map(this::gachaCountComment).toList();
	}
	
	String gachaCountComment(GachaCount gachaCount) {
		return String.format("%d連ガチャ\n%d枚", gachaCount.getLabel(), gachaCount.getUsedMedal());
	}
	
	GachaImageLink createImageLink() {
		OtherData otherData = createOtherData();
		return new GachaImageLink(ImageLink.normalCoreLinkStream().toList(),
				ImageLink.normalWeaponLinkStream().toList(),
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
	void timerStart() {
		timerStart(this::repaint);
		autoRotate.timerStart();
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
		return !isPlayingGacha();
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
	
	void endGacha() {
		messaging.convertAndSend("/topic/gacha/end", "");
	}
	
	@MessageMapping("/gacha/mouse/pressed")
	void mousePressed(@Payload ClickPoint clickPoint) {
		handleMotion.mousePressed(clickPoint.x, clickPoint.y);
	}
	
	@MessageMapping("/gacha/mouse/dragged")
	void mouseDragged(@Payload ClickPoint clickPoint) {
		handleMotion.mouseDragged(clickPoint.x, clickPoint.y);
	}
	
	@MessageMapping("/gacha/mouse/released")
	void mouseReleased() {
		handleMotion.mouseReleased();
	}
	
	record ClickPoint(int x, int y) {}
	
	@MessageMapping("/gacha/count/change")
	void changeCount(@Payload ChangeId changeId) {
		gachaCount = DefaultEnum.getEnum(GachaCount.values(), changeId.id);
	}
	
	record ChangeId(int id) {}
	
	
	
	
	
	
	
	
	
	
	@MessageMapping("/gacha/timer/stop")
	void endTimer() {
		timerStop();
		autoRotate.timerStop();
	}
}