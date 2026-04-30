document.addEventListener('DOMContentLoaded', () => {
	document.getElementById("go-to-login").addEventListener('click', returnAction);
});

function returnAction(){
	window.location.href = "/login";
}