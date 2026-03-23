package com.example.catastrophewar.displaystatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.defaultdata.core.CoreData;

@RestController
public class CoreStatus extends ElementStatus<CoreData>{
	@Override
	@GetMapping("/api/status/core/{id}")
	public Status sendStatus(@PathVariable int id) {
		return null;
	}
}