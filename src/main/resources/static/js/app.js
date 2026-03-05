const canvas = document.getElementById('mainCanvas');
const ctx = canvas.getContext('2d');
const socket = new SockJS('/ws-game');
const stompClient = Stomp.over(socket);
const titleImage = new Image();
const coreImages = [];
const TITLE_X = 80;
const TITLE_Y = 100;

stompClient.connect({}, () => {
	stompClient.send("/app/top/images", {}, {});
	stompClient.subscribe("/topic/top/images", (load) => {
		const list = JSON.parse(load.body);
		titleImage.src = list[0];
		for(let i = 1; i < list.length; i++){
			const image = new Image();
			image.src = list[i];
			coreImages.push(image);
		}
		stompClient.send("/app/top/timer/start", {}, {});
	});
	stompClient.subscribe('/topic/top/repaint', (load) => drawImage(JSON.parse(load.body)));
});

function drawImage(data) {
	const {state, isEnded} = data;
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	state.forEach(i => {
		const image = coreImages[i.id];
		const width = image.width / 2
		const height = image.height / 2;
		ctx.save();
		ctx.translate(i.x + width, i.y + height);
		ctx.rotate(i.angle);
		ctx.drawImage(image, -width, -height);
		ctx.restore();
	});
	if(isEnded){
		ctx.drawImage(titleImage, TITLE_X, TITLE_Y);
	}
}

window.addEventListener('pagehide', _ => {
	navigator.sendBeacon('/api/shutdown');
});