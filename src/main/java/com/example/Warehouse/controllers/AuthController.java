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
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.exceptions.accountService.AccountIsExistsException;
import com.example.Warehouse.exceptions.accountService.BadRequestException;
import com.example.Warehouse.exceptions.accountService.PasswordIsNotMatchException;
import com.example.Warehouse.mapper.AccountMapper;
import com.example.Warehouse.mapper.RegisterMapper;
import com.example.Warehouse.repositories.accountService.AccountRepository;
import com.example.Warehouse.repositories.accountService.PermissionRepository;
import com.example.Warehouse.repositories.accountService.RoleRepository;
import com.example.Warehouse.services.AccountService;
import com.example.Warehouse.services.BukkenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
	@Autowired
	private BukkenService bukkenservice;

	@PostMapping("/login")

	public ResponseEntity<ResponseDto<Object>> authenticateUser(@Valid @RequestBody LoginDto loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		Account thisAccount = accser.getByUserName(authentication.getName());
		System.out.println(authentication.getAuthorities().toArray()[0].toString());
		if ("ROLE_USER".equals(authentication.getAuthorities().toArray()[0].toString())) {
			if (thisAccount.getMatrix() == null || thisAccount.getMatrix().size() == 0) {
				Map<Integer, Integer> matrix = new HashMap<Integer, Integer>();
				List<Bukken> bukkenList = bukkenservice.getAll();
				for (Bukken bukken : bukkenList) {
					matrix.put(bukken.getId(), 0);
				}
				thisAccount.setMatrix(matrix);
				accser.saveAccount(thisAccount);
			}
		}
//		ZoneId defaultZoneId = ZoneId.systemDefault();
//		List<Bukken> bukkens = bukkenservice.getAll();
//		Random rand = new Random();
//		for (Bukken bukken : bukkens) {
//			LocalDate randomDate = createRandomDate(2010, 2025);
//			bukken.setDeliveryDate(Date.from(randomDate.atStartOfDay(defaultZoneId).toInstant()));
//		}
//		bukkenservice.save(bukkens);
		String token = tokenProvider.createToken(authentication);
		ResponseDto<Object> result = new ResponseDto<Object>(new AuthToken(token), HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}
//	public static int createRandomIntBetween(int start, int end) {
//        return start + (int) Math.round(Math.random() * (end - start));
//    }
//
//    public static LocalDate createRandomDate(int startYear, int endYear) {
//        int day = createRandomIntBetween(1, 28);
//        int month = createRandomIntBetween(1, 12);
//        int year = createRandomIntBetween(startYear, endYear);
//        return LocalDate.of(year, month, day);
//    }
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
