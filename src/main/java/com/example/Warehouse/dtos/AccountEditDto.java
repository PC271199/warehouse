package com.example.Warehouse.dtos;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AccountEditDto {
	@NotBlank(message = "email is mandatory")
    @Email
	private String email;
	
	@NotBlank(message = "fullname is mandatory")
	private String fullname;
	@NotBlank(message = "cmnd is mandatory")
	private String cmnd;
	@NotBlank(message = "address is mandatory")
	private String address;
	private Date dateOfBirth;
	public AccountEditDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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

}
