package com.example.catastrophewar.toppage;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.example.commonclass.ImageLink;
import com.example.commonclass.Timer;
import com.example.defaultdata.Core;
import com.example.defaultdata.other.OtherData;

@Component
public class TopPage extends Timer{
	private final SimpMessagingTemplate messaging;
	private final FallMotion[] fallMotion;
	private final FinalMotion[] finalMotion;
	private final MainTimer mainTimer;
	private final List<Integer> randamList;
	private final int NUMBER = 20;
	
	TopPage(ScheduledExecutorService scheduler, SimpMessagingTemplate messaging){
		super(scheduler);
		this.messaging = messaging;
		fallMotion = createFallMotion();
		finalMotion = createFinalMotion();
		mainTimer = createMainTimer(scheduler);
		randamList = createRandamList();
		timerStart(this::repaint);
	}
	
	FallMotion[] createFallMotion(){
		return IntStream.range(0, NUMBER).mapToObj(_ -> new FallMotion()).toArray(FallMotion[]::new);
	}
	
	FinalMotion[] createFinalMotion() {
		return IntStream.range(0, NUMBER).mapToObj(i -> new FinalMotion(i)).toArray(FinalMotion[]::new);
	}
	
	MainTimer createMainTimer(ScheduledExecutorService scheduler){
		return new MainTimer(scheduler, fallMotion, finalMotion);
	}
	
	List<Integer> createRandamList(){
		var random = createRandom();
		return IntStream.range(0, NUMBER).mapToObj(_ -> random.nextInt(Core.values().length)).toList();
	}
	
	Random createRandom() {
		return new Random();
	}
	
	void repaint() {
		messaging.convertAndSend("/topic/topPage", createState());
	}
	
	State createState(){
		return new State(IntStream.range(0, NUMBER).mapToObj(this::createCoreState).toList());
	}
	
	record State(List<CoreState> state) {}
	
	CoreState createCoreState(int number) {
		return new CoreState(randamList.get(number), getData(number, CorePosition::getX), getData(number, CorePosition::getY), getData(number, CorePosition::getAngle));
	}
	
	record CoreState(int id, int x, int y, double angle) {}
	
	<T> T getData(int number, Function<CorePosition, T> task) {
		return mainTimer.isEnd()? task.apply(finalMotion[number]): task.apply(fallMotion[number]);
	}
	
	@MessageMapping("/app/requestImages")
	void sendImage() {
		messaging.convertAndSend("/topic/topImage", createImageLinkList());
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
}