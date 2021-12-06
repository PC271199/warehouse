package com.example.Warehouse.dtos.accountService;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.example.Warehouse.entities.accountService.Permission;
import com.example.Warehouse.entities.accountService.Role;

public class AccountEditDtoAdmin {
	@NotBlank(message = "email is mandatory")
	@Email
	private String email;

	@Valid
	private Role role;
	@Valid
	@NotEmpty(message = "permissions must not be empty")
	private Set<Permission> permissions;

	public AccountEditDtoAdmin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}
