package com.example.Warehouse.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



public class ResponseDto<T> extends ResponseEntityExceptionHandler {

	private Date timestamp = new Date();
	private String message = "";
	private T data = null;
	private HashMap<String, String> errors;
	private int status;
	public ResponseDto() {
		
	}
	public ResponseDto(MethodArgumentNotValidException ex) {
        this.errors=new HashMap<>();
        List<FieldError> ers=ex.getBindingResult().getFieldErrors();
        for(FieldError i : ers) {
            this.errors.put(i.getField(),i.getDefaultMessage());
        }
    }
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public HashMap<String, String> getErrors() {
		return errors;
	}

	public void setErrors(HashMap<String, String> errors) {
		this.errors = errors;
	}

	public ResponseDto(String message, T data) {
		super();
		this.message = message;
		this.data = data;
	}

	public ResponseDto(T data, int status) {
		super();
		this.data = data;
		this.status=status;
	}
	public ResponseDto(HashMap<String, String> errors) {
		this.errors=errors;
	}
}
