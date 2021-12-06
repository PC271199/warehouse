package com.example.Warehouse.exceptions.accountService;

public class LoginFailedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginFailedException() {
        super("User or Password is not correct");
    }
}
