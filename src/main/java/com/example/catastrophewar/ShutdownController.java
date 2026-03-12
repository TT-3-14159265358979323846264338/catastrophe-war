package com.example.catastrophewar;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
public class ShutdownController{
	@Autowired
	private ConfigurableApplicationContext context;
	
	private volatile long time;
	private final int DELAY = 30000;
	private boolean hasShutDown;
	
	ShutdownController(){
		time = System.currentTimeMillis();
	}
	
	@PostMapping("/api/heartbeat")
	void heartbeat() {
		time = System.currentTimeMillis();
	}
	
	@Scheduled(fixedRate = DELAY)
	void timerProcess() {
		if(DELAY < System.currentTimeMillis() - time) {
			shutdown();
		}
	}
	
	@PostMapping("/api/shutdown")
	public void shutdown() {
		CompletableFuture.runAsync(this::shutdownNow);
	}
	
	synchronized void shutdownNow(){
		if(hasShutDown) {
			return;
		}
		hasShutDown = true;
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SpringApplication.exit(context, () -> 0);
		System.exit(0);
	}
}