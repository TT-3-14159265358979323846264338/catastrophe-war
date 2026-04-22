package com.example.catastrophewar.dispose;

import org.springframework.web.bind.annotation.RestController;

import com.example.savedata.CoreRepository;
import com.example.savedata.WeaponRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DisposePage {
	private final CoreRepository coreRepository;
	private final WeaponRepository weaponRepository;
	
	
	
}