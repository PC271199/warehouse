package com.example.Warehouse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.Warehouse.dtos.LoginDto;
import com.example.Warehouse.dtos.RegisterDto;
import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.entities.Account;
import com.example.Warehouse.entities.AuthProvider;
import com.example.Warehouse.entities.Permission;
import com.example.Warehouse.entities.Role;
import com.example.Warehouse.entities.UserInfor;
import com.example.Warehouse.exceptions.BadRequestException;
import com.example.Warehouse.mapper.AccountMapper;
import com.example.Warehouse.repositories.PermissionRepository;
import com.example.Warehouse.repositories.RoleRepository;
import com.example.Warehouse.services.AccountService;
import com.example.Warehouse.repositories.AccountRepository;

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
	private AccountRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private JwtTokenUtil tokenProvider;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AccountMapper accmap;
	@Autowired
	private AccountService accser;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = tokenProvider.createToken(authentication);
		return ResponseEntity.ok(new AuthToken(token));
	}

	@PostMapping("/signup")
	public ResponseEntity<ResponseDto<String>> registerUser(@Valid @RequestBody RegisterDto signUpRequest) {
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new BadRequestException("Email address already in use.");
		}
		// Creating user's account
		Account user = new Account();
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(signUpRequest.getPassword());
		user.setProvider(AuthProvider.local);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		UserInfor userInfor=new UserInfor();
		userInfor.setName(signUpRequest.getName());
		userInfor.setAccount(user);
		Role role = roleRepository.findById(1).get();
		Set<Permission> permissions = new HashSet<>();
		Permission permission1 = permissionRepository.findById(1).get();
		Permission permission2 = permissionRepository.findById(2).get();
		permissions.add(permission1);
		permissions.add(permission2);
		user.setUserinfor(userInfor);
		user.setRole(role);
		user.setPermissions(permissions);
		Account result = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/me")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ResponseDto<String>("User registered successfully@", HttpStatus.OK.value()));
	}

	@GetMapping("/validateToken")
	public ResponseEntity<Object> validateToken(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		return new ResponseEntity<Object>("OK", HttpStatus.OK);
	}
}
