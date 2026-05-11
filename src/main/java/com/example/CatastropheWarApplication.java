package com.example;

import java.awt.Desktop;
import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class CatastropheWarApplication {
	public static void main(String[] args) {
		if (!isDocker()) {
			System.setProperty("java.awt.headless", "false");
		}
		SpringApplication.run(CatastropheWarApplication.class, args);
	}
	
	static boolean isDocker() {
		return "true".equals(System.getenv("IS_DOCKER"));
	}
	
	@EventListener(ApplicationReadyEvent.class)
	void runApplication() {
		if (isDocker()) {
			return;
		}
		try {
			Desktop desktop = createDesktop();
			String url = "http://localhost:8080/";
			desktop.browse(createURI(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	Desktop createDesktop() {
		return Desktop.getDesktop();
	}
	
	URI createURI(String url) throws Exception {
		return new URI(url);
	}
}