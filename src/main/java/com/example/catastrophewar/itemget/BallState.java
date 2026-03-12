package com.example.catastrophewar.itemget;

import java.awt.Point;

interface BallState {
	Point getTopPoint();
	Point getBottomPoint();
	double getTopAngle();
	double getBottomAngle();
}