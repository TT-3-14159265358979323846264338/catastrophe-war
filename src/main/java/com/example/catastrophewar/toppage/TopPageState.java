package com.example.catastrophewar.toppage;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.example.commonclass.Timer;
import com.example.defaultdata.Core;

public class TopPageState extends Timer{
	private final FallMotion[] fallMotion;
	private final FinalMotion[] finalMotion;
	private final MainTimer mainTimer;
	private final List<Integer> randamList;
	private final int NUMBER = 20;
	
	public TopPageState(ScheduledExecutorService scheduler){
		super(scheduler);
		fallMotion = createFallMotion(scheduler);
		finalMotion = createFinalMotion(scheduler);
		mainTimer = createMainTimer(scheduler);
		randamList = createRandamList();
	}
	
	FallMotion[] createFallMotion(ScheduledExecutorService scheduler){
		return IntStream.range(0, NUMBER).mapToObj(_ -> new FallMotion(scheduler)).toArray(FallMotion[]::new);
	}
	
	FinalMotion[] createFinalMotion(ScheduledExecutorService scheduler) {
		return IntStream.range(0, NUMBER).mapToObj(i -> new FinalMotion(scheduler, i)).toArray(FinalMotion[]::new);
	}
	
	MainTimer createMainTimer(ScheduledExecutorService scheduler){
		return new MainTimer(scheduler, this, fallMotion, finalMotion);
	}
	
	List<Integer> createRandamList(){
		var random = createRandom();
		return IntStream.range(0, NUMBER).mapToObj(_ -> random.nextInt(Core.values().length)).toList();
	}
	
	Random createRandom() {
		return new Random();
	}
	
	void drawTimerStart(Runnable task) {
		if(isRunning()) {
			return;
		}
		timerStart(task);
		mainTimer.timerStart();
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
}