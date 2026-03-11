package com.example.catastrophewar.itemget;

import java.awt.Point;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

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
	
	ItemGetPage(ScheduledExecutorService scheduler){
		super(scheduler);
		autoRotate = createAutoRotate(scheduler);
		handleMotion = createHandleMotion(scheduler);
	}
	
	AutoRotate createAutoRotate(ScheduledExecutorService scheduler) {
		return new AutoRotate(scheduler);
	}
	
	HandleMotion createHandleMotion(ScheduledExecutorService scheduler) {
		return new HandleMotion(scheduler);
	}
	
	@MessageMapping("/gacha/images")
	void sendImage() {
		messaging.convertAndSend("/topic/gacha/images", createImageLink());
	}
	
	Image createImageLink() {
		OtherData otherData = createOtherData();
		return new Image(ImageLink.normalCoreLinkStream().toList(),
				ImageLink.normalWeaponLinkStream().toList(),
				otherData.getBall(),
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
			String ballImageLink, 
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
		return new State(autoRotate.getAngle(), !handleMotion.isRunning(), handleMotion.getAngle());
	}
	
	record State(double turnAngle, boolean isTurning, double handleAngle) {}
	
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