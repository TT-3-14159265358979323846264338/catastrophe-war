export const weaponLink = "weapon";
export const coreLink = "core";

export async function status(link, id){
	try{
		const response = await fetch(`/api/status/${link}/${id}`);
		const data = await response.json();
		initialize(data);
	}catch (e) {
		console.error("ステータス画面の初期化失敗:", e);
	}
	getClassList().remove('hidden');
}

function initialize(data){
	changeName(data.name);
	changeWeapon(data.weaponElement, data.leftWeaponStatus, data.rightWeaponStatus);
	changeUnit(data.unitElement, data.unitStatus)
	changeCut(data.cutElement, data.cutStatus)
	
	
	
	
	
	
}

function changeName(name){
	document.getElementById("element-name").textContent = name;
}

function changeWeapon(weaponElement, leftWeaponStatus, rightWeaponStatus){
	const span = document.querySelectorAll("#weapon-status span");
	span[0].textContent = "　";
	for(let i = 1; i < 9; i++){
		span[i].textContent = weaponElement[i - 1]? weaponElement[i - 1]: "　";
	}
	for(let i = 10; i < 18; i++){
		span[i].textContent = leftWeaponStatus[i - 10]? leftWeaponStatus[i - 10]: "　";
	}
	if(rightWeaponStatus){
		span[9].textContent = "左武器";
		span[18].textContent = "右武器";
		for(let i = 19; i < span.length; i++){
			span[i].textContent = rightWeaponStatus[i - 19]? rightWeaponStatus[i - 19]: "　";
		}
	}else{
		span[9].textContent = "武器性能";
		span[18].textContent = "　";
		for(let i = 19; i < span.length; i++){
			span[i].textContent = "　";
		}
	}
}

function changeUnit(unitElement, unitStatus){
	const span = document.querySelectorAll("#core-status span");
	const halfLength = span.length / 2;
	for(let i = 0; i < halfLength; i++){
		span[i].textContent = unitElement[i];
		span[i + halfLength].textContent = unitStatus[i];
	}
}

function changeCut(cutElement, cutStatus){
	const span = document.querySelectorAll("#cut-status span");
	const quarterLength = span.length / 4;
	for(let i = 0; i < quarterLength; i++){
		span[i].textContent = cutElement[i];
		span[i + quarterLength].textContent = cutStatus[i];
		span[i + quarterLength * 2].textContent = cutElement[i + quarterLength];
		span[i + quarterLength * 3].textContent = cutStatus[i + quarterLength];
	}
}

document.addEventListener('DOMContentLoaded', () => {
	document.getElementById("go-to-original-from-status").addEventListener('click', returnButtonAction);
});

function returnButtonAction(){
	getClassList().add('hidden');
}

function getClassList(){
	return document.querySelector('.status-page').classList;
}