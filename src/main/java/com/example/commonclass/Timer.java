package com.example.commonclass;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class Timer {
	private final ScheduledExecutorService scheduler;
	private ScheduledFuture<?> future;
	
	protected Timer(ScheduledExecutorService scheduler) {
		this.scheduler = scheduler;
	}
	
	/**
	 * 約60fpsで画面の更新を行う。
	 * @param task - {@link org.springframework.messaging.simp.SimpMessagingTemplate SimpMessagingTemplate}でデータ送信を行うメソッド
	 */
	protected void timerStart(Runnable task) {
		if(isRunning()) {
			return;
		}
		future = repaintFuture(task);
	}
	
	ScheduledFuture<?> repaintFuture(Runnable task){
		return scheduler.scheduleAtFixedRate(task, 0, 17, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 起動中のタイマーがあれば停止する。
	 */
	protected void timerStop() {
		if(isRunning()) {
			future.cancel(true);
			future = null;
		}
	}
	
	protected boolean isRunning() {
		return future != null;
	}
}