package com.example.catastrophewar.toppage;

import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

import com.example.commonclass.Timer;

class MainTimer extends Timer{
	private final TopPageState topPageState;
	private final FallMotion[] fallMotion;
	private final FinalMotion[] finalMotion;
	private int count;
	private boolean isEndedFallMotion;
	private final int DELAY = 300;
	
	MainTimer(ScheduledExecutorService scheduler, TopPageState topPageState, FallMotion[] fallMotion, FinalMotion[] finalMotion){
		super(scheduler);
		this.topPageState = topPageState;
		this.fallMotion = fallMotion;
		this.finalMotion = finalMotion;
	}
	
	@Override
	protected int interval() {
		return DELAY;
	}
	
	void timerStart() {
		timerStart(this::timerProcess);
	}
	
	void timerProcess() {
		if(isEndedFallMotion) {
			if(Stream.of(finalMotion).noneMatch(FinalMotion::isRunning)) {
				topPageState.timerStop();
				timerStop();
			}
			return;
		}
		try {
			fallMotion[count].timerStart();
		}catch(Exception ignore) {
			//これ以上新たに表示する画像がないので無視
		}
		count++;
		if(Stream.of(fallMotion).noneMatch(FallMotion::isRunning)) {
			Stream.of(finalMotion).forEach(i -> i.timerStart());
			isEndedFallMotion = true;
		}
	}
	
	boolean isEndedFallMotion() {
		return isEndedFallMotion;
	}
}