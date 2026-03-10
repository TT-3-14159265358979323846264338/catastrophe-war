package com.example.commonclass;

import java.util.stream.Stream;

import com.example.defaultdata.Core;
import com.example.defaultdata.DefaultEnum;
import com.example.defaultdata.Weapon;

public class ImageLink {
	public static Stream<String> normalCoreLinkStream(){
		return labelStream(Core.values()).map(i -> i.getImageName());
	}
	
	public static Stream<String> normalWeaponLinkStream(){
		return labelStream(Weapon.values()).map(i -> i.getImageName());
	}
	
	public static <T extends DefaultEnum<U>, U> Stream<U> labelStream(T[] data){
		return Stream.of(data).map(i -> i.getLabel());
	}
}