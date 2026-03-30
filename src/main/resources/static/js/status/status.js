export const weaponLink = "weapon";
export const coreLink = "core";

const statusPageClass = document.querySelector('.status-page').classList;
const branck = "　";

export async function status(link, id){
	try{
		const response = await fetch(`/api/status/${link}/${id}`);
		const data = await response.json();
		initialize(data);
	}catch (e) {
		console.error("ステータス画面の初期化失敗:", e);
	}
	statusPageClass.remove('hidden');
}

function initialize(data){
	drawImage(data.imageLink);
	changeName(data.name);
	changeWeapon(data.weaponElement, data.leftWeaponStatus, data.rightWeaponStatus);
	changeUnit(data.unitElement, data.unitStatus)
	changeCut(data.cutElement, data.cutStatus)
	changeExplanation(data.explanation);
}

function drawImage(imageLink){
	const imageArea = document.querySelectorAll(".status-image img");
	imageArea.forEach((image, i) => {
		if(imageLink[i]){
			image.src = imageLink[i];
			image.style.display = 'block';
		}else{
			image.style.display = 'none';
		}
	});
}

function changeName(name){
	document.getElementById("status-element-name").textContent = name;
}

function changeWeapon(weaponElement, leftWeaponStatus, rightWeaponStatus){
	const span = document.querySelectorAll("#weapon-status span");
	span[0].textContent = branck;
	for(let i = 1; i < 9; i++){
		span[i].textContent = weaponElement[i - 1]? weaponElement[i - 1]: branck;
	}
	for(let i = 10; i < 18; i++){
		span[i].textContent = leftWeaponStatus[i - 10]? leftWeaponStatus[i - 10]: branck;
	}
	if(rightWeaponStatus){
		span[9].textContent = "左武器";
		span[18].textContent = "右武器";
		for(let i = 19; i < span.length; i++){
			span[i].textContent = rightWeaponStatus[i - 19]? rightWeaponStatus[i - 19]: branck;
		}
	}else{
		span[9].textContent = "武器性能";
		span[18].textContent = branck;
		for(let i = 19; i < span.length; i++){
			span[i].textContent = branck;
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

function changeExplanation(explanation){
	const span = document.querySelectorAll(".status-explanation-container span");
	if(explanation[1]){
		span[0].textContent = "右武器";
		span[2].textContent = "コア";
		span[4].textContent = "左武器";
	}else{
		span[0].textContent = "能力/説明";
		span[2].textContent = branck;
		span[4].textContent = branck;
	}
	for(let i = 0; i < span.length / 2; i++){
		span[i * 2 + 1].textContent = explanation[i]? explanation[i]: branck;
	}
}

document.addEventListener('DOMContentLoaded', () => {
	document.getElementById("go-to-original-from-status").addEventListener('click', returnButtonAction);
});

function returnButtonAction(){
	statusPageClass.add('hidden');
}