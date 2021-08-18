package com.example.Warehouse.exceptions;

public class EmptyException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmptyException() {
        super("Response List is empty");
    }
    
}
