package com.example.defaultdata.other;

import java.util.List;

//その他のデータ
public class OtherData {
	//タイトル画像ファイル
	public String getTitler() {
		return "/image/gacha/title.png";
	}

	//ガチャ画像ファイル
	public List<String> getHalfBall(){
		return List.of("/image/gacha/ball bottom.png", "/image/gacha/ball top.png");
	}
	
	public String getHandle() {
		return "/image/gacha/machine handle.png";
	}
	
	public List<String> getMachine(){
		return List.of("/image/gacha/machine bottom.png", "/image/gacha/machine top.png");
	}
	
	public String getTurn() {
		return "/image/gacha/turn.png";
	}
	
	public String getEffect() {
		return "/image/gacha/effect.png";
	}
}