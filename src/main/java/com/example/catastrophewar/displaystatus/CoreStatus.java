package com.example.catastrophewar.displaystatus;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.defaultdata.Core;
import com.example.defaultdata.CoreAtack;
import com.example.defaultdata.CoreUnit;
import com.example.defaultdata.DefaultEnum;
import com.example.defaultdata.core.CoreData;

@RestController
public class CoreStatus extends ElementStatus{
	@Override
	@GetMapping("/api/status/core/{id}")
	public Status sendStatus(@PathVariable int id) {
		CoreData select = DefaultEnum.getLabel(Core.values(), id);
		return new Status(defaultName(select.getRarity(), select.getName()),
				List.of(select.getImageName()),
				defaultLabel(CoreAtack.values()),
				addTimes(select.getWeaponStatus()),
				null,
				defaultLabel(CoreUnit.values()),
				addTimes(select.getUnitStatus()),
				cutLabel(),
				addPercent(select.getCutStatus()),
				List.of(select.getExplanation()));
	}
	
	List<String> addTimes(List<?> elements){
		return elements.stream().map(i -> i + "倍").toList();
	}
}