package com.example.commonclass;

import java.util.function.Consumer;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;

import com.example.catastrophewar.SessionController;
import com.example.catastrophewar.SessionState;

public class Messaging {
	protected MessageHeaders headers(String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
	}
	
	protected void sendMessage(SessionController sessions, String sessionId, Consumer<SessionState> task) {
		SessionState state = sessions.getState(sessionId);
		if(state != null) {
			task.accept(state);
		}
	}
}