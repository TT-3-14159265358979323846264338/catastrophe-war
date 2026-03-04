package com.example.catastrophewar.toppage;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class FinalMotion implements CorePosition{
	private ScheduledFuture<?> finalFuture;
	private final int number;
	private final int x;
	private int y = 300;
	private int count;
	private final int MAX_COUNT = 10;
	private final int COLUMN = 5;
	private final int BASE_X = 100;
	private final int BASE_RISING = 10;
	private final int DELAY = 50;
	
	FinalMotion(int number) {
		this.number = number;
		x = BASE_X * (number % COLUMN);
	}
	
	void finalTimerStart(ScheduledExecutorService scheduler) {
		finalFuture = createFinalFuture(scheduler);
	}
	
	ScheduledFuture<?> createFinalFuture(ScheduledExecutorService scheduler){
		return scheduler.scheduleAtFixedRate(this::finalTimerProcess, 0, DELAY, TimeUnit.MILLISECONDS);
	}
	
	void finalTimerProcess() {
		y -= BASE_RISING * (number / COLUMN);
		count++;
		timerStop();
	}
	
	void timerStop() {
		if(MAX_COUNT < count) {
			finalFuture.cancel(true);
			finalFuture = null;
		}
	}
	
	@Override
	public double getAngle() {
		return 0;
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getY() {
		return y;
	}
}