import {canvasCondition} from '../common/canvas-condition.js';
import {stompCondition} from '../common/stomp-condition.js';
import {inputReducedImage, rotateDraw} from '../common/edit-image.js';
import {heartbeat} from '../common/heartbeat.js'
import {gacha} from '../gacha/gacha.js';
import {editUnit} from '../edit/edit-unit.js';

const topPageClass = document.querySelector('.toppage-item').classList;
let titleImage;
let coreImages;
const TITLE_X = 80;
const TITLE_Y = 40;

heartbeat();

stompCondition.stompConnected = async () => {
	try{
		const response = await fetch('/api/top/data');
		const data = await response.json();
		await inputImage(data);
		topRepaintStart();
	}catch (e) {
		console.error("トップページの初期化失敗:", e);
	}
};

stompCondition.stompActivate();

async function inputImage(data){
	[titleImage, coreImages] = await Promise.all([inputReducedImage(data.title), inputReducedImage(data.core)]);
}

export function topRepaintStart(){
	stompCondition.resetSubscriptions();
	stompCondition.addSubscriptions('/user/queue/top/repaint', drawImage);
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

function gachaButtonAction(){
	ChangeTopPage(gacha);
}

function recycleButtonAction(){
	
}

function compositionButtonAction(){
	
}

function stageButtonAction(){
	
}

function editButtonAction(){
	ChangeTopPage(editUnit);
}

function ChangeTopPage(task){
	topPageClass.add('hidden');
	stompCondition.resetSubscriptions();
	task();
}