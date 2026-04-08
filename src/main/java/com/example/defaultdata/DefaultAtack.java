package com.example.defaultdata;

import java.util.List;

public interface DefaultAtack {
	public String getBulletImageName();
	public List<String> getHitImageName();
	public List<Element> getElement();
	public int getAtackPattern();
}