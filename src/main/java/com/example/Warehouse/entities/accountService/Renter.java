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
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.Valid;

import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.accountService.Role;
import com.example.Warehouse.entities.bukkenService.Bukken;

@Entity
@Table(name = "renter", schema = "public")
public class Renter {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private Date create_At;

	private Date update_At;
	
	private String mailuser;
	
	private String fullname;
	
	private String companyname;
	
	private String phonenumber;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
	@JoinColumn(name = "bukken_id")
	private Bukken bukken;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	private Account account;
	
	@PrePersist
    protected void onCreate(){
        this.create_At =new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.update_At =new Date();
    }
	public Renter() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getMailuser() {
		return mailuser;
	}
	public void setMailuser(String mailuser) {
		this.mailuser = mailuser;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
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
    
}
