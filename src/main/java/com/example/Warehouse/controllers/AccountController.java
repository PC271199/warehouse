package com.example.Warehouse.controllers;

import java.io.InputStream;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Warehouse.dtos.AccountDto;
import com.example.Warehouse.dtos.AccountDtoAdmin;
import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.entities.Account;
import com.example.Warehouse.exceptions.ImportFailException;
import com.example.Warehouse.mapper.AccountAdminMapper;
import com.example.Warehouse.mapper.AccountMapper;
import com.example.Warehouse.services.AccountService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
// add comment here
@RestController
@RequestMapping(value = "/rest-account")
public class AccountController {
	@Autowired
	private AccountService accser;
	@Autowired
	private AccountAdminMapper accAdminMap;
	@Autowired
	private AccountMapper accMap;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<String> test() {
		
		return new ResponseEntity<String>("day la test", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<List<AccountDtoAdmin>>> getAccountList() {
		List<Account> accounts = accser.getAll();
		System.out.println(accounts);
		List<AccountDtoAdmin> accountDtos = accAdminMap.toAccountDtoAdmins(accounts);
		ResponseDto<List<AccountDtoAdmin>> result = new ResponseDto<List<AccountDtoAdmin>>(accountDtos,
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<AccountDtoAdmin>>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/accounts/{accountId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<AccountDto>> getAccountById(@PathVariable int accountId) {
		Account thisaccount = accser.getById(accountId);
		AccountDto accountDto = accMap.toAccountDTO(thisaccount);
		ResponseDto<AccountDto> result = new ResponseDto<AccountDto>(accountDto, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<AccountDto>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<AccountDtoAdmin>> createAccount(
			@Valid @RequestBody AccountDtoAdmin accountDtoAdmin) {
		Account thisAccount = accser.createAccount(accAdminMap.toAccountEntity(accountDtoAdmin));
		AccountDtoAdmin accountDto = accAdminMap.toAccountDtoAdmin(thisAccount);
		ResponseDto<AccountDtoAdmin> result = new ResponseDto<AccountDtoAdmin>(accountDto, HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseDto<AccountDtoAdmin>>(result, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/accounts", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDto<AccountDtoAdmin>> editUserInfor(@RequestBody AccountDtoAdmin accountDtoAdmin) {
		Account updatedAccount = accser.updateAccount(accAdminMap.toAccountEntity(accountDtoAdmin));
		AccountDtoAdmin accountDto = accAdminMap.toAccountDtoAdmin(updatedAccount);
		ResponseDto<AccountDtoAdmin> result = new ResponseDto<AccountDtoAdmin>(accountDto, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<AccountDtoAdmin>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/accounts/{accountId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto<Object>> deleteAccountById(@PathVariable int accountId) {
		accser.deleteAccount(accountId);
		ResponseDto<Object> result = new ResponseDto<Object>("Delete successfully", HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/accounts/data", method = RequestMethod.POST)
	public ResponseEntity<Object> importdata() {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<AccountDtoAdmin>> typeReference = new TypeReference<List<AccountDtoAdmin>>() {
		};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/file/data.json");
		try {
			List<AccountDtoAdmin> accounts=mapper.readValue(inputStream, typeReference);
			accser.importAccount(accAdminMap.toAccountEntities(accounts));
		} catch (Exception e) {
			System.out.println(e);
			throw new ImportFailException();
		}
		return new ResponseEntity<Object>("", HttpStatus.OK);
	}
}
