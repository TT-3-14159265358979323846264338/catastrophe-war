window.addEventListener('pagehide', _ => {
	navigator.sendBeacon('/api/shutdown');
});