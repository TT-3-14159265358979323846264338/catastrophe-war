package com.example.catastrophewar.displaystatus;

import java.util.List;
import java.util.stream.Stream;

import com.example.defaultdata.DefaultEnum;
import com.example.defaultdata.Element;

abstract class ElementStatus {
	abstract public Status sendStatus(int id);
	
	protected record Status(String name,
			List<?> weaponElement, 
			List<?> leftWeaponStatus, 
			List<?> rightWeaponStatus,
			List<?> unitElement, 
			List<?> unitStatus, 
			List<String> cutElement, 
			List<String> cutStatus) {}
	
	protected <U extends DefaultEnum<String>> List<String> defaultLabel(U[] elements){
		return defaultLabelStream(elements).toList();
	}
	
	protected <U extends DefaultEnum<String>> Stream<String> defaultLabelStream(U[] elements){
		return Stream.of(elements).map(DefaultEnum::getLabel);
	}
	
	protected List<String> cutLabel() {
		return Stream.of(Element.values()).map(this::cutLabel).toList();
	}
	
	String cutLabel(Element element) {
		String label = element.getLabel();
		return element == Element.SUPPORT? label + "倍率": label + "耐性";
	}
	
	protected List<String> addPercent(List<?> elements){
		return elements.stream().map(i -> i + "%").toList();
	}
	
	protected Stream<String> unitWeaponType(){
		return Stream.of("距離タイプ", "属性", "ターゲット");
	}
	
	protected Stream<String> facilityWeaponType(){
		return Stream.of("属性", "ターゲット");
	}
	
	protected Stream<String> enemyWeaponType(){
		return Stream.of("移動タイプ", "種別", "属性", "ターゲット");
	}
	
	protected String getElement(List<Element> elementList) {
		if(elementList.isEmpty()) {
			return "なし";
		}
		String element = "";
		for(Element i: elementList) {
			element += i.getLabel() + ", ";
		}
		return element.substring(0, element.length() - 2);
	}
	
	
	
	
	
	
	
}