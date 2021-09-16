package com.example.Warehouse.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */

public class RegisterDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
	@Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Password must be 8 characters including 1 uppercase letter, 1 lowercase letter and numeric characters")
    private String password;
    
    @NotBlank
    private String confirmPassword;


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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
    
}
