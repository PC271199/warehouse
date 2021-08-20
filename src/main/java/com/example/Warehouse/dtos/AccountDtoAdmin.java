package com.example.Warehouse.dtos;

import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.example.Warehouse.entities.AuthProvider;
import com.example.Warehouse.entities.Permission;
import com.example.Warehouse.entities.Role;

// just for response and request with full permission
public class AccountDtoAdmin {

	private int id;
//	@NotBlank(message = "email is mandatory")
//    @Email
	private String email;
//	@NotBlank(message = "password is mandatory")
//	@NotNull
//	@Length(max = 100, message = "Password has max-length = 100")
//	@Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Password must be 8 characters including 1 uppercase letter, 1 lowercase letter and numeric characters")
	private String password;
//	@Valid
	private UserInforDto userinforDto;
//	@Valid
	private Role role;
//	@Valid
//	@NotEmpty(message = "permissions must not be empty")
	private Set<Permission> permissions;
	@Enumerated(EnumType.STRING)
    private AuthProvider provider;
	public AccountDtoAdmin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountDtoAdmin(int id, String email, String password, UserInforDto userinforDto, Role role,
			Set<Permission> permissions) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.userinforDto = userinforDto;
		this.role = role;
		this.permissions = permissions;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	
}
