package com.example.catastrophewar;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SessionController {
	private final ScheduledExecutorService scheduler;
	private final Map<String, SessionState> sessions = new ConcurrentHashMap<>();
	private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
	
	@EventListener
	public void sessionConnected(SessionConnectedEvent event) {
		operateSessions(event, (principal, headers) -> {
			String userName = principal.getName();
			SessionState newState = createSessionState(userName, headers.getSessionId());
			SessionState oldState = sessions.put(userName, newState);
			if (oldState != null) {
				oldState.allTimerStop();
			}
			Map<String, Object> attributes = headers.getSessionAttributes();
			if(attributes == null) {
				return;
			}
			String httpSessionId = (String) attributes.get("HTTP.SESSION.ID");
			Map<String, ? extends Session> userSessions = sessionRepository.findByPrincipalName(userName);
			for (Session session: userSessions.values()) {
				if (!session.getId().equals(httpSessionId)) {
					sessionRepository.deleteById(session.getId());
				}
			}
		});
	}
	
	SessionState createSessionState(String userName, String sessionId){
		return new SessionState(scheduler, userName, sessionId);
	}
	
	@EventListener
	public void sessionDisconnected(SessionDisconnectEvent event) {
		operateSessions(event, (principal, headers) -> {
			String userName = principal.getName();
			sessions.computeIfPresent(userName, (_, state) -> {
				if (state.getSessionId().equals(headers.getSessionId())) {
					state.allTimerStop();
					return null;
				}
				return state;
			});
		});
	}
	
	<T extends AbstractSubProtocolEvent> void operateSessions(T event, BiConsumer<Principal, SimpMessageHeaderAccessor> task) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		Principal principal = headers.getUser();
		if (principal != null) {
			task.accept(principal, headers);
		}
	}
	
	public SessionState getState(Principal principal, String sessionId) {
		return getState(principal.getName(), sessionId);
	}
	
	public SessionState getState(String userName, String sessionId) {
		return activateCheck(validityCheck(userName), sessionId);
	}
	
	SessionState validityCheck(String userName) {
		SessionState sessionState = sessions.get(userName);
		if(sessionState != null) {
			return sessionState;
		}
		throw new IllegalStateException("Userが見つかりません。" + userName);
	}
	
	SessionState activateCheck(SessionState state, String sessionId) {
		if(state.getSessionId().equals(sessionId)) {
			return state;
		}
		throw new IllegalStateException(sessionId + "は既に無効なIDです。");
	}
}