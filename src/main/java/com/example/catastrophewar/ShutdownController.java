package com.example.catastrophewar;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutdownController {
	@Autowired
	private ConfigurableApplicationContext context;
	
	@PostMapping("/api/shutdown")
	public void shutdown() {
		CompletableFuture.runAsync(this::shutdownNow);
	}
	
	void shutdownNow(){
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SpringApplication.exit(context, () -> 0);
		System.exit(0);
	}
}