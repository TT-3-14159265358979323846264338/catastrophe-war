package com.example.catastrophewar.displaystatus;

import java.util.List;

abstract class ElementStatus<T> {
	
	abstract public Status sendStatus(int id);
	
	protected record Status(List<String> weaponElement, 
			List<String> weaponStatus, 
			List<String> coreElement, 
			List<String> coreStatus, 
			List<String> cutElement, 
			List<String> cutStatus) {}
	
	
	
}