package com.example.catastrophewar.itemget;

import java.awt.Point;
import java.util.concurrent.ScheduledExecutorService;

import com.example.commonclass.Timer;

class OpenBallMotion extends Timer implements BallState{
	//private final HoldMedal holdMedal;
	//private final GachaInformation gachaInformation;
	private final ItemGetPage itemGetPage;
	private double bottomAngle;
	private double topAngle;
	private Point bottomPoint;
	private Point topPoint;
	private int color;
	private int expansion;
	private int count;
	private final int DELAY = 40;
	private final int DEFAULT = 0;
	private final int DEFAULT_EXPANSION = -250;
	private final double TOP_ANGLE_CHANGE = 0.;
	private final double BOTTOM_ANGLE_CHANGE = 0.1;
	private final int TOP_POSITION_CHANGE = 2;
	private final int BOTTOM_POSITION_CHANGE_X = 2;
	private final int BOTTOM_POSITION_CHANGE_Y = 1;
	private final int COLOR_CHANGE = 5;
	private final int EXPANSION_CHANGE = 20;
	private final int END_COUNT = 30;
	
	//OpenBallMotion(HoldMedal holdMedal, GachaInformation gachaInformation, ItemGetImage itemGetImage, ScheduledExecutorService scheduler) {
	OpenBallMotion(ItemGetPage itemGetPage, ScheduledExecutorService scheduler) {
		super(scheduler);
		//this.holdMedal = holdMedal;
		//this.gachaInformation = gachaInformation;
		this.itemGetPage = itemGetPage;
		reset();
	}
	
	void reset() {
		color = DEFAULT;
		expansion = DEFAULT_EXPANSION;
		count = 0;
	}
	
	@Override
	protected int interval() {
		return DELAY;
	}
	
	void timerStart(Point point, double angle) {
		topPoint = (Point) point.clone();
		bottomPoint = (Point) point.clone();
		topAngle = angle;
		bottomAngle = angle;
		timerStart(this::timerProcess);
	}
	
	void timerProcess(){
		topAngle += TOP_ANGLE_CHANGE;
		bottomAngle += BOTTOM_ANGLE_CHANGE;
		topPoint.x -= TOP_POSITION_CHANGE;
		topPoint.y += TOP_POSITION_CHANGE;
		bottomPoint.x += BOTTOM_POSITION_CHANGE_X;
		bottomPoint.y -= BOTTOM_POSITION_CHANGE_Y;
		color += COLOR_CHANGE;
		expansion += EXPANSION_CHANGE;
		count++;
		if(END_COUNT < count) {
			gacha();
			reset();
			itemGetPage.endGacha();
			timerStop();
		}
	}
	
	void gacha() {
		//new GachaResult(gachaInformation, holdMedal, itemGetImage);
	}

	@Override
	public Point getTopPoint() {
		return topPoint;
	}

	@Override
	public Point getBottomPoint() {
		return bottomPoint;
	}

	@Override
	public double getTopAngle() {
		return topAngle;
	}

	@Override
	public double getBottomAngle() {
		return bottomAngle;
	}
	
	int getColor() {
		return color;
	}
	
	int getExpansion() {
		return expansion;
	}
}