import {canvasCondition} from '../common/canvas-condition.js';
import {stompCondition} from '../common/stomp-condition.js';
import {topRepaintStart} from '../app/app.js';
import {editStage} from './edit-stage.js';

const editUnitClas = document.querySelector(".edit-unit").classList;
const switchButton = document.getElementById("go-to-edit-stage-from-edit-unit");
const returnButton = document.getElementById("go-to-toppage-from-edit-unit");

export function editUnit(){
	canvasCondition.clearArea();
	stompCondition.resetSubscriptions();
	editUnitClas.remove('hidden');
}

document.addEventListener('DOMContentLoaded', () => {
	switchButton.addEventListener('click', switchButtonAction);
	returnButton.addEventListener('click', returnButtonAction);
});

function switchButtonAction(){
	endPage();
	editStage();
}

function returnButtonAction(){
	endPage();
	topRepaintStart();
}

function endPage(){
	editUnitClas.add('hidden');
	stompCondition.resetSubscriptions();
}