package com.example.commonclass;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import com.example.defaultdata.Core;
import com.example.defaultdata.DefaultData;
import com.example.defaultdata.DefaultEnum;
import com.example.defaultdata.Stage;
import com.example.defaultdata.Weapon;
import com.example.defaultdata.stage.StageData;

public class ImageLink {
	public static List<String> normalCoreLinkList(){
		return normalCoreLinkStream().toList();
	}
	
	public static List<String> normalWeaponLinkList(){
		return normalWeaponLinkStream().toList();
	}
	
	public static Stream<String> normalCoreLinkStream(){
		return imageLinkStream(Core.values());
	}
	
	public static Stream<String> normalWeaponLinkStream(){
		return imageLinkStream(Weapon.values());
	}
	
	public static <T extends DefaultEnum<U>, U extends DefaultData> Stream<String> imageLinkStream(T[] data){
		return DefaultEnum.getLabelStream(data).map(DefaultData::getImageName);
	}
	
	public static List<String> stageNameList(){
		return stageDataList(StageData::getName);
	}
	
	public static List<String> stageImageList(){
		return stageDataList(StageData::getImageName);
	}
	
	public static List<String> stageDataList(Function<StageData, String> task){
		return DefaultEnum.getLabelStream(Stage.values()).map(task).toList();
	}
}