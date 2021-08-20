package com.example.Warehouse.dtos;


// just for response with limitation role
public class AccountDto {
	private int id;
	private String username;
	private UserInforDto userInforDto;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public AccountDto(int id, String username, UserInforDto userInforDto) {
		super();
		this.id = id;
		this.username = username;
		this.userInforDto = userInforDto;
	}

	public AccountDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserInforDto getUserInforDto() {
		return userInforDto;
	}

	public void setUserInforDto(UserInforDto userInforDto) {
		this.userInforDto = userInforDto;
	}

}
