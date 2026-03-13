package com.example.catastrophewar.itemget;

import java.awt.Point;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.commonclass.ImageLink;
import com.example.commonclass.Timer;
import com.example.defaultdata.other.OtherData;

@Controller
public class ItemGetPage extends Timer{
	@Autowired
	private SimpMessagingTemplate messaging;
	
	private final AutoRotate autoRotate;
	private final HandleMotion handleMotion;
	private final FallBallMotion fallBallMotion;
	private final OpenBallMotion openBallMotion;
	
	ItemGetPage(ScheduledExecutorService scheduler){
		super(scheduler);
		autoRotate = createAutoRotate(scheduler);
		openBallMotion = createOpenBallMotion(scheduler);
		fallBallMotion = createfallBallMotion(scheduler);
		handleMotion = createHandleMotion(scheduler);
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
	
	@MessageMapping("/gacha/images")
	void sendImage() {
		messaging.convertAndSend("/topic/gacha/images", createImageLink());
	}
	
	Image createImageLink() {
		OtherData otherData = createOtherData();
		return new Image(ImageLink.normalCoreLinkStream().toList(),
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
	
	record Image(List<String> coreImageLink, 
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
	void mousePressed(Point point) {
		handleMotion.mousePressed(point);
	}
	
	@MessageMapping("/gacha/mouse/dragged")
	void mouseDragged(Point point) {
		handleMotion.mouseDragged(point);
	}
	
	@MessageMapping("/gacha/mouse/released")
	void mouseReleased() {
		handleMotion.mouseReleased();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@MessageMapping("/gacha/timer/stop")
	void endTimer() {
		timerStop();
		autoRotate.timerStop();
	}
}