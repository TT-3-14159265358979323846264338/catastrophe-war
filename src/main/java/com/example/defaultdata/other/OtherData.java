package com.example.defaultdata.other;

import java.util.List;

import com.example.defaultdata.DefaultData;

//その他のデータ
public class OtherData {
	private static final String LINK = "/image/gacha/";
	
	protected String imageLink(String link) {
		return LINK + link + DefaultData.PNG;
	}
	
	//タイトル画像ファイル
	public String getTitler() {
		return imageLink("title");
	}

	//ガチャ画像ファイル
	public List<String> getHalfBall(){
		return List.of(imageLink("ball-bottom"), imageLink("ball-top"));
	}
	
	public String getHandle() {
		return imageLink("machine-handle");
	}
	
	public List<String> getMachine(){
		return List.of(imageLink("machine-bottom"), imageLink("machine-top"));
	}
	
	public String getTurn() {
		return imageLink("turn");
	}
	
	public String getEffect() {
		return imageLink("effect");
	}
}