package com.example.catastrophewar;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.catastrophewar.toppage.TopPageState;

@Controller
public class AllSessionControl {
	private final Map<String, TopPageState> sessions = new ConcurrentHashMap<>();
	
	@EventListener
	public void removeSessions(SessionDisconnectEvent event) {
		TopPageState state = sessions.remove(event.getSessionId());
		if (state != null) {
			state.timerStop();
		}
	}
}