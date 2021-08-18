package com.example.Warehouse.exceptions;

public class AccountNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    public AccountNotFoundException(int id) {
        super("User "+id+" not found : " );
    }
}
