package com.example.Warehouse.exceptions.accountService;

public class EmailIsExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    public EmailIsExistsException() {
        super("Email exists");
    }

}
