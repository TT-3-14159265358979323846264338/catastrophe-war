import {canvasCondition} from '../common/canvas-condition.js';
import {stompCondition} from '../common/stomp-condition.js';
import {topRepaintStart} from '../app/app.js';
import {inputReducedImages, inputReducedImage, rotateDraw, effectImage} from '../common/edit-image.js';
import {gachaDetailPage} from './gacha-detail.js'

const gachaPageClass = document.querySelector('.gacha-item').classList;
const gachaScrollClass = document.querySelector('.gacha-scroll').classList;
const gachaList = document.getElementById("gacha-list")
const detailButton = document.getElementById("gacha-detail");
const countButton = document.getElementById("gacha-count");
const returnButton = document.getElementById("go-to-toppage-from-gacha");
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

export function gacha(){
	if(!id){
		initialize();
	}
	addMouseListener();
	stompCondition.resetSubscriptions();
	stompCondition.addSubscriptions("/topic/gacha/list", inputGachaList);
	stompCondition.addSubscriptions("/topic/gacha/repaint", drawImage);
	stompCondition.addSubscriptions("/topic/gacha/play", playGacha);
	stompCondition.addSubscriptions("/topic/gacha/end", endGacha);
	stompCondition.publish("/app/gacha/timer/start");
	gachaPageClass.remove('hidden');
}

async function initialize(){
	try{
		const response = await fetch('/api/gacha/data');
		const data = await response.json();
		inputMedal(data.medal);
		id = data.id
		inputGachaData(data.gachaCount);
		inputImage(data.links);
	}catch (e) {
		console.error("ガチャ画面の初期化失敗:", e);
	}
}

function inputMedal(medal){
	document.getElementById("gacha-medal-label").textContent = `メダル数: ${medal}`;
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

function inputGachaList(data){
	JSON.parse(data.body).forEach(addGachaList);
	selectGacha(gachaList.querySelector('li'));
}

function addGachaList(element){
	const addList = document.createElement('li');
	addList.dataset.id = element.id;
	addList.textContent = element.label.name;
	gachaList.appendChild(addList);
}

function removeGachaList(){
	gachaList.innerHTML = "";
}

function drawImage(data) {
	if(machineImage.length !== 2){
		return;
	}
	const state = JSON.parse(data.body);
	canvasCondition.clearArea();
	canvasCondition.ctx.drawImage(machineImage[0], GACHA_X, GACHA_Y);
	rotateDraw(halfBallImage[0], state.bottomPoint.x, state.bottomPoint.y, state.bottomAngle);
	rotateDraw(halfBallImage[1], state.topPoint.x, state.topPoint.y, state.topAngle);
	canvasCondition.ctx.drawImage(machineImage[1], GACHA_X, GACHA_Y);
	rotateDraw(handleImage, HANDLE_X, HANDLE_Y, state.handleAngle);
	if(state.canPlayGacha){
		rotateDraw(turnImage, TURN_X, TURN_Y, state.turnAngle);
	}
	if(state.color !== 0){
		const image = effectImage(dafaultEffectImage, state.color, state.expansion);
		const x = EFFECT_X - state.expansion / 2;
		const y = EFFECT_Y - state.expansion / 2;
		canvasCondition.ctx.drawImage(image, x, y);
	}
}

function playGacha(){
	gachaScrollClass.add('disable-scroll');
	switchAllButton(true);
	removeMouseListener();
}

function endGacha(data){
	inputMedal(JSON.parse(data.body));
	ableToPlayGacha();
}

export function ableToPlayGacha(){
	gachaScrollClass.remove('disable-scroll');
	switchAllButton(false);
	addMouseListener();
}

function switchAllButton(isDisabled){
	switchButton(detailButton, isDisabled);
	switchButton(countButton, isDisabled);
	switchButton(returnButton, isDisabled);
}

function switchButton(button, isDisabled){
	button.disabled = isDisabled;
}

function addMouseListener(){
	removeMouseListener();
	canvasCondition.canvas.addEventListener('mousedown', mousePressed);
	window.addEventListener('mousemove', mouseDragged);
	window.addEventListener('mouseup', mouseReleased);
}

function removeMouseListener(){
	canvasCondition.canvas.removeEventListener('mousedown', mousePressed);
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
		stompCondition.publish("/app/gacha/mouse/released");
	}
}

function sendPoint(e, address){
	const canvasRect = canvasCondition.canvas.getBoundingClientRect();
	const x = Math.round(e.clientX - canvasRect.left);
	const y = Math.round(e.clientY - canvasRect.top);
	stompCondition.publish(address, {x, y});
}

document.addEventListener('DOMContentLoaded', () => {
	gachaList.addEventListener('click', gachaListAction);
	detailButton.addEventListener('click', detailButtonAction);
	countButton.addEventListener('click', countButtonAction);
	returnButton.addEventListener('click', returnButtonAction);
});

function gachaListAction(e){
	const target = e.target.closest('li');
	if(!target){
		return;
	}
	document.querySelectorAll('#gacha-list li').forEach(i => i.classList.remove('selected'));
	selectGacha(target);
}

function selectGacha(target){
	target.classList.add('selected');
	const selectId = target.dataset.id;
	stompCondition.publish("/app/gacha/select", {selectId});
}

function detailButtonAction(){
	playGacha();
	gachaDetailPage();
}

function countButtonAction(){
	id = (id === gachaCount.length - 1)? 0: id + 1;
	changeCountButtonText();
	stompCondition.publish("/app/gacha/count/change", {id});
}

function changeCountButtonText(){
	countButton.textContent = gachaCount[id];
}

function returnButtonAction(_){
	gachaPageClass.add('hidden');
	topRepaintStart();
	gachaRepaintStop();
}

function gachaRepaintStop(){
	stompCondition.resetSubscriptions();
	removeGachaList();
	removeMouseListener();
	stompCondition.publish("/app/gacha/timer/stop");
}