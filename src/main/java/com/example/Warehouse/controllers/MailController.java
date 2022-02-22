package com.example.Warehouse.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.exceptions.accountService.AccountNotFoundException;
import com.example.Warehouse.pojo.Mail;
import com.example.Warehouse.repositories.accountService.AccountRepository;
import com.example.Warehouse.services.MailService;
import com.example.Warehouse.services.MailServiceImpl;
import com.example.Warehouse.util.PasswordUtil;

// add comment here
@RestController
@RequestMapping(value = "/rest-mail")
public class MailController {
	@Autowired
	MailServiceImpl mailServiceImpl;
	@Autowired
	private AccountRepository accRepo;
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<Object>> createScheduleBukkenUser(HttpServletRequest request,@RequestBody Mail mail) {
		Optional<Account> oldaccount = this.accRepo.findByEmail(mail.getMailTo());
		if (!oldaccount.isPresent()) {
			throw new AccountNotFoundException(mail.getMailTo());
		}
		Account accountEntityDB = oldaccount.get();
		try {
			String codeId = PasswordUtil.generatePswd(40, 50, 10, 30, 0);
			accountEntityDB.setCodeId(codeId);
			accountEntityDB.setCodeIdExpired(true);
			accRepo.save(accountEntityDB);
			mail.setMailContent(request.getHeader("Origin")+"/changePassword/"+codeId);
			MailService mailService = (MailService) mailServiceImpl;
			mailService.sendEmailPassword(mail);
			
			ResponseDto<Object> result = new ResponseDto<Object>("abcd", HttpStatus.OK.value());
			return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
	}
}
