import {canvasCondition} from '../common/canvas-condition.js';
import {stompCondition} from '../common/stomp-condition.js';
import {gacha} from '../gacha/gacha.js';
import {editUnit} from '../edit/edit-unit.js';
import {rotateDraw} from '../common/edit-image.js';

const topPageClass = document.querySelector('.toppage-item').classList;
const titleImage = new Image();
const coreImages = [];
const TITLE_X = 80;
const TITLE_Y = 40;
const INTERVAL = 25000;

stompCondition.stompConnected = async _ => {
	try{
		const response = await fetch('/api/top/data');
		const data = await response.json();
		inputImage(data);
		topRepaintStart();
	}catch (e) {
		console.error("トップページの初期化失敗:", e);
	}
};

stompCondition.stompActivate();

function inputImage(imageList){
	titleImage.src = imageList[0];
	for(let i = 1; i < imageList.length; i++){
		const image = new Image();
		image.src = imageList[i];
		coreImages.push(image);
	}
}

export function topRepaintStart(){
	stompCondition.resetSubscriptions();
	stompCondition.addSubscriptions('/topic/top/repaint', drawImage);
	stompCondition.publish("/app/top/timer/start");
	topPageClass.remove('hidden');
}

function drawImage(data) {
	const {state, isEnded} = JSON.parse(data.body);
	canvasCondition.clearArea();
	state.forEach(draw);
	if(isEnded){
		canvasCondition.ctx.drawImage(titleImage, TITLE_X, TITLE_Y);
	}
}

function draw(coreState){
	const image = coreImages[coreState.id];
	rotateDraw(image, coreState.x, coreState.y, coreState.angle);
}

document.addEventListener('DOMContentLoaded', () => {
	document.getElementById("go-to-gacha-from-toppage").addEventListener('click', gachaButtonAction);
	document.getElementById("go-to-recycle-from-toppage").addEventListener('click', recycleButtonAction);
	document.getElementById("go-to-composition-from-toppage").addEventListener('click', compositionButtonAction);
	document.getElementById("go-to-stage-from-toppage").addEventListener('click', stageButtonAction);
	document.getElementById("go-to-edit-from-toppage").addEventListener('click', editButtonAction);
});

function gachaButtonAction(_){
	ChangeTopPage(gacha);
}

function recycleButtonAction(_){
	
}

function compositionButtonAction(_){
	
}

function stageButtonAction(_){
	
}

function editButtonAction(_){
	ChangeTopPage(editUnit);
}

function ChangeTopPage(task){
	topPageClass.add('hidden');
	stompCondition.resetSubscriptions();
	task();
}

window.addEventListener('pagehide', _ => {
	navigator.sendBeacon('/api/shutdown');
});

setInterval(sendHeartbeat, INTERVAL);

function sendHeartbeat(){
	navigator.sendBeacon("/api/heartbeat");
}