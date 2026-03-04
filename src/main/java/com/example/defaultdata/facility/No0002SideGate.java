package com.example.defaultdata.facility;

import java.util.Arrays;
import java.util.List;

public class No0002SideGate extends No0001FrontGate{
	@Override
	public List<String> getActionImageName() {
		return Arrays.asList("/image/facility/side gate.png");
	}
}