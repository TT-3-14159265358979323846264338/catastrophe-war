document.addEventListener('DOMContentLoaded', () => {
	document.getElementById("logout").addEventListener('click', logoutAction);
});

function logoutAction(){
	if (!confirm("ログアウトしますか？")) {
		return;
	}
	const form = document.createElement('form');
	form.method = 'POST';
	form.action = '/logout';
	document.body.appendChild(form);
	form.submit();
}