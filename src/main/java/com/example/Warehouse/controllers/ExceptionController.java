package com.example.Warehouse.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.exceptions.accountService.AccountIsExistsException;
import com.example.Warehouse.exceptions.accountService.AccountNotFoundException;
import com.example.Warehouse.exceptions.accountService.EmailIsExistsException;
import com.example.Warehouse.exceptions.accountService.EmptyException;
import com.example.Warehouse.exceptions.accountService.ImportFailException;
import com.example.Warehouse.exceptions.accountService.LoginFailedException;
import com.example.Warehouse.exceptions.accountService.NoAccessRightException;
import com.example.Warehouse.exceptions.accountService.PasswordIsNotMatchException;
import com.example.Warehouse.exceptions.accountService.TokenIsExpireException;
import com.example.Warehouse.exceptions.bukkenService.BukkenNotFoundException;
import com.example.Warehouse.exceptions.bukkenService.InterestedExistException;
import com.example.Warehouse.exceptions.common.NullException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", status.value());

		// Get all errors
		HashMap<String, String> error = new HashMap<>();
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			error.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
//		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
//				.collect(Collectors.toList());

		body.put("error", error);
		ResponseDto<Object> result = new ResponseDto<Object>();
		result.setData(null);
		result.setErrors(error);
		return new ResponseEntity<Object>(result, headers, status);

	}

	@ExceptionHandler(value = EmptyException.class)
	public ResponseEntity<Object> exception(EmptyException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AccountNotFoundException.class)
	public ResponseEntity<Object> exception(AccountNotFoundException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EmailIsExistsException.class)
	public ResponseEntity<Object> exception(EmailIsExistsException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = AccountIsExistsException.class)
	public ResponseEntity<Object> exception(AccountIsExistsException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = PasswordIsNotMatchException.class)
	public ResponseEntity<Object> exception(PasswordIsNotMatchException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ImportFailException.class)
	public ResponseEntity<Object> exception(ImportFailException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = LoginFailedException.class)
	public ResponseEntity<Object> exception(LoginFailedException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = TokenIsExpireException.class)
	public ResponseEntity<Object> exception(TokenIsExpireException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(value = BukkenNotFoundException.class)
	public ResponseEntity<Object> exception(BukkenNotFoundException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = NoAccessRightException.class)
	public ResponseEntity<Object> exception(NoAccessRightException exception) {
		return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> exception(MaxUploadSizeExceededException exception) {
        return new ResponseEntity<Object>("File too large!", HttpStatus.EXPECTATION_FAILED);
    }
	@ExceptionHandler(NullException.class)
    public ResponseEntity<Object> exception(NullException exception) {
        return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(InterestedExistException.class)
    public ResponseEntity<Object> exception(InterestedExistException exception) {
        return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
