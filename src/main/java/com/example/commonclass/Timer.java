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
	 * 通常時は約60fpsで更新を行う。
	 * {@link #interval}をオーバーライドすることで更新間隔を変更できる。
	 * @param task - {@link org.springframework.messaging.simp.SimpMessagingTemplate SimpMessagingTemplate}でデータ送信を行うメソッド
	 */
	protected void timerStart(Runnable task) {
		if(isRunning()) {
			return;
		}
		future = repaintFuture(task);
	}
	
	ScheduledFuture<?> repaintFuture(Runnable task){
		return scheduler.scheduleAtFixedRate(task, 0, interval(), TimeUnit.MILLISECONDS);
	}
	
	/**
	 * タイマーの実行周期を指定する。
	 * @return - タイマーの実行周期 [milliseconds]。
	 */
	protected int interval() {
		return 1000 / 60;
	}
	
	/**
	 * 起動中のタイマーがあれば停止する。
	 */
	public void timerStop() {
		if(isRunning()) {
			future.cancel(true);
			future = null;
		}
	}
	
	/**
	 * このタイマーが起動中か確認する。
	 * @return - タイマーが起動していればtrueを返却する。
	 */
	public boolean isRunning() {
		return future != null;
	}
}