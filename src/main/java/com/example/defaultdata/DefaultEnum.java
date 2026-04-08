package com.example.defaultdata;

import java.util.stream.Stream;

public interface DefaultEnum<T> {
	public int getId();
	public T getLabel();
	
	public static <T extends DefaultEnum<U>, U> Stream<U> getLabelStream(T[] data){
		return Stream.of(data).map(DefaultEnum::getLabel);
	}
	
	static <T, U extends Enum<U> & DefaultEnum<T>> T getLabel(U[] data, int id) {
		var selectEnum = getEnum(data, id);
		if(selectEnum != null) {
			return selectEnum.getLabel();
		}
		return null;
	}
	
	static <T, U extends Enum<U> & DefaultEnum<T>> U getEnum(U[] data, int id) {
		for(U i: data) {
			if(i.getId() == id) {
				return i;
			}
		}
		return null;
	}
}