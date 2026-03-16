import {topRepaintStart} from './app.js';
import {inputReducedImages, inputReducedImage, rotateDraw, effectImage} from './editImage.js';

const canvas = document.getElementById('mainCanvas');
const ctx = canvas.getContext('2d');
let stompClient;
let subscription = [];
const detailButton = document.getElementById("gacha-detail");
const countButton = document.getElementById("gacha-count");
const returnButton = document.getElementById("go-to-toppage-from-gacha");
const topPage = document.querySelector('.toppage-item');
const gachaPage = document.querySelector('.gacha-item');
let gachaCount = [];
let id;
let coreImage = [];
let weaponImage = [];
let halfBallImage = [];
let handleImage = new Image();
let machineImage = [];
let turnImage = new Image();
let dafaultEffectImage = new Image();
let isPressed = false;
const RATIO = 1.3;
const GACHA_X = 200;
const GACHA_Y = 10;
const HANDLE_X = 342;
const HANDLE_Y = 330;
const TURN_X = 278;
const TURN_Y = 266;
const EFFECT_X = 240;
const EFFECT_Y = 350;

export function gacha(stomp){
	gachaRepaintStop();
	if(!stompClient){
		stompClient = stomp;
		subscription.push(stompClient.subscribe("/topic/gacha/data", dataInstall));	
		stompClient.publish({destination: "/app/gacha/data"});
	}
	addMouseListener();
	subscription.push(stompClient.subscribe("/topic/gacha/repaint", drawImage), 
					stompClient.subscribe("/topic/gacha/play", playGacha),
					stompClient.subscribe("/topic/gacha/end", endGacha));
	stompClient.publish({destination: "/app/gacha/timer/start"});
}

function dataInstall(data){
	const gachaData = JSON.parse(data.body);
	id = gachaData.id
	inputGachaData(gachaData.gachaCount);
	inputImage(gachaData.links);
}

function inputGachaData(countText){
	countText.forEach(i => gachaCount.push(i));
	changeCountButtonText();
}

async function inputImage(links){
	await inputReducedImages(coreImage, links.coreImageLink, RATIO);
	await inputReducedImages(weaponImage, links.weaponImageLink, RATIO);
	await inputReducedImages(halfBallImage, links.halfBallImageLink, RATIO);
	handleImage = await inputReducedImage(links.handleImageLink, RATIO);
	await inputReducedImages(machineImage, links.machineImageLink, RATIO);
	turnImage = await inputReducedImage(links.turnImageLink, RATIO);
	dafaultEffectImage.src = links.effectImageLink;
}

function drawImage(data) {
	if(machineImage.length !== 2){
		return;
	}
	const state = JSON.parse(data.body);
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	ctx.drawImage(machineImage[0], GACHA_X, GACHA_Y);
	rotateDraw(ctx, halfBallImage[0], state.bottomPoint.x, state.bottomPoint.y, state.bottomAngle);
	rotateDraw(ctx, halfBallImage[1], state.topPoint.x, state.topPoint.y, state.topAngle);
	ctx.drawImage(machineImage[1], GACHA_X, GACHA_Y);
	rotateDraw(ctx, handleImage, HANDLE_X, HANDLE_Y, state.handleAngle);
	if(state.canPlayGacha){
		rotateDraw(ctx, turnImage, TURN_X, TURN_Y, state.turnAngle);
	}
	if(state.color !== 0){
		const image = effectImage(dafaultEffectImage, state.color, state.expansion);
		const x = EFFECT_X - state.expansion / 2;
		const y = EFFECT_Y - state.expansion / 2;
		ctx.drawImage(image, x, y);
	}
}

function playGacha(){
	switchAllButton(true);
	removeMouseListener();
}

function endGacha(){
	switchAllButton(false);
	addMouseListener();
}

function switchAllButton(isActive){
	switchButton(detailButton, isActive);
	switchButton(countButton, isActive);
	switchButton(returnButton, isActive);
}

function switchButton(button, isActive){
	button.disabled = isActive;
}

function addMouseListener(){
	canvas.addEventListener('mousedown', mousePressed);
	window.addEventListener('mousemove', mouseDragged);
	window.addEventListener('mouseup', mouseReleased);
}

function removeMouseListener(){
	canvas.removeEventListener('mousedown', mousePressed);
	window.removeEventListener('mousemove', mouseDragged);
	window.removeEventListener('mouseup', mouseReleased);
}

function mousePressed(e){
	isPressed = true;
	sendPoint(e, "/app/gacha/mouse/pressed");
}

function mouseDragged(e){
	if(isPressed){
		sendPoint(e, "/app/gacha/mouse/dragged");
	}
}

function mouseReleased(){
	if(isPressed){
		isPressed = false;
		stompClient.publish({destination: "/app/gacha/mouse/released"});
	}
}

function sendPoint(e, address){
	const canvasRect = canvas.getBoundingClientRect();
	const x = Math.round(e.clientX - canvasRect.left);
	const y = Math.round(e.clientY - canvasRect.top);
	stompClient.publish({
		destination: address,
		body: JSON.stringify({x, y})
	});
}








document.addEventListener('DOMContentLoaded', () => {
	detailButton.addEventListener('click', detailButtonAction);
	countButton.addEventListener('click', countButtonAction);
	returnButton.addEventListener('click', returnButtonAction);
});

function detailButtonAction(){
	
}

function countButtonAction(){
	id = (id === gachaCount.length - 1)? 0: id + 1;
	changeCountButtonText();
	stompClient.publish({
		destination: "/app/gacha/count/change",
		body: JSON.stringify({id})
	});
}

function changeCountButtonText(){
	countButton.textContent = gachaCount[id];
}

function returnButtonAction(_){
	gachaPage.classList.add('hidden');
	topPage.classList.remove('hidden');
	topRepaintStart();
	gachaRepaintStop();
}

function gachaRepaintStop(){
	if(subscription.length === 0){
		return;
	}
	removeMouseListener();
	subscription.forEach(data => data.unsubscribe());
	subscription = [];
	stompClient.publish({destination: "/app/gacha/timer/stop"});
}