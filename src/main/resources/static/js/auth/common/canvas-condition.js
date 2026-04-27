class CanvasCondition{
	canvas;
	ctx;
	
	constructor() {
		if(CanvasCondition.instance){
			return CanvasCondition.instance;
		}
		CanvasCondition.instance = this;
		this.canvas = document.getElementById('game-display');
		this.ctx = this.canvas.getContext('2d');
	}
	
	clearArea(){
		this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
	}
}

export const canvasCondition = new CanvasCondition();