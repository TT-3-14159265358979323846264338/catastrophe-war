package com.example.catastrophewar;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.commonclass.Messaging;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SessionController extends Messaging{
	private final ScheduledExecutorService scheduler;
	private final SimpMessagingTemplate messaging;
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
				doubleLoginHandling(userName, oldState.getSessionId());
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
	
	void doubleLoginHandling(String userName, String sessionId) {
		messaging.convertAndSendToUser(userName, "/queue/error/double/login", "", headers(sessionId));
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
		return activateCheck(principal.getName(), sessionId);
	}
	
	public SessionState getState(String userName, String sessionId) {
		return activateCheck(userName, sessionId);
	}
	
	SessionState activateCheck(String userName, String sessionId) {
		SessionState state = sessions.get(userName);
		if(state == null) {
			throw new IllegalStateException("Userが見つかりません。" + userName);
		}
		if(state.getSessionId().equals(sessionId)) {
			return state;
		}
		doubleLoginHandling(userName, sessionId);
		throw new IllegalStateException(userName + "の" + sessionId + "は既に無効なIDです。");
	}
}