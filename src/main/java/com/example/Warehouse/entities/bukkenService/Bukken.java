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
@Table(name = "bukken", schema = "public")
public class Bukken {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank(message = "name is mandatory")
	private String name;

	private double latitude;

	private double longitude;

	private double rentFee;

	private double area;

	private int numberOfFloors;

	private Date deliveryDate;

	private String imgURL;

	@Column(nullable = true, columnDefinition = "int default 0")
	private int countLike = 0;

	@Column(nullable = true, columnDefinition = "int default 0")
	private int countVisited = 0;
	
	@Column(nullable = true, columnDefinition = "int default 0")
	private int countSearch = 0;

	@Enumerated(EnumType.STRING)
	private BukkenStatus status;

	@Column(nullable = true, columnDefinition = "boolean default false")
	private boolean configured;
	
	@Column(nullable = true, columnDefinition = "boolean default false")
	private boolean vr;
	
	private Date create_At;

	private Date update_At;

	// owning side has joincolumn
	// cascade ALL appear in refering side
//	@ManyToMany(fetch = FetchType.LAZY,mappedBy = "bukkens")
//	@JsonIgnore
//	private Set<Account> accounts;

//	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinTable(name = "account_bukken", joinColumns = @JoinColumn(name = "bukken_id"), inverseJoinColumns = @JoinColumn(name = "account_id"))
//	@JsonIgnore
//	private Set<Account> accounts;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	private Account account;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "station_id")
	private Station station;

	// refering side has mappedBy att
	@OneToOne(mappedBy = "bukken", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ScheduleBukken scheduleBukken;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bukken", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<File> files;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bukken", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Tour> tours;

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "bukken", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<InterestedBukken> interestedBukkens;
	
	@PrePersist
	protected void onCreate() {
		this.create_At = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.update_At = new Date();
	}

	public Bukken() {
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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getRentFee() {
		return rentFee;
	}

	public void setRentFee(double rentFee) {
		this.rentFee = rentFee;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public BukkenStatus getStatus() {
		return status;
	}

	public void setStatus(BukkenStatus status) {
		this.status = status;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public ScheduleBukken getScheduleBukken() {
		return scheduleBukken;
	}

	public void setScheduleBukken(ScheduleBukken scheduleBukken) {
		this.scheduleBukken = scheduleBukken;
	}

	public Set<File> getFiles() {
		return files;
	}

	public void setFiles(Set<File> files) {
		this.files = files;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Set<Tour> getTours() {
		return tours;
	}

	public void setTours(Set<Tour> tours) {
		this.tours = tours;
	}

	public int getCountLike() {
		return countLike;
	}

	public void setCountLike(int countLike) {
		this.countLike = countLike;
	}

	public int getCountVisited() {
		return countVisited;
	}

	public void setCountVisited(int countVisited) {
		this.countVisited = countVisited;
	}

	public Set<InterestedBukken> getInterestedBukkens() {
		return interestedBukkens;
	}

	public void setInterestedBukkens(Set<InterestedBukken> interestedBukkens) {
		this.interestedBukkens = interestedBukkens;
	}

	public int getCountSearch() {
		return countSearch;
	}

	public void setCountSearch(int countSearch) {
		this.countSearch = countSearch;
	}

	public boolean isConfigured() {
		return configured;
	}

	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

	public boolean isVr() {
		return vr;
	}

	public void setVr(boolean vr) {
		this.vr = vr;
	}

}
