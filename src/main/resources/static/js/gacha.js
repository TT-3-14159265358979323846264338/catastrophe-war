import {repaintStart} from './app.js';

const canvas = document.getElementById('mainCanvas');
const ctx = canvas.getContext('2d');
let subscription = [];
const topPage = document.querySelector('.toppage-item');
const gachaPage = document.querySelector('.gacha-item');

export function gacha(stompClient){
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	subscription.forEach(data => data.unsubscribe());
	subscription = [];
	subscription.push();
}

document.addEventListener('DOMContentLoaded', () => {
	const returnButton = document.getElementById("go-to-toppage-from-gacha");
	returnButton.addEventListener('click', returnButtonAction);
});

function returnButtonAction(_){
	gachaPage.classList.add('hidden');
	topPage.classList.remove('hidden');
	repaintStart();
}