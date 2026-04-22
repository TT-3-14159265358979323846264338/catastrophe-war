import {topRepaintStart} from '../app/app.js';

const disposeClass = document.querySelector('.dispose-page').classList;
const returnButton = document.getElementById("go-to-toppage-from-gacha-dispose");

export function dispose(){
	disposeClass.remove('hidden');
}

document.addEventListener('DOMContentLoaded', () => {
	returnButton.addEventListener('click', returnButtonAction);
});

function returnButtonAction(){
	disposeClass.add('hidden');
	topRepaintStart();
}