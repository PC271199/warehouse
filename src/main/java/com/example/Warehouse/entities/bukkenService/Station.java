package com.example.Warehouse.entities.bukkenService;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "station", schema = "public")
public class Station {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	
	private double lat;
	
	private double lng;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "station", cascade = CascadeType.ALL)
//	@JsonIgnore
//	private Set<Bukken> bukkens;

	
	private double S10;

	private Date create_At;

	private Date update_At;

	public Station() {
		super();
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

//	public Set<Bukken> getBukkens() {
//		return bukkens;
//	}
//
//	public void setBukkens(Set<Bukken> bukkens) {
//		this.bukkens = bukkens;
//	}


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

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

}
