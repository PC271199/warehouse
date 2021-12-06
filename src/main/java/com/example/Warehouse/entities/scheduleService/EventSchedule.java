package com.example.Warehouse.entities.scheduleService;

import java.util.Date;

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
import javax.persistence.Table;


@Entity
@Table(name = "eventschedule", schema = "public")
public class EventSchedule {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Date startDate;
	
	private Date endDate;
	
	private String startTime;
	
	private String endTime;
	
	private boolean mon;
	
	private boolean tue;
	
	private boolean wed;
	
	private boolean thu;
	
	private boolean fri;
	
	private boolean sar;
	
	private boolean sun;
	
	@Enumerated(EnumType.STRING)
    private EventStatusSchedule status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "schedulebukken_id")
	private ScheduleBukken schedulebukken;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "schedulebukkenuser_id")
	private ScheduleBukkenUser schedulebukkenuser;

	public EventSchedule() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public boolean isMon() {
		return mon;
	}

	public void setMon(boolean mon) {
		this.mon = mon;
	}

	public boolean isTue() {
		return tue;
	}

	public void setTue(boolean tue) {
		this.tue = tue;
	}

	public boolean isWed() {
		return wed;
	}

	public void setWed(boolean wed) {
		this.wed = wed;
	}

	public boolean isThu() {
		return thu;
	}

	public void setThu(boolean thu) {
		this.thu = thu;
	}

	public boolean isFri() {
		return fri;
	}

	public void setFri(boolean fri) {
		this.fri = fri;
	}

	public boolean isSar() {
		return sar;
	}

	public void setSar(boolean sar) {
		this.sar = sar;
	}

	public boolean isSun() {
		return sun;
	}

	public void setSun(boolean sun) {
		this.sun = sun;
	}

	public EventStatusSchedule getStatus() {
		return status;
	}

	public void setStatus(EventStatusSchedule status) {
		this.status = status;
	}

	public ScheduleBukken getSchedulebukken() {
		return schedulebukken;
	}

	public void setSchedulebukken(ScheduleBukken schedulebukken) {
		this.schedulebukken = schedulebukken;
	}

	public ScheduleBukkenUser getSchedulebukkenuser() {
		return schedulebukkenuser;
	}

	public void setSchedulebukkenuser(ScheduleBukkenUser schedulebukkenuser) {
		this.schedulebukkenuser = schedulebukkenuser;
	}
}
