package com.example.catastrophewar.toppage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.commonclass.ImageLink;
import com.example.defaultdata.other.OtherData;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TopPage{
	private final ScheduledExecutorService scheduler;
	private final SimpMessagingTemplate messaging;
	private final Map<String, TopPageState> sessions = new ConcurrentHashMap<>();
	
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
		TopPageState state = sessions.computeIfAbsent(sessionId, _ -> new TopPageState(scheduler));
		state.drawTimerStart(() -> repaint(sessionId));
	}
	
	void repaint(String sessionId) {
		TopPageState state = sessions.get(sessionId);
		if(state != null) {
			SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
	        headerAccessor.setSessionId(sessionId);
	        headerAccessor.setLeaveMutable(true);
			messaging.convertAndSendToUser(sessionId, "/queue/top/repaint", state.createState(), headerAccessor.getMessageHeaders());
		}
	}
	
	@EventListener
	public void removeSessions(SessionDisconnectEvent event) {
		TopPageState state = sessions.remove(event.getSessionId());
		if (state != null) {
			state.timerStop();
		}
	}
}