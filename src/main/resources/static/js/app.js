import {gacha} from './gacha.js';
import {rotateDraw} from './editImage.js';

const canvas = document.getElementById('mainCanvas');
const ctx = canvas.getContext('2d');
const stompClient = Stomp.over(new SockJS('/ws-game'));
let inputSubscribe;
let timerSubscribe;
const topPage = document.querySelector('.toppage-item');
const gachaPage = document.querySelector('.gacha-item');
const titleImage = new Image();
const coreImages = [];
const TITLE_X = 80;
const TITLE_Y = 40;
const INTERVAL = 25000;

stompClient.connect({}, () => {
	inputSubscribe = stompClient.subscribe("/topic/top/images", initialize);
	stompClient.send("/app/top/images", {}, {});
});

function initialize(data){
	inputImage(data);
	postProcessing();
}

function inputImage(data){
	const imageList = JSON.parse(data.body);
	titleImage.src = imageList[0];
	for(let i = 1; i < imageList.length; i++){
		const image = new Image();
		image.src = imageList[i];
		coreImages.push(image);
	}
}

function postProcessing(){
	inputSubscribe.unsubscribe();
	inputSubscribe = null;
	topRepaintStart();
}

export function topRepaintStart(){
	topRepaintStop();
	timerSubscribe = stompClient.subscribe('/topic/top/repaint', drawImage);
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
	rotateDraw(ctx, image, coreState.x, coreState.y, coreState.angle);
}

document.addEventListener('DOMContentLoaded', () => {
	const gachaButton = document.getElementById("go-to-gacha-from-toppage");
	const recycleButton = document.getElementById("go-to-recycle-from-toppage");
	const compositionButton = document.getElementById("go-to-composition-from-toppage");
	const stageButton = document.getElementById("go-to-stage-from-toppage");
	gachaButton.addEventListener('click', gachaButtonAction);
	recycleButton.addEventListener('click', recycleButtonAction);
	compositionButton.addEventListener('click', compositionButtonAction);
	stageButton.addEventListener('click', stageButtonAction);
});

function gachaButtonAction(_){
	topPage.classList.add('hidden');
	gachaPage.classList.remove('hidden');
	gacha(stompClient);
	topRepaintStop();
}

function recycleButtonAction(_){
	
}

function compositionButtonAction(_){
	
}

function stageButtonAction(_){
	
}

function topRepaintStop(){
	if(!timerSubscribe){
		return;
	}
	timerSubscribe.unsubscribe();
	timerSubscribe = null;
}

window.addEventListener('pagehide', _ => {
	navigator.sendBeacon('/api/shutdown');
});

setInterval(sendHeartbeat, INTERVAL);

function sendHeartbeat(){
	navigator.sendBeacon("/api/heartbeat");
}