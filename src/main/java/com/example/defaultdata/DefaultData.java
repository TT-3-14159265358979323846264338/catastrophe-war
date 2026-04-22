package com.example.defaultdata;

import java.util.List;

public interface DefaultData {
	public static final String PNG = ".png";
	public String getName();
	public String getExplanation();
	public String getImageName();
	public List<?> getWeaponStatus();
	public List<?> getUnitStatus();
	public List<?> getCutStatus();
	public List<List<Double>> getBuff();
}