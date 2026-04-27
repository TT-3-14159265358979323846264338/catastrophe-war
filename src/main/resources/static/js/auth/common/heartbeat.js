const INTERVAL = 25000;

export function heartbeat(){
	sendHeartbeat();
	setInterval(sendHeartbeat, INTERVAL);
}

function sendHeartbeat(){
	navigator.sendBeacon("/api/heartbeat");
}