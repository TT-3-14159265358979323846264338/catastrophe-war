package com.example.defaultdata;

import com.example.defaultdata.facility.*;

public enum Facility implements DefaultEnum<FacilityData>{
	CASTLE(0, new No0000Castle()),
	FRONT_GATE(1, new No0001FrontGate()),
	SIDE_GATE(2, new No0002SideGate()),
	STRONGHOLD(3, new No0003Stronghold());
	
	private final int id;
	private final FacilityData label;

	Facility(int id, FacilityData label) {
		this.id = id;
		this.label = label;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public FacilityData getLabel() {
		return label;
	}
}