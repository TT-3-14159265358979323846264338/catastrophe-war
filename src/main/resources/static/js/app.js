const canvas = document.getElementById('mainCanvas');
const ctx = canvas.getContext('2d');
const socket = new SockJS('/ws-game');
const stompClient = Stomp.over(socket);
const titleImage = new Image();
const coreImages = [];

stompClient.connect({}, () => {
	stompClient.subscribe("/topic/topImage", (load) => {
		const list = JSON.parse(load.body);
		titleImage.src = list[0];
		for(let i = 1; i < list.length; i++){
			const image = new Image();
			image.src = list[i];
			coreImages.push(image);
		}
	});
	stompClient.subscribe('/topic/topPage', (load) => drawImage(JSON.parse(load.body).state));
	stompClient.send("/app/requestImages", {}, {});
});

function drawImage(state) {
	console.log("受信データ:", state);
	if(coreImages.length === 0){
		return;
	}
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	state.forEach(i => {
		const image = coreImages[i.id];
		ctx.save();
		ctx.translate(i.x, i.y);
		ctx.rotate(i.angle);
		ctx.drawImage(image, -image.width / 2, -image.height / 2);
		ctx.restore();
	});
}

window.addEventListener('pagehide', _ => {
	navigator.sendBeacon('/api/shutdown');
});