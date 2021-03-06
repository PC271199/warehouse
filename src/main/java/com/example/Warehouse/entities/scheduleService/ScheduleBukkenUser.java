package com.example.Warehouse.entities.scheduleService;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.example.Warehouse.entities.accountService.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "schedulebukkenuser", schema = "public")
public class ScheduleBukkenUser implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int statusId;

	private String statusName;
	
	@Enumerated(EnumType.STRING)
	private Target target;
	
	@Enumerated(EnumType.STRING)
	private Target2 target2;
	
	private String note;
	
	private String bukkenName;
	
	private Date create_At;

	private Date update_At;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	private Account account;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "schedulebukken_id")
	private ScheduleBukken schedulebukken;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "schedulebukkenuser", cascade = CascadeType.ALL)
	@OrderBy("orderWish ASC")
	private Set<EventSchedule> eventScheduleList;

	@PrePersist
	protected void onCreate() {
		this.create_At = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.update_At = new Date();
	}

	public ScheduleBukkenUser() {
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
