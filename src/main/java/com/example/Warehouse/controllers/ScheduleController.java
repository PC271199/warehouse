package com.example.Warehouse.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.dtos.accountService.AccountDtoAdmin;
import com.example.Warehouse.dtos.scheduleService.ScheduleBukkenDto;
import com.example.Warehouse.entities.fileService.File;
import com.example.Warehouse.entities.scheduleService.ScheduleBukken;
import com.example.Warehouse.entities.scheduleService.ScheduleBukkenUser;
import com.example.Warehouse.mapper.ScheduleBukkenUserMapper;
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

	// create and get scheduleBukken
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/scheduleBukken/{bukkenId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<ScheduleBukken>> DSgetScheduleBukken(@PathVariable int bukkenId) {
		ScheduleBukken thisScheduleBukken = scheduleService.DS_getScheduleBukken(bukkenId);
		ResponseDto<ScheduleBukken> result = new ResponseDto<ScheduleBukken>(thisScheduleBukken, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<ScheduleBukken>>(result, HttpStatus.OK);
	}

	// save setting scheduleBukken
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/scheduleBukken", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<ScheduleBukken>> saveScheduleBukken(
			@RequestBody ScheduleBukkenDto scheduleBukkenDto) {
		ScheduleBukken thisScheduleBukken = scheduleService.saveScheduleBukken(scheduleBukkenDto.getScheduleBukken());
		fileService.fileResponse2File(scheduleBukkenDto.getFiles());
		ResponseDto<ScheduleBukken> result = new ResponseDto<ScheduleBukken>(thisScheduleBukken, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<ScheduleBukken>>(result, HttpStatus.OK);
	}

	// create scheduleBukkenUser
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/scheduleBukkenUser/create/{scheduleBukkenId}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<ScheduleBukkenUser>> createScheduleBukkenUser(
			@PathVariable int scheduleBukkenId) {
		ScheduleBukkenUser thisScheduleBukkenUser = scheduleService.createScheduleBukkenUser(scheduleBukkenId);
		ResponseDto<ScheduleBukkenUser> result = new ResponseDto<ScheduleBukkenUser>(thisScheduleBukkenUser,
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<ScheduleBukkenUser>>(result, HttpStatus.OK);
	}

	// get all scheduleBukkenUser
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/scheduleBukkenUser", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<ScheduleBukkenUser>>> getAllScheduleBukkenUser() {
		List<ScheduleBukkenUser> scheduleBukkenUserList = scheduleService.getAllScheduleBukkenUser();
		ResponseDto<List<ScheduleBukkenUser>> result = new ResponseDto<List<ScheduleBukkenUser>>(scheduleBukkenUserList,
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<ScheduleBukkenUser>>>(result, HttpStatus.OK);
	}

	// get ScheduleBukken by ScheduleBukkenUser_Id
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/scheduleBukkenUser/scheduleBukken/{scheduleBukkenUserId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<ScheduleBukken>> getScheduleBukken_ByScheduleUserId(
			@PathVariable int scheduleBukkenUserId) {
		ScheduleBukken scheduleBukken = scheduleService.getScheduleBukken_ByScheduleBukkenUser(scheduleBukkenUserId);
		ResponseDto<ScheduleBukken> result = new ResponseDto<ScheduleBukken>(scheduleBukken, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<ScheduleBukken>>(result, HttpStatus.OK);
	}

	// get ScheduleBukken by ScheduleBukkenUser_Id
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/scheduleBukkenUser/{scheduleBukkenUserId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<ScheduleBukkenUser>> getScheduleBukkenUser(
			@PathVariable int scheduleBukkenUserId) {
		ScheduleBukkenUser scheduleBukkenUser = scheduleService.getScheduleBukkenUserById(scheduleBukkenUserId);
		ResponseDto<ScheduleBukkenUser> result = new ResponseDto<ScheduleBukkenUser>(scheduleBukkenUser,
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<ScheduleBukkenUser>>(result, HttpStatus.OK);
	}

	// get all ScheduleBukkenUser by page
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/scheduleBukkenUser/page/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Page<ScheduleBukkenUser>>> getScheduleBukkenUserByPage(
			@PathVariable int pageIndex) {
		Page<ScheduleBukkenUser> scheduleBukkenUserList = scheduleService.getAllByPage(pageIndex);
		ResponseDto<Page<ScheduleBukkenUser>> result = new ResponseDto<Page<ScheduleBukkenUser>>(scheduleBukkenUserList,
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Page<ScheduleBukkenUser>>>(result, HttpStatus.OK);
	}

	// get all ScheduleBukkenUser by account per page (admin)
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/scheduleBukkenUser/account/page/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Page<ScheduleBukkenUser>>> getScheduleBukkenUser_ByAccount_PerPage(
			@PathVariable int pageIndex) {
		Page<ScheduleBukkenUser> scheduleBukkenUserList = scheduleService.getScheduleBukkenUser_ByAccount(pageIndex);
		ResponseDto<Page<ScheduleBukkenUser>> result = new ResponseDto<Page<ScheduleBukkenUser>>(scheduleBukkenUserList,
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Page<ScheduleBukkenUser>>>(result, HttpStatus.OK);
	}

	// get all ScheduleBukkenUser belong to owner
	@PreAuthorize("hasRole('ROLE_OWNER')")
	@RequestMapping(value = "/scheduleBukkenUser/owner/page/{pageIndex}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<Page<ScheduleBukkenUser>>> getScheduleBukkenUser_BelongOwner_PerPage(
			@PathVariable int pageIndex, @RequestParam int ownerId) {
		Page<ScheduleBukkenUser> scheduleBukkenUserList = scheduleService.getScheduleBukkenUser_BelongOwner(pageIndex,
				ownerId);
		ResponseDto<Page<ScheduleBukkenUser>> result = new ResponseDto<Page<ScheduleBukkenUser>>(scheduleBukkenUserList,
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Page<ScheduleBukkenUser>>>(result, HttpStatus.OK);
	}

	// count scheduleBukkenUser belong to owner
	@PreAuthorize("hasRole('ROLE_OWNER')")
	@RequestMapping(value = "/scheduleBukkenUser/count/owner/{ownerId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Integer>> countAll_ByOwner(@PathVariable int ownerId) {
		int count = scheduleService.countScheduleBukkenUser_BelongOwner(ownerId);
		ResponseDto<Integer> result = new ResponseDto<Integer>(count, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Integer>>(result, HttpStatus.OK);
	}

	// count all scheduleBukkenUser
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/scheduleBukkenUser/count", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Long>> countAll() {
		long count = scheduleService.countAll();
		ResponseDto<Long> result = new ResponseDto<Long>(count, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Long>>(result, HttpStatus.OK);
	}

	// save scheduleBukkenUser
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OWNER','ROLE_USER')")
	@RequestMapping(value = "/scheduleBukkenUser", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<ScheduleBukkenUser>> saveScheduleBukkenUser(
			@RequestBody ScheduleBukkenUser scheduleBukkenUser) {
		ScheduleBukkenUser thisScheduleBukkenUser = scheduleService.saveScheduleBukkenUser(scheduleBukkenUser);
		ResponseDto<ScheduleBukkenUser> result = new ResponseDto<ScheduleBukkenUser>(thisScheduleBukkenUser,
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<ScheduleBukkenUser>>(result, HttpStatus.OK);
	}

	// forward scheduleBukkenUser
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/scheduleBukkenUser/Admin", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDto<ScheduleBukkenUser>> forwardScheduleBukkenUser(
			@RequestBody ScheduleBukkenUser scheduleBukkenUser) {
		ScheduleBukkenUser thisScheduleBukkenUser = scheduleService.forwardScheduleBukkenUser(scheduleBukkenUser);
		ResponseDto<ScheduleBukkenUser> result = new ResponseDto<ScheduleBukkenUser>(thisScheduleBukkenUser,
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<ScheduleBukkenUser>>(result, HttpStatus.OK);
	}

	// delete all scheduleBukkenUser
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/scheduleBukkenUser", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto<Object>> deleteAllScheduleBukkenUser() {
		scheduleService.deleteAllScheduleBukkenUser();
		ResponseDto<Object> result = new ResponseDto<Object>("Delete scheduleBukkenUser successfully",
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	// delete all scheduleBukken
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/scheduleBukken", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto<Object>> deleteAllScheduleBukken() {
		scheduleService.deleteAllScheduleBukken();
		ResponseDto<Object> result = new ResponseDto<Object>("Delete scheduleBukken successfully",
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}
}
