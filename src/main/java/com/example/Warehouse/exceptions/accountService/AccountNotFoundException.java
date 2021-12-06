package com.example.Warehouse.exceptions.accountService;

public class AccountNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    public AccountNotFoundException(int id) {
        super("User "+id+" not found : " );
    }
    public AccountNotFoundException(String email) {
        super("User "+email+" not found : " );
    }
    public AccountNotFoundException() {
        super();
    }
}
