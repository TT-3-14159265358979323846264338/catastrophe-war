package com.example.catastrophewar.displaystatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.defaultdata.weapon.WeaponData;

@RestController
public class WeaponStatus extends ElementStatus<WeaponData>{
	@Override
	@GetMapping("/api/status/weapon{id}")
	public Status sendStatus(@PathVariable int id) {
		return null;
	}
}