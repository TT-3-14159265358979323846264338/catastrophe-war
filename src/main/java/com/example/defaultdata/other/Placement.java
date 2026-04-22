package com.example.defaultdata.other;

import java.util.List;

import com.example.defaultdata.DefaultData;

public class Placement {
	private static final String LINK = "/image/field/";
	
	protected String imageLink(String link) {
		return LINK + link + DefaultData.PNG;
	}
	
	public List<String> getPlacement(){
		return List.of(
				imageLink("near-placement"),
				imageLink("far-placement"),
				imageLink("all-placement")
				);
	}
}