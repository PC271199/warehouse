package com.example.Warehouse.exceptions.accountService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PasswordIsNotMatchException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public PasswordIsNotMatchException() {
		super("Password is not matched");
	}
	public PasswordIsNotMatchException(String message) {
		super(message);
	}
	
}
