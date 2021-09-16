package com.example.Warehouse.dtos;

import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


import com.example.Warehouse.entities.AuthProvider;
import com.example.Warehouse.entities.Permission;
import com.example.Warehouse.entities.Role;

// just for response and request with full permission
public class AccountDtoAdmin {

	private int id;
	@NotBlank(message = "email is mandatory")
    @Email
	private String email;
	
	private String password;
	
	private UserInforDto userinforDto;
	@Valid
	private Role role;
	@Valid
	@NotEmpty(message = "permissions must not be empty")
	private Set<Permission> permissions;
	
	@Enumerated(EnumType.STRING)
    private AuthProvider provider;
	
	public AccountDtoAdmin() {
		super();
		// TODO Auto-generated constructor stub
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


	public UserInforDto getUserinforDto() {
		return userinforDto;
	}

	public void setUserinforDto(UserInforDto userinforDto) {
		this.userinforDto = userinforDto;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public AuthProvider getProvider() {
		return provider;
	}

	public void setProvider(AuthProvider provider) {
		this.provider = provider;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	
	
}
