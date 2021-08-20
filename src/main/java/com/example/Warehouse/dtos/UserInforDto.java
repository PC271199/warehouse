package com.example.Warehouse.dtos;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

// just for response
public class UserInforDto {
	private String name;
	private String address;
	private Date yearOfBirth;
	public UserInforDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(Date yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	

}
