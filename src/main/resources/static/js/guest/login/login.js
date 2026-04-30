const userNameInput = document.getElementById("username-input");
const passwordInput = document.getElementById("password-input");

document.addEventListener('DOMContentLoaded', () => {
	document.getElementById("user-login").addEventListener('click', userAction);
	document.getElementById("admin-login").addEventListener('click', adminAction);
});

function userAction(){
	changeUser("user", "password");
}

function adminAction(){
	changeUser("admin", "adminpassword");
}

function changeUser(name, password){
	userNameInput.value = name;
	passwordInput.value = password;
}