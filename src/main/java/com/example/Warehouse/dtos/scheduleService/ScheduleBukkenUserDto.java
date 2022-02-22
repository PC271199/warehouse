package com.example.Warehouse.dtos.scheduleService;

import java.util.Date;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.scheduleService.EventSchedule;
import com.example.Warehouse.entities.scheduleService.ScheduleBukken;
import com.example.Warehouse.entities.scheduleService.Target;
import com.example.Warehouse.entities.scheduleService.Target2;

public class ScheduleBukkenUserDto {
	private int id;

	private int statusId;

	private String statusName;

	@Enumerated(EnumType.STRING)
	private Target target;

	@Enumerated(EnumType.STRING)
	private Target2 target2;

	private String bukkenName;
	
	private String note;

	private Date create_At;

	private Date update_At;

	private Account account;

	private ScheduleBukken schedulebukken;

	private Set<EventSchedule> eventScheduleList;

	public ScheduleBukkenUserDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public ScheduleBukken getSchedulebukken() {
		return schedulebukken;
	}

	public void setSchedulebukken(ScheduleBukken schedulebukken) {
		this.schedulebukken = schedulebukken;
	}

	public Set<EventSchedule> getEventScheduleList() {
		return eventScheduleList;
	}

	public void setEventScheduleList(Set<EventSchedule> eventScheduleList) {
		this.eventScheduleList = eventScheduleList;
	}

	public Date getCreate_At() {
		return create_At;
	}

	public void setCreate_At(Date create_At) {
		this.create_At = create_At;
	}

	public Date getUpdate_At() {
		return update_At;
	}

	public void setUpdate_At(Date update_At) {
		this.update_At = update_At;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public Target2 getTarget2() {
		return target2;
	}

	public void setTarget2(Target2 target2) {
		this.target2 = target2;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getBukkenName() {
		return bukkenName;
	}

	public void setBukkenName(String bukkenName) {
		this.bukkenName = bukkenName;
	}
	
}
