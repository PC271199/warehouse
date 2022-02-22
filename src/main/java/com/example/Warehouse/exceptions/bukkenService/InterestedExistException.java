package com.example.Warehouse.exceptions.bukkenService;

public class InterestedExistException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    public InterestedExistException() {
        super("WareHouse exists in your bookmark list");
    }
}
