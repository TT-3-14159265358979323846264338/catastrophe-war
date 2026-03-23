const returnButton = document.getElementById("go-to-original-from-status");
export const weaponLink = "weapon";
export const coreLink = "core";

export async function status(link, id){
	try{
		const response = await fetch(`/api/status/${link}/${id}`);
		const data = await response.json();
		
		
		
	}catch (e) {
		console.error("ステータス画面の初期化失敗:", e);
	}
	getClassList().remove('hidden');
}

document.addEventListener('DOMContentLoaded', () => {
	returnButton.addEventListener('click', returnButtonAction);
});

function returnButtonAction(){
	getClassList().add('hidden');
}

function getClassList(){
	return document.querySelector('.status-page').classList;
}