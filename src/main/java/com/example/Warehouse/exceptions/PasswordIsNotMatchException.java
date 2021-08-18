package com.example.Warehouse.exceptions;

public class PasswordIsNotMatchException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public PasswordIsNotMatchException() {
		super("Password is not matched");
	}
	
}
