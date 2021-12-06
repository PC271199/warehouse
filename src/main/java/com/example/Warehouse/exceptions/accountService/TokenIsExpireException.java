package com.example.Warehouse.exceptions.accountService;

public class TokenIsExpireException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    public TokenIsExpireException() {
        super("Your token is expired");
    }

}
