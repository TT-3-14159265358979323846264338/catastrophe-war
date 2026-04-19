package com.example.catastrophewar.toppage;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catastrophewar.SessionController;
import com.example.catastrophewar.SessionState;
import com.example.commonclass.ImageLink;
import com.example.commonclass.Messaging;
import com.example.defaultdata.other.OtherData;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TopPage extends Messaging{
	private final SimpMessagingTemplate messaging;
	private final SessionController sessions;
	
	@GetMapping("/api/top/data")
	public TopImage sendImage() {
		return new TopImage(getTitle(), createCoreLinkList());
	}
	
	record TopImage(String title, List<String> core) {}
	
	String getTitle() {
		return new OtherData().getTitler();
	}
	
	List<String> createCoreLinkList(){
		return ImageLink.normalCoreLinkStream().toList();
	}
	
	@MessageMapping("/top/timer/start")
	public void timerStart(SimpMessageHeaderAccessor accessor) {
		String sessionId = accessor.getSessionId();
		SessionState state = sessions.getState(sessionId);
		state.setTopPageState();
		state.getTopPageState().drawTimerStart(() -> repaint(sessionId));
	}
	
	void repaint(String sessionId) {
		sendMessage(sessions, sessionId, state -> messaging.convertAndSendToUser(sessionId, "/queue/top/repaint", state.getTopPageState().createState(), headers(sessionId)));
	}
}