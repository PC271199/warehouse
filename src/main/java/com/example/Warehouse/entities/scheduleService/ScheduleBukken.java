package com.example.Warehouse.entities.scheduleService;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "schedulebukken", schema = "public")
public class ScheduleBukken {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String note;
	
	private boolean configured;
	
	@ElementCollection(targetClass=String.class)
    private List<String> dates;
	
	private String startTime;
	
	private String endTime;
	
	private Date startDate;
	
	private Date endDate;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "bukken_id")
 	@JsonIgnore
 	private Bukken bukken;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "schedulebukken",cascade = CascadeType.ALL)
	private Set<ScheduleBukkenUser> scheduleBukkenUserList;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "schedulebukken",cascade = CascadeType.ALL)
	private Set<EventSchedule> eventScheduleList;
	
	
	private Date create_At;

	private Date update_At;

	public ScheduleBukken() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isConfigured() {
		return configured;
	}

	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<String> getDates() {
		return dates;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public Set<ScheduleBukkenUser> getScheduleBukkenUserList() {
		return scheduleBukkenUserList;
	}

	public void setScheduleBukkenUserList(Set<ScheduleBukkenUser> scheduleBukkenUserList) {
		this.scheduleBukkenUserList = scheduleBukkenUserList;
	}

	public Bukken getBukken() {
		return bukken;
	}

	public void setBukken(Bukken bukken) {
		this.bukken = bukken;
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

	public Set<ScheduleBukkenUser> getScheduleWarehouseUserList() {
		return scheduleBukkenUserList;
	}

	public void setScheduleWarehouseUserList(Set<ScheduleBukkenUser> scheduleBukkenUserList) {
		this.scheduleBukkenUserList = scheduleBukkenUserList;
	}

	public Set<EventSchedule> getEventScheduleList() {
		return eventScheduleList;
	}

	public void setEventScheduleList(Set<EventSchedule> eventScheduleList) {
		this.eventScheduleList = eventScheduleList;
	}
	
}
