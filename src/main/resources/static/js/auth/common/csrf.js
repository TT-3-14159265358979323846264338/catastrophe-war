export function csrfHeaders() {
	const token = getToken();
	const header = getHeader();
	return {
		'Content-Type': 'application/json',
		[header]: token
	};
}

export function csrfFormInput(){
	const input = document.createElement('input');
	input.type = 'hidden'
	input.name = '_csrf';
	input.value = getToken();
	return input;
}

function getToken(){
	return document.querySelector('meta[name="_csrf"]').content;
}

function getHeader(){
	return document.querySelector('meta[name="_csrf_header"]').content;
}