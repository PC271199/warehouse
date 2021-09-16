package com.example.Warehouse.exceptions;

public class AccountIsExistsException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountIsExistsException(String username) {
        super(username + " exists");
    }
	public AccountIsExistsException() {
       super("Account exists");
    }
	
}
