package com.example.Warehouse.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

// just for response
public class UserInforDto {
	@NotBlank(message = "name is mandatory")
	@Length(max = 50, message = "name has max-length = 50")
	private String name;
	@NotBlank(message = "address is mandatory")
	@Length(max = 100, message = "address has max-length = 100")
	private String address;
	@Min(value = 1)
	private int age;

	@NotBlank(message = "email is mandatory")
	@Length(max = 50, message = "email has max-length = 50")
	@Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email not match")
	private String email;

	public UserInforDto(
			@NotBlank(message = "name is mandatory") @Length(max = 50, message = "name has max-length = 50") String name,
			@NotBlank(message = "address is mandatory") @Length(max = 100, message = "address has max-length = 100") String address,
			@Min(1) int age,
			@NotBlank(message = "email is mandatory") @Length(max = 50, message = "email has max-length = 50") @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email not match") String email) {
		super();
		this.name = name;
		this.address = address;
		this.age = age;
		this.email = email;
	}

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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
