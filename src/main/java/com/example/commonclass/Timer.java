package com.example.commonclass;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class Timer {
	private final ScheduledExecutorService scheduler;
	private ScheduledFuture<?> future;
	
	public Timer(ScheduledExecutorService scheduler) {
		this.scheduler = scheduler;
	}
	
	/**
	 * 約60fpsで画面の更新を行う。
	 * @param task - {@link org.springframework.messaging.simp.SimpMessagingTemplate SimpMessagingTemplate}でデータ送信を行うメソッド
	 */
	public void timerStart(Runnable task) {
		future = repaintFuture(task);
	}
	
	ScheduledFuture<?> repaintFuture(Runnable task){
		return scheduler.scheduleAtFixedRate(task, 0, 17, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 起動中のタイマーがあれば停止する。
	 */
	public void timerStop() {
		if(future != null) {
			future.cancel(true);
			future = null;
		}
	}
}