package com.example.Warehouse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.Warehouse.config.AuthToken;
import com.example.Warehouse.config.JwtTokenUtil;
import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.dtos.accountService.AccountDto;
import com.example.Warehouse.dtos.accountService.LoginDto;
import com.example.Warehouse.dtos.accountService.PasswordDto;
import com.example.Warehouse.dtos.accountService.RegisterDto;
import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.accountService.AuthProvider;
import com.example.Warehouse.entities.accountService.Permission;
import com.example.Warehouse.entities.accountService.Role;
import com.example.Warehouse.entities.accountService.UserInfor;
import com.example.Warehouse.exceptions.accountService.AccountIsExistsException;
import com.example.Warehouse.exceptions.accountService.BadRequestException;
import com.example.Warehouse.exceptions.accountService.PasswordIsNotMatchException;
import com.example.Warehouse.mapper.AccountMapper;
import com.example.Warehouse.mapper.RegisterMapper;
import com.example.Warehouse.repositories.accountService.AccountRepository;
import com.example.Warehouse.repositories.accountService.PermissionRepository;
import com.example.Warehouse.repositories.accountService.RoleRepository;
import com.example.Warehouse.services.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountMapper accMap;
	@Autowired
	private JwtTokenUtil tokenProvider;
	@Autowired
	private RegisterMapper registermap;
	@Autowired
	private AccountService accser;

	@PostMapping("/login")
	
	public ResponseEntity<ResponseDto<Object>> authenticateUser(@Valid @RequestBody LoginDto loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = tokenProvider.createToken(authentication);
		ResponseDto<Object> result = new ResponseDto<Object>(new AuthToken(token), HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<ResponseDto<String>> registerUser(@Valid @RequestBody RegisterDto signUpRequest) {
		if (accountRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new AccountIsExistsException();
		}
		if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
			throw new PasswordIsNotMatchException("Your confirm password is not matched");
		}
		// Creating user's account
		Account result = accser.add(registermap.toAccountEntity(signUpRequest));

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/me")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ResponseDto<String>("User registered successfully@", HttpStatus.OK.value()));
	}

	@RequestMapping(value = "/codeId/{codeId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<AccountDto>> getAccountByCodeId(@PathVariable String codeId) {
		Account thisaccount = accser.getByCodeId(codeId);
		AccountDto accountDto = accMap.toAccountDTO(thisaccount);
		ResponseDto<AccountDto> result = new ResponseDto<AccountDto>(accountDto, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<AccountDto>>(result, HttpStatus.OK);
	}

	@PutMapping("/changePass")
	public ResponseEntity<Object> changePassword(@Valid @RequestBody PasswordDto passwordDto) {
		this.accser.changePassWord(passwordDto);
		return new ResponseEntity<Object>("Change Password successfully", HttpStatus.OK);
	}

	@RequestMapping(value = "/verify/{verifyToken}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<AccountDto>> confirmRegisterLocal(@PathVariable String verifyToken) {
		Account result = accser.verifyAccount(verifyToken);
		return new ResponseEntity<ResponseDto<AccountDto>>(
				new ResponseDto<AccountDto>(accMap.toAccountDTO(result), HttpStatus.OK.value()), HttpStatus.OK);
	}

	@GetMapping("/validateToken")
	public ResponseEntity<Object> validateToken(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		return new ResponseEntity<Object>("OK", HttpStatus.OK);
	}
	
	@Scheduled(fixedRate = 600000)
	public void testmethod() {
		System.out.println("ok");
	}
}
