package com.example.Warehouse.dtos;

// just for response with limitation role
public class AccountDto {
	private int id;
	private String email;
	private UserInforDto userInforDto;

	public AccountDto() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserInforDto getUserInforDto() {
		return userInforDto;
	}

	public void setUserInforDto(UserInforDto userInforDto) {
		this.userInforDto = userInforDto;
	}

}
