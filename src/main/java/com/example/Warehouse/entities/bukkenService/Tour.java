package com.example.Warehouse.entities.bukkenService;

import java.util.Date;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.accountService.UserInfor;
import com.example.Warehouse.entities.fileService.File;
import com.example.Warehouse.entities.scheduleService.ScheduleBukken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.Nullable;

@Entity
@Table(name = "tour", schema = "public")
public class Tour {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String name;
	
	private String idIframe;
	
	private String tourUUID;
	
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "bukken_id")
	private Bukken bukken;
	
	private Date create_At;

	private Date update_At;

	@PrePersist
	protected void onCreate() {
		this.create_At = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.update_At = new Date();
	}

	public Tour() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getIdIframe() {
		return idIframe;
	}

	public void setIdIframe(String idIframe) {
		this.idIframe = idIframe;
	}

	public String getTourUUID() {
		return tourUUID;
	}

	public void setTourUUID(String tourUUID) {
		this.tourUUID = tourUUID;
	}

}
