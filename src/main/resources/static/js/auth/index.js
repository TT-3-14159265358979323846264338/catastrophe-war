import {csrfForm as csrfFormInput} from './common/csrf.js';

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
	form.appendChild(csrfFormInput());
	document.body.appendChild(form);
	form.submit();
}