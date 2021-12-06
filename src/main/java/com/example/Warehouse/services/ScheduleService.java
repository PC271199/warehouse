package com.example.Warehouse.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.scheduleService.ScheduleBukken;
import com.example.Warehouse.repositories.scheduleService.ScheduleBukkenRepository;

@Service
public class ScheduleService {
	@Autowired
	private ScheduleBukkenRepository scheduleRepo;
	
	@Autowired
	private BukkenService bukkenService;
	
	public ScheduleBukken DS_getScheduleBukken(int bukkenId) {
		Bukken thisBukken=bukkenService.getById(bukkenId);
		if (thisBukken.getScheduleBukken() != null) {
			return thisBukken.getScheduleBukken();
		}
		else {
			ScheduleBukken newScheduleBukken = new ScheduleBukken();
			newScheduleBukken.setBukken(thisBukken);
			scheduleRepo.save(newScheduleBukken);
			return newScheduleBukken;
		}
	}
	public ScheduleBukken saveScheduleBukken(ScheduleBukken scheduleBukken) {
		Optional<ScheduleBukken> thisScheduleBukken= scheduleRepo.findById(scheduleBukken.getId());
		ScheduleBukken result=thisScheduleBukken.get();
		result.setConfigured(true);
		result.setDates(scheduleBukken.getDates());
		result.setStartDate(scheduleBukken.getStartDate());
		result.setEndDate(scheduleBukken.getEndDate());
		result.setStartTime(scheduleBukken.getStartTime());
		result.setEndTime(scheduleBukken.getEndTime());
		result.setNote(scheduleBukken.getNote());
		scheduleRepo.save(result);
		return scheduleBukken;
	}
}
