package com.example.Warehouse.exceptions.common;

public class NullException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NullException() {
        super("Null Response");
    }
    
}
