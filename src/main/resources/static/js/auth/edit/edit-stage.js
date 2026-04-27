import {inputReducedImage} from '../common/edit-image.js';
import {csrfHeaders} from '../common/csrf.js';
import {topRepaintStart} from '../app/app.js';
import {activateEditUnit} from './edit-unit.js';
import {addScroll} from './common-edit.js';

const editUnitClass = document.querySelector(".edit-stage").classList;
const saveButton = document.getElementById("edit-stage-save");
const switchButton = document.getElementById("go-to-edit-unit-from-edit-stage");
const returnButton = document.getElementById("go-to-toppage-from-edit-stage");
const medalInput = document.getElementById("edit-item-medal");
const stageList = document.getElementById("edit-stage-list");
let clearInput;
let meritInput;
const STAGE_ID = "stage";
const IMAGE_RATIO = 30;

export async function editStage(){
	stageList.innerHTML = "";
	clearInput = [];
	meritInput = [];
	try{
		const response = await fetch("/api/edit/stage/data");
		const data = await response.json();
		await createScroll(data);
	}catch (e) {
		console.error("ステージ編集画面の初期化失敗:", e);
	}
}

async function createScroll(data){
	const stageImages = await inputReducedImage(data.stageData.map(i => i.image), IMAGE_RATIO);
	medalInput.value = data.medal;
	addScroll(STAGE_ID, stageList, stageImages, htmlTask(data.stageData));
}

function htmlTask(data){
	return (imageId, i) => createHTML(imageId, i, data[i].name, data[i].stageClear, data[i].meritClear);
}

function createHTML(imageId, index, stageClearText, hasClearedStage, hasClearedMerit){
	const stageClearInputId = `edit-stage-clear-input-${index}`;
	const stageClearRadioName = `edit-stage-clear-name-${index}`;
	clearInput.push(stageClearInputId);
	meritInput.push([]);
	return `
	<div class="edit-scroll-list edit-stage-scroll-list default-label">
		<img id="${imageId}">
		<input type="checkbox" id="${stageClearInputId}" name="${stageClearRadioName}" ${hasClearedStage? 'checked' : ''}>
		<label for="${stageClearInputId}">${stageClearText}</label>
		${createMeritHTML(index, hasClearedMerit)}
	</div>
	`;
}

function createMeritHTML(index, hasClearedMerit){
	return hasClearedMerit.map((hasCleraed, i) => {
		const number = i + 1;
		const meritClearInputId = `edit-stage-${index}-merit-input-${number}`;
		const meritClearRadioName = `edit-stage-${index}-merit-name-${number}`;
		meritInput[index].push(meritClearInputId);
		return `
		<input type="checkbox" id="${meritClearInputId}" name="${meritClearRadioName}" ${hasCleraed? 'checked' : ''}>
		<label for="${meritClearInputId}">戦功${number}</label>
		`
	}).join('');
}

export function activateEditStage(){
	editUnitClass.remove('hidden');
}

document.addEventListener('DOMContentLoaded', () => {
	saveButton.addEventListener('click', saveButtonAction);
	switchButton.addEventListener('click', switchButtonAction);
	returnButton.addEventListener('click', returnButtonAction);
});

async function saveButtonAction(){
	const medal =  medalInput.value;
	const clear = checkCondition(clearInput);
	const merit = meritInput.map(i => checkCondition(i));
	try{
		await fetch("/api/edit/save/stage", {
			method: 'POST',
			headers: csrfHeaders(),
			body: JSON.stringify({medal, clear, merit})
		});
	}catch (e) {
		console.error("ステージのセーブに失敗:", e);
	}
}

function checkCondition(input){
	return input.map(i => document.getElementById(i).checked);
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
	editUnitClass.add('hidden');
}