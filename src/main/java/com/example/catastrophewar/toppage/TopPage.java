package com.example.catastrophewar.toppage;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.commonclass.ImageLink;
import com.example.commonclass.Timer;
import com.example.defaultdata.Core;
import com.example.defaultdata.other.OtherData;

@Controller
public class TopPage extends Timer{
	@Autowired
	private SimpMessagingTemplate messaging;
	
	private final FallMotion[] fallMotion;
	private final FinalMotion[] finalMotion;
	private final MainTimer mainTimer;
	private final List<Integer> randamList;
	private final int NUMBER = 20;
	
	TopPage(ScheduledExecutorService scheduler){
		super(scheduler);
		fallMotion = createFallMotion();
		finalMotion = createFinalMotion();
		mainTimer = createMainTimer(scheduler);
		randamList = createRandamList();
	}
	
	FallMotion[] createFallMotion(){
		return IntStream.range(0, NUMBER).mapToObj(_ -> new FallMotion()).toArray(FallMotion[]::new);
	}
	
	FinalMotion[] createFinalMotion() {
		return IntStream.range(0, NUMBER).mapToObj(i -> new FinalMotion(i)).toArray(FinalMotion[]::new);
	}
	
	MainTimer createMainTimer(ScheduledExecutorService scheduler){
		return new MainTimer(this, scheduler, fallMotion, finalMotion);
	}
	
	List<Integer> createRandamList(){
		var random = createRandom();
		return IntStream.range(0, NUMBER).mapToObj(_ -> random.nextInt(Core.values().length)).toList();
	}
	
	Random createRandom() {
		return new Random();
	}
	
	@MessageMapping("/top/images")
	void sendImage() {
		messaging.convertAndSend("/topic/top/images", createImageLinkList());
	}
	
	List<String> createImageLinkList(){
		return Stream.concat(titleStream(), createCoreLinkList()).toList();
	}
	
	Stream<String> titleStream(){
		return Stream.of(OtherData.TITLE);
	}
	
	Stream<String> createCoreLinkList(){
		return ImageLink.normalCoreLinkStream();
	}
	
	@MessageMapping("/top/timer/start")
	void timerStart() {
		if(isRunning()) {
			return;
		}
		timerStart(this::repaint);
		mainTimer.timerStart();
	}
	
	void repaint() {
		messaging.convertAndSend("/topic/top/repaint", createState());
	}
	
	State createState(){
		return new State(IntStream.range(0, NUMBER).mapToObj(this::createCoreState).toList(), mainTimer.isEndedFallMotion());
	}
	
	record State(List<CoreState> state, boolean isEnded) {}
	
	CoreState createCoreState(int number) {
		return new CoreState(randamList.get(number), getData(number, CorePosition::getX), getData(number, CorePosition::getY), getData(number, CorePosition::getAngle));
	}
	
	record CoreState(int id, int x, int y, double angle) {}
	
	<T> T getData(int number, Function<CorePosition, T> task) {
		return mainTimer.isEndedFallMotion()? task.apply(finalMotion[number]): task.apply(fallMotion[number]);
	}
	
	void endTimer() {
		timerStop();
	}
}