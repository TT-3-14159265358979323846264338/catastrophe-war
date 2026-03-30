import {ableToPlayGacha} from './gacha.js'
import {weaponLink, coreLink, status} from '../status/status.js'

const gachaDetailPageClass = document.querySelector('.gacha-detail-page').classList;
const detailList = document.getElementById("gacha-detail-list");
const returnButton = document.getElementById("go-to-gacha-from-gacha-detail");
const coreId = "コア";
const weaponId = "武器";
const noId = "no id";

export async function gachaDetailPage(){
	try{
		const response = await fetch('/api/gacha/detail');
		const data = await response.json();
		initialize(data);
	}catch (e) {
		console.error("ガチャ詳細の取得に失敗:", e);
	}
	gachaDetailPageClass.remove('hidden');
}

function initialize(data){
	if(data.coreRatio.length !== 0){
		addTotalRatio(coreId, data.coreRatio);
		addElement(coreId, data.coreLineup, data.coreRatio);
		addList(addList => addList.innerHTML = "&nbsp;");
	}
	if(data.weaponRatio.length !== 0){
		addTotalRatio(weaponId, data.weaponRatio);
		addElement(weaponId, data.weaponLineup, data.weaponRatio);	
	}else{
		detailList.lastElementChild.remove();
	}
}

function addTotalRatio(elementId, ratioLineup){
	addDetailList(noId, noId, `【${elementId}】`, totalRatio(ratioLineup));
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
	if(!id || id === noId){
		return;
	}
	const link = target.dataset.elementId === coreId? coreLink: weaponLink;
	status(link, id);
}

function returnButtonAction(){
	detailList.innerHTML = "";
	gachaDetailPageClass.add('hidden');
	ableToPlayGacha();
}