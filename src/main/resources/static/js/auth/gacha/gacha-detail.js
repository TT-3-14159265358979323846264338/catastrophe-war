import {stompCondition} from '../common/stomp-condition.js';
import {ableToPlayGacha} from './gacha.js'
import {WEAPON_LINK, CORE_LINK, status} from '../status/status.js'

const gachaDetailPageClass = document.querySelector('.gacha-detail-page').classList;
const detailList = document.getElementById("gacha-detail-list");
const returnButton = document.getElementById("go-to-gacha-from-gacha-detail");
const CORE_ID = "コア";
const WEAPON_ID = "武器";
const NO_ID = "no id";

export function gachaDetailPage(){
	stompCondition.addSubscriptions("/user/queue/gacha/detail/data", initialize);
}

export function activateGachaDetailPage(){
	gachaDetailPageClass.remove('hidden');
}

function initialize(data){
	const state = JSON.parse(data.body)
	if(state.coreRatio.length !== 0){
		addTotalRatio(CORE_ID, state.coreRatio);
		addElement(CORE_ID, state.coreLineup, state.coreRatio);
		addList(addList => addList.innerHTML = "&nbsp;");
	}
	if(state.weaponRatio.length !== 0){
		addTotalRatio(WEAPON_ID, state.weaponRatio);
		addElement(WEAPON_ID, state.weaponLineup, state.weaponRatio);	
	}else{
		detailList.lastElementChild.remove();
	}
}

function addTotalRatio(elementId, ratioLineup){
	addDetailList(NO_ID, NO_ID, `【${elementId}】`, totalRatio(ratioLineup));
}

function totalRatio(ratioLineup){
	return ratioLineup.reduce((total, i) => total + i, 0).toFixed(2);
}

function addElement(elementId, lineup, ratioLineup){
	lineup.forEach((lineupElement, i) => addDetailList(elementId, lineupElement.id, lineupElement.label.name, ratioLineup[i].toFixed(2)));
}

function addDetailList(elementId, id, leftComment, rightComment){
	addList(addList => {
		addList.dataset.elementId = elementId;
		addList.dataset.id = id;
		addList.innerHTML = 
			`<span class="left-aligned">${leftComment}</span>
			<span class="right-aligned">${rightComment}%</span>`;
	});
}

function addList(run){
	const addList = document.createElement('li');
	run(addList);
	detailList.appendChild(addList);
}

document.addEventListener('DOMContentLoaded', () => {
	detailList.addEventListener('click', detailListAction);
	returnButton.addEventListener('click', returnButtonAction);
});

function detailListAction(e){
	const target = e.target.closest('li');
	if(!target){
		return;
	}
	const id = target.dataset.id;
	if(!id || id === NO_ID){
		return;
	}
	const link = target.dataset.elementId === CORE_ID? CORE_LINK: WEAPON_LINK;
	status(link, id);
}

function returnButtonAction(){
	gachaDetailPageClass.add('hidden');
	detailList.innerHTML = "";
	ableToPlayGacha();
}