class StompCondition{
	#stompClient;
	#subscriptions;
	
	constructor(defaultStomp = null) {
		if(StompCondition.instance){
			return StompCondition.instance;
		}
		StompCondition.instance = this;
		this.#stompClient = defaultStomp || new StompJs.Client({
		    webSocketFactory: () => new SockJS('/ws-game'),
			reconnectDelay: 5000,
		});
		this.#subscriptions = [];
	}
	
	/**
	 * @param {Function} task
	 */
	set stompConnected(task){
		this.#stompClient.onConnect = task;
	}
	
	stompActivate(){
		this.#stompClient.activate();
	}
	
	publish(destination, body = null){
		const options = {destination: destination};
		if (body) {
			options.body = JSON.stringify(body);
		}
		this.#stompClient.publish(options);
	}
	
	addSubscriptions(destination, task){
		this.#subscriptions.push(this.#stompClient.subscribe(destination, task));
	}
	
	resetSubscriptions(){
		if(this.#subscriptions.length === 0){
			return;
		}
		this.#subscriptions.forEach(data => data.unsubscribe());
		this.#subscriptions = [];
	}
}

export const stompCondition = new StompCondition();