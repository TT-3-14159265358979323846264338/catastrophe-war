import {stompCondition} from '../common/stomp-condition.js';
import {inputReducedImage} from '../common/edit-image.js';
import {topRepaintStart} from '../app/app.js';
import {activateEditUnit} from './edit-unit.js';

const editUnitClas = document.querySelector(".edit-stage").classList;
const saveButton = document.getElementById("edit-stage-save");
const switchButton = document.getElementById("go-to-edit-unit-from-edit-stage");
const returnButton = document.getElementById("go-to-toppage-from-edit-stage");
let medal;
let stageClear = [];
let meritClear = [];
const IMAGE_RATIO = 5;

export async function editStage(){
	try{
		const response = await fetch("/api/edit/stage/data");
		const data = await response.json();
		initialize(data);
		await createScroll(data);
	}catch (e) {
		console.error("ステージ編集画面の初期化失敗:", e);
	}
}

function initialize(data){
	medal = data.medal;
	stageClear = data.stageClear;
	meritClear = data.meritClear;
}

async function createScroll(data){
	const stageName = data.stageName;
	const stageImages = await inputReducedImage(data.stageImages, IMAGE_RATIO);
	
	
	
}









export function activateEditStage(){
	stompCondition.resetSubscriptions();
	editUnitClas.remove('hidden');
}

document.addEventListener('DOMContentLoaded', () => {
	saveButton.addEventListener('click', saveButtonAction);
	switchButton.addEventListener('click', switchButtonAction);
	returnButton.addEventListener('click', returnButtonAction);
});

function saveButtonAction(){
	
}

function switchButtonAction(){
	endPage();
	activateEditUnit();
}

function returnButtonAction(){
	endPage();
	topRepaintStart();
}

function endPage(){
	editUnitClas.add('hidden');
	stompCondition.resetSubscriptions();
}