import {stompCondition} from '../common/stomp-condition.js';
import {inputReducedImage} from '../common/edit-image.js';
import {topRepaintStart} from '../app/app.js';
import {editUnit} from './edit-unit.js';

const editUnitClas = document.querySelector(".edit-stage").classList;
const switchButton = document.getElementById("go-to-edit-unit-from-edit-stage");
const returnButton = document.getElementById("go-to-toppage-from-edit-stage");
let medal;
let stageClear = [];
let meritClear = [];
const IMAGE_RATIO = 5;

export async function initializeStage(){
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









export function editStage(){
	stompCondition.resetSubscriptions();
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
	stompCondition.resetSubscriptions();
}