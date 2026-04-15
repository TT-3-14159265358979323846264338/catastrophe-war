export function addScroll(elementId, list, imageList, htmlTask){
	imageList.forEach((image, i) => {
		const addList = document.createElement('li');
		const imageId = `edit-${elementId}-image-${i}`;
		addList.innerHTML = htmlTask(imageId, i);
		list.appendChild(addList);
		image.toBlob((blob) => imageDraw(blob, imageId));
	});
}

function imageDraw(blob, imageId){
	const url = URL.createObjectURL(blob);
	const image = document.getElementById(`${imageId}`);
	image.onload = () => URL.revokeObjectURL(url);
	image.src = url;	
}
