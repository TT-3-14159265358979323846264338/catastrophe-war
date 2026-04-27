import {status} from '../status/status.js'

const gachaResultClass = document.querySelector('.gacha-result-page').classList;
const resultLabel = document.getElementById("gacha-result-label");
const imageArea = document.getElementById("gacha-result-image-area");
const returnButton = document.getElementById("go-to-gacha-from-gacha-result");

export function gachaResultPage(result){
	imageArea.innerHTML = ""
	if(result.length === 0){
		resultLabel.textContent = "ガチャでエラーが発生したため無効になりました。";
		return;
	}
	resultLabel.textContent = "ガチャ結果";
	createImageHTML(result);
	gachaResultClass.remove('hidden');
}

function createImageHTML(result){
	result.forEach(data => {
		const image = document.createElement('img');
		image.src = data.imageLink;
		image.classList.add('pointer');
		image.addEventListener('click', () => status(data.unitCode, data.id));
		imageArea.appendChild(image);
	});
}

document.addEventListener('DOMContentLoaded', () => returnButton.addEventListener('click', returnButtonAction));

function returnButtonAction(){
	gachaResultClass.add('hidden');
}