import {canvasCondition} from '../common/canvas-condition.js';
import {stompCondition} from '../common/stomp-condition.js';
import {inputReducedImage} from '../common/edit-image.js';
import {csrfHeaders} from '../common/csrf-headers.js';
import {topRepaintStart} from '../app/app.js';
import {editStage, activateEditStage} from './edit-stage.js';
import {addScroll} from './common-edit.js';

const editUnitClas = document.querySelector(".edit-unit").classList;
const saveButton = document.getElementById("edit-unit-save");
const switchButton = document.getElementById("go-to-edit-stage-from-edit-unit");
const returnButton = document.getElementById("go-to-toppage-from-edit-unit");
const coreList = document.getElementById("edit-unit-core-list");
const weaponList = document.getElementById("edit-unit-weapon-list");
let coreInput;
let weaponInput;
const CORE_ID = "core";
const WEAPON_ID = "weapon";
const IMAGE_RATIO = 5;

export async function editUnit(){
	canvasCondition.clearArea();
	coreList.innerHTML = "";
	weaponList.innerHTML = "";
	coreInput = []
	weaponInput = []
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
		await Promise.all([inputReducedImage(data.coreData.map(i => i.image), IMAGE_RATIO), 
			inputReducedImage(data.weaponData.map(i => i.image), IMAGE_RATIO)]);
	addScroll(CORE_ID, coreList, coreImages, htmlTask(coreInput, data.coreData));
	addScroll(WEAPON_ID, weaponList, weaponImages, htmlTask(weaponInput, data.weaponData));
}

function htmlTask(input, data){
	return (imageId, i) => createHTML(imageId, i, input, data[i].name, data[i].number);
}

function createHTML(imageId, index, input, text, number){
	const inputId = `edit-${imageId}-input-${index}`;
	input.push(inputId);
	return `
	<div class="edit-scroll-list edit-unit-scroll-list default-label">
		<img id="${imageId}">
		<label for="${inputId}">${text}</label>
		<input type="number" id="${inputId}" name="number" min="0" max="99" step="1" value="${number}">
	</div>
	`;
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

async function saveButtonAction(){
	const coreNumber = radioValue(coreInput);
	const weaponNumber = radioValue(weaponInput);
	try{
		await fetch("/api/edit/save/unit", {
			method: 'POST',
			headers: csrfHeaders(),
			body: JSON.stringify({coreNumber, weaponNumber})
		});
	}catch (e) {
		console.error("ユニットのセーブに失敗:", e);
	}
}

function radioValue(input){
	return input.map(i => document.getElementById(i).value);
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
}