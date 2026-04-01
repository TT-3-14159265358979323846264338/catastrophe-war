import {topRepaintStart} from '../app/app.js';
import {editUnit} from './edit-unit.js';

const canvas = document.getElementById('game-display');
const ctx = canvas.getContext('2d');
let stompClient;
let subscription = [];
const editUnitClas = document.querySelector(".edit-stage").classList;
const switchButton = document.getElementById("go-to-edit-unit-from-edit-stage");
const returnButton = document.getElementById("go-to-toppage-from-edit-stage");

export function editStage(stomp){
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
	editUnit(stompClient);
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