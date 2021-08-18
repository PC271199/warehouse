package com.example.Warehouse.controllers;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.exceptions.AccountIsExistsException;
import com.example.Warehouse.exceptions.AccountNotFoundException;
import com.example.Warehouse.exceptions.EmailIsExistsException;
import com.example.Warehouse.exceptions.EmptyException;
import com.example.Warehouse.exceptions.ImportFailException;
import com.example.Warehouse.exceptions.LoginFailedException;
import com.example.Warehouse.exceptions.PasswordIsNotMatchException;

@ControllerAdvice
public class AccountExceptionController extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", status.value());

		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("errors", errors);
		ResponseDto<Object> result=new ResponseDto<Object>();
		result.setData(null);
		result.setErrors(errors);
		return new ResponseEntity<Object>(result, headers, status);

	}

	@ExceptionHandler(value = EmptyException.class)
	public ResponseEntity<Object> exception(EmptyException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = AccountNotFoundException.class)
	public ResponseEntity<Object> exception(AccountNotFoundException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = EmailIsExistsException.class)
	public ResponseEntity<Object> exception(EmailIsExistsException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = AccountIsExistsException.class)
	public ResponseEntity<Object> exception(AccountIsExistsException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = PasswordIsNotMatchException.class)
	public ResponseEntity<Object> exception(PasswordIsNotMatchException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(value = ImportFailException.class)
	public ResponseEntity<Object> exception(ImportFailException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(value = LoginFailedException.class)
	public ResponseEntity<Object> exception(LoginFailedException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
}
