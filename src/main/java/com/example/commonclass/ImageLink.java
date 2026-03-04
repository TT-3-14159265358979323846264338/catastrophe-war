package com.example.commonclass;

import java.util.stream.Stream;

import com.example.defaultdata.Core;

public class ImageLink {
	public static Stream<String> normalCoreLinkStream(){
		return Stream.of(Core.values()).map(i -> i.getLabel().getImageName());
	}
}