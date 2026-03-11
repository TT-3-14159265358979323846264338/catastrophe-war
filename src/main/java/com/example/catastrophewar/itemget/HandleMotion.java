package com.example.catastrophewar.itemget;

import java.awt.Point;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiFunction;

import com.example.commonclass.Timer;

class HandleMotion extends Timer{
	//private final MenuItemGet MenuItemGet;
	//private final HoldMedal HoldMedal;
	//private final BallMotion BallMotion;
	private int startPointX;
	private int startPointY;
	private int activePointX;
	private int activePointY;
	private double angle;
	private final int CENTER_X = 389;
	private final int CENTER_Y = 377;
	private final double RIGHT_ANGLE = Math.PI / 2.0;
	private final double AROUND = Math.PI * 2;
	private final int DELAY = 20;
	
	//HandleMotion(MenuItemGet menuItemGet, HoldMedal holdMedal, BallMotion ballMotion, ScheduledExecutorService scheduler) {
	HandleMotion(ScheduledExecutorService scheduler) {
		super(scheduler);
		//this.MenuItemGet = menuItemGet;
		//this.HoldMedal = holdMedal;
		//this.BallMotion = ballMotion;
	}
	
	double getAngle() {
		if(isRunning()) {
			return angle;
		}
		if(startPointX == 0) {
			return 0;
		}
		angle = manualAngle();
		return angle;
	}
	
	double manualAngle() {
		//x, y は 角度を求めたい点, 目標点1, 目標点2 の位置順
		//角度は余弦定理から算出
		double[] x = {CENTER_X, activePointX, startPointX};
		double[] y = {CENTER_Y, activePointY, startPointY};
		double[] distance = new double[3];
		BiFunction<Double, Double, Double> pow = (x1, x2) -> {
			return Math.pow(x1 - x2, 2);
		};
		BiFunction<Integer, Integer, Double> sqrt = (i, j) -> {
			return Math.sqrt(pow.apply(x[i], x[j]) + pow.apply(y[i], y[j]));
		};
		distance[0] = sqrt.apply(1, 2);
		distance[1] = sqrt.apply(0, 2);
		distance[2] = sqrt.apply(0, 1);
		return Math.acos((Math.pow(distance[0], 2) - Math.pow(distance[1], 2) - Math.pow(distance[2], 2)) / (-2 * distance[1] * distance[2]));
	}
	
	void mousePressed(Point point) {
		startPointX = point.x;
		startPointY = point.y;
		activePointX = point.x;
		activePointY = point.y;
	}
	
	void mouseDragged(Point point) {
		if(isRunning()) {
			return;
		}
		//if(HoldMedal.canPossessMedal()) {
			activePointX = point.x;
			activePointY = point.y;
			if(RIGHT_ANGLE < angle) {
				autoTurnStart();
			}
		//}
	}
	
	void mouseReleased() {
		reset();
	}
	
	@Override
	protected int interval() {
		return DELAY;
	}
	
	void autoTurnStart() {
		//MenuItemGet.deactivatePanel();
		timerStart(this::handleFutureProcess);
	}
	
	void handleFutureProcess() {
		angle += 0.1;
		if(AROUND < angle) {
			autoTurnStop();
		}
	}
	
	void autoTurnStop() {
		timerStop();
		//BallMotion.timerStart(this);
		angle = 0;
		reset();
	}
	
	void reset() {
		startPointX = 0;
		startPointY = 0;
		activePointX = 0;
		activePointY = 0;
	}
}