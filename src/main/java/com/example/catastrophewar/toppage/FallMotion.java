package com.example.catastrophewar.toppage;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class FallMotion implements CorePosition{
	private ScheduledFuture<?> fallFuture;
	private double angle;
	private final int x;
	private int y = -300;
	private boolean canStart;
	private final int MAX_X = 1050;
	private final double STEP_SIZE = 100.0;
	private final int MAX_ANGLE = (int) (Math.PI * 2 * STEP_SIZE);
	private final double ANGLE_CHANGE = 0.1;
	private final int COODINATE_CHANGE = 10;
	private final int FINAL_COODINATE = 800;
	private final int DELAY = 20;
	
	FallMotion(){
		angle = randomAngle();
		x = randomX();
	}
	
	double randomAngle() {
		return createRandom().nextInt(MAX_ANGLE) / STEP_SIZE;
	}
	
	int randomX() {
		return createRandom().nextInt(MAX_X);
	}
	
	Random createRandom() {
		return new Random();
	}
	
	void fallTimerStart(ScheduledExecutorService scheduler) {
		fallFuture = createFallFuture(scheduler);
		canStart = true;
	}
	
	ScheduledFuture<?> createFallFuture(ScheduledExecutorService scheduler){
		return scheduler.scheduleAtFixedRate(this::fallTimerProcess, 0, DELAY, TimeUnit.MILLISECONDS);
	}
	
	void fallTimerProcess() {
		angle += ANGLE_CHANGE;
		y += COODINATE_CHANGE;
		timerStop();
	}
	
	void timerStop() {
		if(FINAL_COODINATE < y) {
			canStart = false;
			fallFuture.cancel(true);
			fallFuture = null;
		}
	}
	
	boolean canStart() {
		return canStart;
	}
	
	@Override
	public double getAngle() {
		return angle;
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