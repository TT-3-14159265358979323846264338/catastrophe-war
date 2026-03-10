export async function inputReducedImages(array, imageLinks, ratio){
	for (const link of imageLinks) {
		array.push(await inputReducedImage(link, ratio));
	}
}

export function inputReducedImage(imageLink, ratio){
	return new Promise(function(resolve) {
		inputImage(imageLink, ratio, resolve);
	});
}

function inputImage(imageLink, ratio, resolve){
	const image = new Image();
	image.src = imageLink;
	image.onload = function(){
		resolve(reducedImage(image, ratio));
	};
}

function reducedImage(image, ratio){
	const canvas = document.createElement('canvas')
	const ctx = canvas.getContext('2d');
	const width = image.width / ratio;
	const height = image.height / ratio;
	canvas.width = width;
	canvas.height = height;
	ctx.imageSmoothingEnabled = true;
	ctx.imageSmoothingQuality = 'high';
	ctx.drawImage(image, 0, 0, width, height);
	return canvas;
}

export function rotateDraw(ctx, image, x, y, angle){
	const width = image.width / 2
	const height = image.height / 2;
	ctx.save();
	ctx.translate(x + width, y + height);
	ctx.rotate(angle);
	ctx.drawImage(image, -width, -height);
	ctx.restore();
}