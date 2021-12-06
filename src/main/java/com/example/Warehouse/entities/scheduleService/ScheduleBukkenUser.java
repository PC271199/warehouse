package com.example.Warehouse.entities.scheduleService;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.accountService.Role;

@Entity
@Table(name = "schedulebukkenuser", schema = "public")
public class ScheduleBukkenUser {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	private Account account;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "schedulebukken_id")
	private ScheduleBukken schedulebukken;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy = "schedulebukkenuser",cascade = CascadeType.ALL)
	private Set<EventSchedule> eventScheduleList;

	public ScheduleBukkenUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
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
	
	
}
