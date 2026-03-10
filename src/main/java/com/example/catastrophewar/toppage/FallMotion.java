package com.example.catastrophewar.toppage;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

import com.example.commonclass.Timer;

class FallMotion extends Timer implements CorePosition{
	private double angle;
	private final int x;
	private int y = -300;
	private final int MAX_X = 1050;
	private final double STEP_SIZE = 100.0;
	private final int MAX_ANGLE = (int) (Math.PI * 2 * STEP_SIZE);
	private final double ANGLE_CHANGE = 0.1;
	private final int COODINATE_CHANGE = 10;
	private final int FINAL_COODINATE = 600;
	private final int DELAY = 20;
	
	FallMotion(ScheduledExecutorService scheduler){
		super(scheduler);
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
	
	@Override
	protected int interval() {
		return DELAY;
	}
	
	void timerStart() {
		timerStart(this::timerProcess);
	}
	
	void timerProcess() {
		angle += ANGLE_CHANGE;
		y += COODINATE_CHANGE;
		if(FINAL_COODINATE < y) {
			timerStop();
		}
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