import {topRepaintStart} from './app.js';
import {inputReducedImages, inputReducedImage, rotateDraw} from './editImage.js';

const canvas = document.getElementById('mainCanvas');
const ctx = canvas.getContext('2d');
let stompClient;
let subscription = [];
const topPage = document.querySelector('.toppage-item');
const gachaPage = document.querySelector('.gacha-item');
let coreImage = [];
let weaponImage = [];
let ballImage = new Image();
let halfBallImage = [];
let handleImage = new Image();
let machineImage = [];
let turnImage = new Image();
let effectImage = new Image();
const RATIO = 1.3;
const GACHA_X = 200;
const GACHA_Y = 10;

export function gacha(stomp){
	gachaRepaintStop();
	if(!stompClient){
		stompClient = stomp;
		subscription.push(stompClient.subscribe("/topic/gacha/images", inputImage));	
		stompClient.send("/app/gacha/images", {}, {});
	}
	subscription.push(stompClient.subscribe("/topic/gacha/repaint", drawImage));
	stompClient.send("/app/gacha/timer/start", {}, {});
}

async function inputImage(data){
	const links = JSON.parse(data.body);
	await inputReducedImages(coreImage, links.coreImageLink, RATIO);
	await inputReducedImages(weaponImage, links.weaponImageLink, RATIO);
	ballImage = await inputReducedImage(links.ballImageLink, RATIO);
	await inputReducedImages(halfBallImage, links.halfBallImageLink, RATIO);
	handleImage = await inputReducedImage(links.handleImageLink, RATIO);
	await inputReducedImages(machineImage, links.machineImageLink, RATIO);
	turnImage = await inputReducedImage(links.turnImageLink, RATIO);
	effectImage.src = links.effectImageLink;
}

function drawImage(data) {
	if(machineImage.length !== 2){
		return;
	}
	const state = JSON.parse(data.body);
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	ctx.drawImage(machineImage[0], GACHA_X, GACHA_Y);
	//drawBall();
	ctx.drawImage(machineImage[1], GACHA_X, GACHA_Y);
	
	
	
	
	
}








document.addEventListener('DOMContentLoaded', () => {
	const returnButton = document.getElementById("go-to-toppage-from-gacha");
	returnButton.addEventListener('click', returnButtonAction);
});

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
	subscription.forEach(data => data.unsubscribe());
	subscription = [];
	stompClient.send("/app/gacha/timer/stop", {}, {});
}