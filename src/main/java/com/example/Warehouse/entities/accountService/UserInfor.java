package com.example.Warehouse.entities.accountService;

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
@Table(name = "userinfor", schema = "public")
public class UserInfor {
	@Id 
	@Column(name = "id", columnDefinition = "serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String fullname;
	
	private String cmnd;
	
	private String address;
	
	private Date dateOfBirth;
	
	private String companyname;
	
	private String phonenumber;
	
	private String firstname;
	
	private String lastname;
	
	private String provincecode;
	
	@Column(nullable = true, columnDefinition = "int default 0")
	private int exp;
	
	private String adddetail;
	
	private String imgURL;
	
	private Date create_At;
	
    private Date update_At;
	
    // owning side has joincolumn
 	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
 	@JsonIgnore
 	private Account account;
	
	@PrePersist
    protected void onCreate(){
        this.create_At =new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.update_At =new Date();
    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getCmnd() {
		return cmnd;
	}
	public void setCmnd(String cmnd) {
		this.cmnd = cmnd;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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
	
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getProvincecode() {
		return provincecode;
	}
	public void setProvincecode(String provincecode) {
		this.provincecode = provincecode;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public String getAdddetail() {
		return adddetail;
	}
	public void setAdddetail(String adddetail) {
		this.adddetail = adddetail;
	}
	public String getImgURL() {
		return imgURL;
	}
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
	
}
