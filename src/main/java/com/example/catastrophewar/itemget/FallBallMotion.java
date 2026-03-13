package com.example.catastrophewar.itemget;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import com.example.commonclass.Timer;

class FallBallMotion extends Timer implements BallState{
	private final OpenBallMotion openBallMotion;
	private double angle;
	private Point point;
	private final List<Integer> moveList = Arrays.asList(30, 45, 125, 75, 95);
	private final List<Integer> distanceList = Arrays.asList(3, 1, 2, -2, 1);
	private int moveNumber;
	private final Point defaultPoint = new Point(360, 407);
	private final int DELAY = 30;
	private final int DEFAULT = 0;
	private final double ANGLE_CHANGE = 0.2;
	
	FallBallMotion(OpenBallMotion openBallMotion, ScheduledExecutorService scheduler) {
		super(scheduler);
		this.openBallMotion = openBallMotion;
		reset();
	}
	
	void reset() {
		angle = DEFAULT;
		point = (Point) defaultPoint.clone();
		moveNumber = DEFAULT;
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
		point.y += moveDistance();
	}
	
	int moveDistance() {
		try {
			if(point.y == defaultPoint.y + moveList.get(moveNumber)) {
				moveNumber++;
			}
			return distanceList.get(moveNumber);
		}catch(Exception e) {
			openBallMotion.timerStart(point, angle);
			reset();
			timerStop();
			return DEFAULT;
		}
	}

	@Override
	public Point getTopPoint() {
		return point;
	}

	@Override
	public Point getBottomPoint() {
		return point;
	}

	@Override
	public double getTopAngle() {
		return angle;
	}

	@Override
	public double getBottomAngle() {
		return angle;
	}
}