package com.example.catastrophewar.toppage;

import java.util.concurrent.ScheduledExecutorService;

import com.example.commonclass.Timer;

class FinalMotion extends Timer implements CorePosition{
	private final int number;
	private final int x;
	private int y = 340;
	private int count;
	private final int MAX_COUNT = 10;
	private final int COLUMN = 5;
	private final int CRRECTION_X = 350;
	private final int BASE_X = 100;
	private final int BASE_RISING = 10;
	private final int DELAY = 50;
	
	FinalMotion(ScheduledExecutorService scheduler, int number) {
		super(scheduler);
		this.number = number;
		x = CRRECTION_X + BASE_X * (number % COLUMN);
	}
	
	@Override
	protected int interval() {
		return DELAY;
	}
	
	void timerStart() {
		timerStart(this::timerProcess);
	}
	
	void timerProcess() {
		y -= BASE_RISING * (number / COLUMN);
		count++;
		if(MAX_COUNT < count) {
			timerStop();
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