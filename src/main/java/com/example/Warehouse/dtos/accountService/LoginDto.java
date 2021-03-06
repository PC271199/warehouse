package com.example.Warehouse.dtos.accountService;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */
public class LoginDto {
	@NotBlank(message = "username is mandatory")
    @Email
    private String email;

	@NotBlank
	@Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Password must be 8 characters including 1 uppercase letter, 1 lowercase letter and numeric characters")
    private String password;

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
}
