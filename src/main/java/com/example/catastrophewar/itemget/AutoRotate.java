package com.example.catastrophewar.itemget;

import java.util.concurrent.ScheduledExecutorService;

import com.example.commonclass.Timer;

class AutoRotate extends Timer{
	private double angle;
	private final int DELAY = 50;
	
	AutoRotate(ScheduledExecutorService scheduler){
		super(scheduler);
	}
	
	@Override
	protected int interval() {
		return DELAY;
	}
	
	void timerStart(){
		timerStart(this::timerProcess);
	}
	
	void timerProcess() {
		angle += 0.03;
		if(Math.PI * 10000 < angle) {
			angle = 0;
		}
	}
	
	double getAngle() {
		return angle;
	}
}