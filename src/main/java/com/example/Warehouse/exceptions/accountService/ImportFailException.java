package com.example.Warehouse.exceptions.accountService;

public class ImportFailException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImportFailException() {
        super("Import Fail exception");
    }
    
}
