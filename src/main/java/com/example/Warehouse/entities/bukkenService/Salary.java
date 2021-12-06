package com.example.Warehouse.entities.bukkenService;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "salary", schema = "public")
public class Salary {
	@Id
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private double S2;
	private double S10;
	
	private Date create_At;

	private Date update_At;
	// owning side has joincolumn
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "bukken_id")
	@JsonIgnore
	private Bukken bukken;

	@PrePersist
	protected void onCreate() {
		this.create_At = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.update_At = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getS2() {
		return S2;
	}

	public void setS2(double s2) {
		S2 = s2;
	}

	public double getS10() {
		return S10;
	}

	public void setS10(double s10) {
		S10 = s10;
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

	public Bukken getBukken() {
		return bukken;
	}

	public void setBukken(Bukken bukken) {
		this.bukken = bukken;
	}
	
}
