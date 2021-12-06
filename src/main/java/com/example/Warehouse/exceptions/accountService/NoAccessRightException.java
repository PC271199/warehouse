package com.example.Warehouse.exceptions.accountService;

public class NoAccessRightException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    public NoAccessRightException() {
        super("No Access Right");
    }

}
