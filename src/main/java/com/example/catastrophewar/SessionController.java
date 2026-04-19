package com.example.catastrophewar;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SessionController {
	private final ScheduledExecutorService scheduler;
	private final Map<String, SessionState> sessions = new ConcurrentHashMap<>();
	
	@EventListener
	public void sessionConnected(SessionConnectedEvent event) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		sessions.computeIfAbsent(headers.getSessionId(), this::createSessionState);
	}
	
	SessionState createSessionState(String sessionId){
		return new SessionState(scheduler, sessionId);
	}
	
	@EventListener
	public void sessionDisconnected(SessionDisconnectEvent event) {
		SessionState state = sessions.remove(event.getSessionId());
		if (state != null) {
			state.allTimerStop();
		}
	}
	
	public SessionState getState(SimpMessageHeaderAccessor accessor) {
		return getState(accessor.getSessionId());
	}
	
	public SessionState getState(String sessionId) {
		return validityCheck(sessions.get(sessionId), sessionId);
	}
	
	SessionState validityCheck(SessionState sessionState, String sessionId) {
		if(sessionState != null) {
			return sessionState;
		}
		throw new IllegalStateException("SessionIdが見つかりません。" + sessionId);
	}
}