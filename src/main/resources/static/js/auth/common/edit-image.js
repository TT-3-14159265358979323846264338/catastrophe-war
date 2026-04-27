import {canvasCondition} from './canvas-condition.js';

export async function inputReducedImage(imageLink, ratio = 1){
	if(Array.isArray(imageLink)){
		return Promise.all(imageLink.map(link => inputReducedImage(link, ratio)));
	}
	try {
		const image = new Image();
		image.src = imageLink;
		await image.decode();
		return ratio === 1? image: reducedImage(image, ratio);
	} catch (e) {
		throw new Error(`${imageLink}が取り込めませんでした`)
	}
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

export function rotateDraw(image, x, y, angle){
	if(angle === 0){
		canvasCondition.ctx.drawImage(image, x, y);
		return;
	}
	const width = image.width / 2
	const height = image.height / 2;
	canvasCondition.ctx.save();
	canvasCondition.ctx.translate(x + width, y + height);
	canvasCondition.ctx.rotate(angle);
	canvasCondition.ctx.drawImage(image, -width, -height);
	canvasCondition.ctx.restore();
}

export function effectImage(image, color, expansion){
	return blurCanvas(colorChangeCanvas(image, color), expansion);
}

function colorChangeCanvas(image, color){
	const blue = 255 < color? 255: color;
	const alpha = blue / 255;
	const canvas = document.createElement('canvas');
	canvas.width = image.width;
	canvas.height = image.height;
	const ctx = canvas.getContext('2d');
	ctx.drawImage(image, 0, 0);
	ctx.globalCompositeOperation = 'source-in';
	ctx.fillStyle = `rgba(255, 255, ${blue}, ${alpha})`;
	ctx.fillRect(0, 0, image.width, image.height);
	ctx.globalCompositeOperation = 'source-over';
	return canvas;
}

function blurCanvas(defaultCanvas, expansion){
	const canvas = document.createElement('canvas');
	canvas.width = defaultCanvas.width + expansion;
	canvas.height = defaultCanvas.height + expansion;
	const ctx = canvas.getContext('2d');
	ctx.filter = `blur(4px) brightness(150%)`;
	ctx.drawImage(defaultCanvas, 0, 0, defaultCanvas.width, defaultCanvas.height, 0, 0, canvas.width, canvas.height);
	return canvas;
}