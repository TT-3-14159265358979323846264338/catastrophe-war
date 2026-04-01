import {topRepaintStart} from '../app/app.js';
import {editStage} from './edit-stage.js';

const canvas = document.getElementById('game-display');
const ctx = canvas.getContext('2d');
let stompClient;
let subscription = [];
const editUnitClas = document.querySelector(".edit-unit").classList;
const switchButton = document.getElementById("go-to-edit-stage-from-edit-unit");
const returnButton = document.getElementById("go-to-toppage-from-edit-unit");

export function editUnit(stomp){
	if(!stompClient){
		stompClient = stomp;
	}
	editUnitClas.remove('hidden');
}

document.addEventListener('DOMContentLoaded', () => {
	switchButton.addEventListener('click', switchButtonAction);
	returnButton.addEventListener('click', returnButtonAction);
});

function switchButtonAction(){
	endPage();
	editStage(stompClient);
}

function returnButtonAction(){
	endPage();
	topRepaintStart();
}

function endPage(){
	editUnitClas.add('hidden');
	subscription.forEach(data => data.unsubscribe());
	subscription = [];
}