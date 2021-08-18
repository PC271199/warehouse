package com.example.Warehouse.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PasswordDto {
	public PasswordDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PasswordDto(@NotBlank(message = "password can't be blank") @NotNull int accountId,
			@Length(max = 100, message = "Password has max-length = 100") @NotBlank(message = "password can't be blank") @NotNull String oldPassword,
			@Length(max = 100, message = "Password has max-length = 100") @NotBlank(message = "password can't be blank") @NotNull String newPassword,
			String confirmPassword) {
		super();
		this.accountId = accountId;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}

	@NotBlank(message = "password can't be blank")
	@NotNull
	private int accountId;

	@Length(max = 100, message = "Password has max-length = 100")
	@NotBlank(message = "password can't be blank")
	@NotNull
	private String oldPassword;

	@Length(max = 100, message = "Password has max-length = 100")
	@NotBlank(message = "password can't be blank")
	@NotNull
	private String newPassword;

	private String confirmPassword;

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
