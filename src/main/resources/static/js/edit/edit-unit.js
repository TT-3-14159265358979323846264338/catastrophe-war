import {canvasCondition} from '../common/canvas-condition.js';
import {stompCondition} from '../common/stomp-condition.js';
import {inputReducedImage} from '../common/edit-image.js';
import {topRepaintStart} from '../app/app.js';
import {editStage, activateEditStage} from './edit-stage.js';

const editUnitClas = document.querySelector(".edit-unit").classList;
const saveButton = document.getElementById("edit-unit-save");
const switchButton = document.getElementById("go-to-edit-stage-from-edit-unit");
const returnButton = document.getElementById("go-to-toppage-from-edit-unit");
const coreList = document.getElementById("edit-unit-core-list");
const weaponList = document.getElementById("edit-unit-weapon-list");
const CORE_ID = "コア";
const WEAPON_ID = "武器";
const IMAGE_RATIO = 5;

export async function editUnit(){
	canvasCondition.clearArea();
	coreList.innerHTML = "";
	weaponList.innerHTML = "";
	try{
		const response = await fetch("/api/edit/unit/data");
		const data = await response.json();
		await createScroll(data);
	}catch (e) {
		console.error("ユニット編集画面の初期化失敗:", e);
	}
	editStage();
	activateEditUnit();
}

async function createScroll(data){
	const [coreImages, weaponImages] = 
		await Promise.all([inputReducedImage(data.coreImages, IMAGE_RATIO), 
			inputReducedImage(data.weaponImages, IMAGE_RATIO)]);
	addScroll(CORE_ID, coreList, data.coreName, coreImages, data.coreNumber);
	addScroll(WEAPON_ID, weaponList, data.weaponName, weaponImages, data.weaponNumber);
}

function addScroll(elementId, list, nameList, imageList, numberList){
	nameList.forEach((name, i) => {
		const addList = document.createElement('li');
		addList.dataset.elementId = elementId;
		addList.dataset.id = i;
		const imageId = `edit-unit-${elementId}-image-${i}`;
		const inputId = `edit-unit-${elementId}-input-${i}`;
		addList.innerHTML = createHTML(imageId, inputId, name, numberList[i]);
		list.appendChild(addList);
		imageList[i].toBlob((blob) => imageDraw(blob, imageId));
	});
}

function createHTML(imageId, inputId, name, number){
	return `
	<div class="edit-unit-scroll-list default-spinner">
		<img id="${imageId}">
		<label for="${inputId}">${name}</label>
		<input id="${inputId}" name="number" type="number" min="0" max="99" step="1" value="${number}">
	</div>
	`;
}

function imageDraw(blob, imageId){
	const url = URL.createObjectURL(blob);
	const image = document.getElementById(`${imageId}`);
	image.onload = () => URL.revokeObjectURL(url);
	image.src = url;	
}

export function activateEditUnit(){
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
	activateEditStage();
}

function returnButtonAction(){
	endPage();
	topRepaintStart();
}

function endPage(){
	editUnitClas.add('hidden');
	stompCondition.resetSubscriptions();
}