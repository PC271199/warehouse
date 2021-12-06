package com.example.Warehouse.dtos.accountService;

import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.example.Warehouse.entities.accountService.AuthProvider;
import com.example.Warehouse.entities.accountService.Permission;
import com.example.Warehouse.entities.accountService.Role;
import com.example.Warehouse.entities.bukkenService.Bukken;

// just for response and request with full permission
public class AccountDtoAdmin {

	private int id;
	@NotBlank(message = "email is mandatory")
    @Email
	private String email;
	@NotBlank
	@Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Password must be 8 characters including 1 uppercase letter, 1 lowercase letter and numeric characters")
	private String password;
	
	private boolean isEnabled;
	
	private UserInforDto userinforDto;
	@Valid
	private Role role;
	@Valid
	@NotEmpty(message = "permissions must not be empty")
	private Set<Permission> permissions;
	
	private Set<Bukken> bukkens;
	
//	@Enumerated(EnumType.STRING)
//    private AuthProvider provider;
	
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

//	public AuthProvider getProvider() {
//		return provider;
//	}
//
//	public void setProvider(AuthProvider provider) {
//		this.provider = provider;
//	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Set<Bukken> getBukkens() {
		return bukkens;
	}

	public void setBukkens(Set<Bukken> bukkens) {
		this.bukkens = bukkens;
	}
}
