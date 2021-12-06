package com.example.Warehouse.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.dtos.scheduleService.ScheduleBukkenDto;
import com.example.Warehouse.entities.fileService.File;
import com.example.Warehouse.entities.scheduleService.ScheduleBukken;
import com.example.Warehouse.repositories.systemService.FileRepository;
import com.example.Warehouse.services.FileService;
import com.example.Warehouse.services.ScheduleService;

// add comment here
@RestController
@RequestMapping(value = "/rest-schedule")
public class ScheduleController {
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
    private FileRepository fileRepo;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/scheduleBukken/{bukkenId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<ScheduleBukken>> DSgetScheduleBukken(@PathVariable int bukkenId) {
		ScheduleBukken thisScheduleBukken= scheduleService.DS_getScheduleBukken(bukkenId);
		ResponseDto<ScheduleBukken> result = new ResponseDto<ScheduleBukken>(thisScheduleBukken, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<ScheduleBukken>>(result, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/scheduleBukken", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<ScheduleBukken>> saveScheduleBukken(@RequestBody ScheduleBukkenDto scheduleBukkenDto) {
		ScheduleBukken thisScheduleBukken= scheduleService.saveScheduleBukken(scheduleBukkenDto.getScheduleBukken());
		fileService.fileResponse2File(scheduleBukkenDto.getFiles());
		ResponseDto<ScheduleBukken> result = new ResponseDto<ScheduleBukken>(thisScheduleBukken, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<ScheduleBukken>>(result, HttpStatus.OK);
	}

}
