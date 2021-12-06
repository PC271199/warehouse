package com.example.Warehouse.exceptions.bukkenService;

public class BukkenNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    public BukkenNotFoundException(int id) {
        super("Bukken "+id+" not found : " );
    }
    public BukkenNotFoundException(String email) {
        super("Bukken "+email+" not found : " );
    }
    public BukkenNotFoundException() {
        super();
    }
}
