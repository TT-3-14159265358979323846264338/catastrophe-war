package com.example.catastrophewar.toppage;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

class MainTimer {
	private final TopPage topPage;
	private final ScheduledExecutorService scheduler;
	private ScheduledFuture<?> mainFuture;
	private final FallMotion[] fallMotion;
	private final FinalMotion[] finalMotion;
	private int count;
	private boolean isEndedFallMotion;
	private final int DELAY = 300;
	
	MainTimer(TopPage topPage, ScheduledExecutorService scheduler, FallMotion[] fallMotion, FinalMotion[] finalMotion){
		this.topPage = topPage;
		this.scheduler = scheduler;
		this.fallMotion = fallMotion;
		this.finalMotion = finalMotion;
	}
	
	void timerStart() {
		mainFuture = createEffectTimer();
	}
	
	ScheduledFuture<?> createEffectTimer() {
		return scheduler.scheduleAtFixedRate(this::effectTimerProcess, 0, DELAY, TimeUnit.MILLISECONDS);
	}
	
	void effectTimerProcess() {
		if(isEndedFallMotion) {
			if(Stream.of(finalMotion).anyMatch(FinalMotion::isEnded)) {
				topPage.endTimer();
				mainFuture.cancel(true);
				mainFuture = null;
			}
			return;
		}
		try {
			fallMotion[count].fallTimerStart(scheduler);
		}catch(Exception ignore) {
			//これ以上新たに表示する画像がないので無視
		}
		count++;
		if(Stream.of(fallMotion).noneMatch(FallMotion::isRunning)) {
			Stream.of(finalMotion).forEach(i -> i.finalTimerStart(scheduler));
			isEndedFallMotion = true;
		}
	}
	
	boolean isEndedFallMotion() {
		return isEndedFallMotion;
	}
}