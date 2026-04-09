import {canvasCondition} from '../common/canvas-condition.js';
import {stompCondition} from '../common/stomp-condition.js';
import {inputReducedImage} from '../common/edit-image.js';
import {topRepaintStart} from '../app/app.js';
import {initializeStage, editStage} from './edit-stage.js';

const editUnitClas = document.querySelector(".edit-unit").classList;
const switchButton = document.getElementById("go-to-edit-stage-from-edit-unit");
const returnButton = document.getElementById("go-to-toppage-from-edit-unit");
const coreList = document.getElementById("edit-unit-core-list");
const weaponList = document.getElementById("edit-unit-weapon-list");
let coreNumber = [];
let weaponNumber = [];
const IMAGE_RATIO = 5;

export async function editUnit(){
	canvasCondition.clearArea();
	stompCondition.resetSubscriptions();
	try{
		const response = await fetch("/api/edit/unit/data");
		const data = await response.json();
		initializeUnit(data);
		await createScroll(data);
	}catch (e) {
		console.error("ユニット編集画面の初期化失敗:", e);
	}
	initializeStage();
	editUnitClas.remove('hidden');
}

function initializeUnit(data){
	coreNumber = data.coreNumber;
	weaponNumber = data.weaponNumber;
}

async function createScroll(data){
	const coreName = data.coreName;
	const weaponName = data.weaponName;
	const [coreImages, weaponImages] = 
		await Promise.all([inputReducedImage(data.coreImages, IMAGE_RATIO), 
			inputReducedImage(data.weaponImages, IMAGE_RATIO)]);
	addScroll(coreList, coreName, coreImages);
	addScroll(weaponList, weaponName, weaponImages);
}

function addScroll(list, nameList, imageList){
	const addList = document.createElement('li');
	
	list.appendChild(addList);
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