const canvas = document.getElementById('mainCanvas');
const ctx = canvas.getContext('2d');
const socket = new SockJS('/ws-game');
const stompClient = Stomp.over(socket);
const titleImage = new Image();
const coreImages = [];
const TITLE_X = 80;
const TITLE_Y = 40;

stompClient.connect({}, () => {
	stompClient.send("/app/top/images", {}, {});
	stompClient.subscribe("/topic/top/images", inputImage);
	stompClient.subscribe('/topic/top/repaint', drawImage);
});

function inputImage(data){
	const imageList = JSON.parse(data.body);
	titleImage.src = imageList[0];
	for(let i = 1; i < imageList.length; i++){
		const image = new Image();
		image.src = imageList[i];
		coreImages.push(image);
	}
	stompClient.send("/app/top/timer/start", {}, {});
}

function drawImage(data) {
	const {state, isEnded} = JSON.parse(data.body);
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	state.forEach(draw);
	if(isEnded){
		ctx.drawImage(titleImage, TITLE_X, TITLE_Y);
	}
}

function draw(coreState){
	const image = coreImages[coreState.id];
	const width = image.width / 2
	const height = image.height / 2;
	ctx.save();
	ctx.translate(coreState.x + width, coreState.y + height);
	ctx.rotate(coreState.angle);
	ctx.drawImage(image, -width, -height);
	ctx.restore();
}

document.addEventListener('DOMContentLoaded', () => {
	const gachaButton = document.getElementById("gacha");
	const recycleButton = document.getElementById("recycle");
	const compositionButton = document.getElementById("composition");
	const stageButton = document.getElementById("stage");
	gachaButton.addEventListener('click', gachaButtonAction);
	recycleButton.addEventListener('click', recycleButtonAction);
	compositionButton.addEventListener('click', compositionButtonAction);
	stageButton.addEventListener('click', stageButtonAction);
});

function gachaButtonAction(_){
	
}

function recycleButtonAction(_){
	
}

function compositionButtonAction(_){
	
}

function stageButtonAction(_){
	
}

window.addEventListener('pagehide', _ => {
	navigator.sendBeacon('/api/shutdown');
});