package com.example.catastrophewar;

import java.util.concurrent.ScheduledExecutorService;

import com.example.catastrophewar.itemget.ItemGetPageState;
import com.example.catastrophewar.toppage.TopPageState;
import com.example.commonclass.Timer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SessionState {
	private final ScheduledExecutorService scheduler;
	private final String sessionId;
	private TopPageState topPageState;
	private ItemGetPageState itemGetPageState;
	
	void allTimerStop() {
		timerStop(topPageState);
		timerStop(itemGetPageState);
	}
	
	<T extends Timer> void timerStop(T state){
		if(state != null) {
			state.timerStop();
		}
	}

	public TopPageState getTopPageState() {
		return topPageState;
	}

	public void setTopPageState() {
		if(topPageState == null) {
			topPageState = createTopPageState();
		}
	}
	
	TopPageState createTopPageState() {
		return new TopPageState(scheduler);
	}

	public ItemGetPageState getItemGetPageState() {
		return itemGetPageState;
	}

	public void setItemGetPageState() {
		if(itemGetPageState == null) {
			itemGetPageState = createItemGetPageState();
		}
	}
	
	ItemGetPageState createItemGetPageState() {
		return new ItemGetPageState(scheduler, sessionId);
	}
}