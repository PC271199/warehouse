package com.example.Warehouse.controllers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Warehouse.dtos.MailContractDto;
import com.example.Warehouse.dtos.ResponseDto;
import com.example.Warehouse.dtos.accountService.AccountDto;
import com.example.Warehouse.dtos.accountService.AccountDtoAdmin;
import com.example.Warehouse.dtos.accountService.AccountEditDto;
import com.example.Warehouse.dtos.accountService.AccountEditDtoAdmin;
import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.accountService.Renter;
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.exceptions.accountService.ImportFailException;
import com.example.Warehouse.exceptions.accountService.NoAccessRightException;
import com.example.Warehouse.exceptions.common.NullException;
import com.example.Warehouse.mapper.AccountAdminMapper;
import com.example.Warehouse.mapper.AccountEditDtoAdminMapper;
import com.example.Warehouse.mapper.AccountMapper;
import com.example.Warehouse.repositories.accountService.RenterRepository;
import com.example.Warehouse.services.AccountService;
import com.example.Warehouse.services.BukkenService;
import com.example.Warehouse.services.RenterService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// add comment here
@RestController
@RequestMapping(value = "/rest-account")
public class AccountController {
	public static final String encodedKeys = "ZjMyNDg0NWYtYzUyYy00MDg4LWE5ZjYtZmI2Njc1MDkwMjI2OmQ0ZTdjYWVkLTA3NzQtNGNlNS05ZGFlLTRmMzRiMTQ0MTI0Yg==";
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private AccountService accser;
	@Autowired
	private BukkenService bukkenser;
	@Autowired
	private AccountAdminMapper accAdminMap;
	@Autowired
	private AccountEditDtoAdminMapper accEditAdminMap;
	@Autowired
	private AccountMapper accMap;
	@Autowired
	private RenterService renterservice;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/accounts", method = RequestMethod.GET)

	public ResponseEntity<ResponseDto<List<AccountDtoAdmin>>> getAccountList() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Account thisaccount = accser.getByUserName(authentication.getName());
		if (accser.checkaction(thisaccount.getPermissions(), "ACCOUNT_READ") == false) {
			throw new NoAccessRightException();
		}
		List<Account> accounts = accser.getAll();
		List<AccountDtoAdmin> accountDtos = accAdminMap.toAccountDtoAdmins(accounts);
		ResponseDto<List<AccountDtoAdmin>> result = new ResponseDto<List<AccountDtoAdmin>>(accountDtos,
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<List<AccountDtoAdmin>>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/accounts/page/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Page<AccountDtoAdmin>>> getAccountListByPage(@PathVariable int pageIndex) {
		Page<AccountDtoAdmin> accounts = accser.getAllByPage(pageIndex);
		ResponseDto<Page<AccountDtoAdmin>> result = new ResponseDto<Page<AccountDtoAdmin>>(accounts,
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Page<AccountDtoAdmin>>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/accounts/count", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<Long>> countAll() {
		long count = accser.countAll();
		ResponseDto<Long> result = new ResponseDto<Long>(count, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Long>>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/accounts/{accountId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto<AccountDtoAdmin>> getAccountById(@PathVariable int accountId) {
		Account thisaccount = accser.getById(accountId);
		AccountDtoAdmin accountDto = accAdminMap.toAccountDtoAdmin(thisaccount);
		ResponseDto<AccountDtoAdmin> result = new ResponseDto<AccountDtoAdmin>(accountDto, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<AccountDtoAdmin>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto<AccountDtoAdmin>> createAccount(
			@Valid @RequestBody AccountDtoAdmin accountDtoAdmin) {
		Account thisAccount = accser.createAccount(accAdminMap.toAccountEntity(accountDtoAdmin));
		AccountDtoAdmin accountDto = accAdminMap.toAccountDtoAdmin(thisAccount);
		ResponseDto<AccountDtoAdmin> result = new ResponseDto<AccountDtoAdmin>(accountDto, HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseDto<AccountDtoAdmin>>(result, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/accounts", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDto<AccountDtoAdmin>> updateAccount(
			@RequestBody AccountEditDtoAdmin accountEditDtoAdmin) {
		Account updatedAccount = accser.updateAccount(accEditAdminMap.toAccountEntity(accountEditDtoAdmin));
		AccountDtoAdmin accountDto = accAdminMap.toAccountDtoAdmin(updatedAccount);
		ResponseDto<AccountDtoAdmin> result = new ResponseDto<AccountDtoAdmin>(accountDto, HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<AccountDtoAdmin>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/accounts/{accountId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto<Object>> deleteAccountById(@PathVariable int accountId) {
		accser.deleteAccount(accountId);
		ResponseDto<Object> result = new ResponseDto<Object>("Delete successfully", HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/accounts", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto<Object>> deleteAll() {
		accser.deleteAll();
		ResponseDto<Object> result = new ResponseDto<Object>("Delete successfully", HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/accounts/toggleStatus/{accountId}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDto<Object>> toggleEnabledAccount(@PathVariable int accountId) {
		accser.toggleEnabledAccount(accountId);
		ResponseDto<Object> result = new ResponseDto<Object>("Toggle enabled account successfully",
				HttpStatus.OK.value());
		return new ResponseEntity<ResponseDto<Object>>(result, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/accounts/data", method = RequestMethod.POST)
	public ResponseEntity<Object> importdata() {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<AccountDtoAdmin>> typeReference = new TypeReference<List<AccountDtoAdmin>>() {
		};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/file/data.json");
		try {
			List<AccountDtoAdmin> accounts = mapper.readValue(inputStream, typeReference);
			accser.importAccount(accAdminMap.toAccountEntities(accounts));
		} catch (Exception e) {
			System.out.println(e);
			throw new ImportFailException();
		}
		return new ResponseEntity<Object>("", HttpStatus.OK);
	}

	@RequestMapping(value = "/editUserInfor", method = RequestMethod.PUT)
	public ResponseEntity<ResponseDto<AccountDto>> editUserInfor(@Valid @RequestBody AccountEditDto accountEditDto) {
		Account result = accser.edit(accountEditDto);
		return new ResponseEntity<ResponseDto<AccountDto>>(
				new ResponseDto<AccountDto>(accMap.toAccountDTO(result), HttpStatus.OK.value()), HttpStatus.OK);
	}

	@RequestMapping(value = "/authenDocsign/{code}/{bukkenId}", method = RequestMethod.POST)
	public ResponseEntity<Object> authenAndSendDocsign(HttpServletRequest request, @PathVariable String code,
			@PathVariable int bukkenId, @RequestBody MailContractDto mailContractDto) throws Exception {
		// get accessToken-Docsign
		try {
			HttpHeaders headers = new HttpHeaders();
			String authHeader = "Basic " + encodedKeys;
			headers.set("Authorization", authHeader);
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
			// Y??u c???u tr??? v??? ?????nh d???ng JSON
			headers.setContentType(MediaType.APPLICATION_JSON);
			JSONObject object = new JSONObject();
			object.put("code", code);
			object.put("grant_type", "authorization_code");
			HttpEntity<String> entity = new HttpEntity<String>(object.toString(), headers);
			// RestTemplate
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange("https://account-d.docusign.com/oauth/token",
					HttpMethod.POST, entity, String.class);
			JsonNode root = objectMapper.readTree(response.getBody());
			System.out.println(root.get("access_token").asText());
			// send contract
			HttpHeaders headers2 = new HttpHeaders();
			String authHeader2 = "Bearer " + root.get("access_token").asText();
			headers2.set("Authorization", authHeader2);
			headers2.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
			// Y??u c???u tr??? v??? ?????nh d???ng JSON
			headers2.setContentType(MediaType.APPLICATION_JSON);
			JSONObject object2 = new JSONObject();
			List<JSONObject> objectDocumentList = new ArrayList<JSONObject>();
			JSONObject objectDocument = new JSONObject();
			objectDocument.put("documentBase64",
					"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");
			objectDocument.put("documentId", "1");
			objectDocument.put("fileExtension", "docx");
			objectDocument.put("name", "document");
			objectDocumentList.add(objectDocument);
			object2.put("documents", new JSONArray(objectDocumentList));
			object2.put("emailSubject", "Simple Signing Example");

			JSONObject objectRecipients = new JSONObject();
			List<JSONObject> singerList = new ArrayList<JSONObject>();
			// singer1
			JSONObject signer1 = new JSONObject();
			System.out.println(mailContractDto.getFrom());
			signer1.put("email", mailContractDto.getFrom());
			signer1.put("name", "cong");
			signer1.put("recipientId", "1");
			JSONObject tab1 = new JSONObject();
			List<JSONObject> signHereTabs1 = new ArrayList<JSONObject>();
			JSONObject signHereTab = new JSONObject();
			signHereTab.put("anchorString", "/sn1/");
			signHereTab.put("anchorXOffset", "10");
			signHereTab.put("anchorYOffset", "10");
			signHereTab.put("anchorUnits", "pixels");
			signHereTab.put("tabId", "1");
			signHereTabs1.add(signHereTab);
			tab1.put("signHereTabs", new JSONArray(signHereTabs1));
			signer1.put("tabs", tab1);
			// singer2
			JSONObject signer2 = new JSONObject();
			signer2.put("email", mailContractDto.getTo());
			signer2.put("name", "cong");
			signer2.put("recipientId", "2");
			JSONObject tab2 = new JSONObject();
			List<JSONObject> signHereTabs2 = new ArrayList<JSONObject>();
			JSONObject signHereTab2 = new JSONObject();
			signHereTab2.put("anchorString", "/sn2/");
			signHereTab2.put("anchorXOffset", "10");
			signHereTab2.put("anchorYOffset", "10");
			signHereTab2.put("anchorUnits", "pixels");
			signHereTab2.put("tabId", "2");
			signHereTabs2.add(signHereTab2);
			tab2.put("signHereTabs", new JSONArray(signHereTabs2));
			signer2.put("tabs", tab2);
			// singerList
			singerList.add(signer1);
			singerList.add(signer2);
			objectRecipients.put("signers", new JSONArray(singerList));
			object2.put("recipients", objectRecipients);
			object2.put("status", "sent");
			HttpEntity<String> entity2 = new HttpEntity<String>(object2.toString(), headers2);
			// RestTemplate
			ResponseEntity<String> response2 = restTemplate.exchange(
					"https://demo.docusign.net/restapi/v2.1/accounts/726c5825-36d7-4b6b-a1c0-f97527c2cac4/envelopes",
					HttpMethod.POST, entity2, String.class);
			JsonNode root2 = objectMapper.readTree(response2.getBody());
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Account account = accser.getByUserName(authentication.getName());
			Bukken bukken = bukkenser.getById(bukkenId);
			if (renterservice.findbyAccountId_BukkenId(account.getId(), bukken.getId()) != null) {
				return new ResponseEntity<Object>("You have already signed contract with this warehouse",
						HttpStatus.CONFLICT);
			} else {
				Renter thisRenter = new Renter();
				thisRenter.setAccount(account);
				thisRenter.setBukken(bukken);
				bukkenser.increaseCountSign(bukkenId);
				if (account.getUserinfor() != null) {
					if (account.getUserinfor().getFullname() != null) {
						thisRenter.setFullname(account.getUserinfor().getFullname());
					}
					if (account.getUserinfor().getCompanyname() != null) {
						thisRenter.setCompanyname(account.getUserinfor().getCompanyname());
					}
					if (account.getUserinfor().getPhonenumber() != null) {
						thisRenter.setPhonenumber(account.getUserinfor().getPhonenumber());
					}
					thisRenter.setMailuser(account.getEmail());
				}
				renterservice.save(thisRenter);
				return new ResponseEntity<Object>(root, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw e;
		}

	}

	@RequestMapping(value = "/sendContract/{accessTokenDocsign}", method = RequestMethod.GET)
	public ResponseEntity<Object> testAPI2(@PathVariable String accessTokenDocsign) throws Exception {
		try {
			HttpHeaders headers = new HttpHeaders();
			String authHeader = "Bearer " + accessTokenDocsign;
			headers.set("Authorization", authHeader);
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
			// Y??u c???u tr??? v??? ?????nh d???ng JSON
			headers.setContentType(MediaType.APPLICATION_JSON);
			JSONObject object = new JSONObject();
			List<JSONObject> objectDocumentList = new ArrayList<JSONObject>();
			JSONObject objectDocument = new JSONObject();
			objectDocument.put("documentBase64",
					"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");
			objectDocument.put("documentId", "1");
			objectDocument.put("fileExtension", "docx");
			objectDocument.put("name", "document");
			objectDocumentList.add(objectDocument);
			object.put("documents", new JSONArray(objectDocumentList));
			object.put("emailSubject", "Simple Signing Example");

			JSONObject objectRecipients = new JSONObject();
			List<JSONObject> singerList = new ArrayList<JSONObject>();
			// singer1
			JSONObject signer1 = new JSONObject();
			signer1.put("email", "phuoccong99@gmail.com");
			signer1.put("name", "cong");
			signer1.put("recipientId", "1");
			JSONObject tab1 = new JSONObject();
			List<JSONObject> signHereTabs1 = new ArrayList<JSONObject>();
			JSONObject signHereTab = new JSONObject();
			signHereTab.put("anchorString", "/sn1/");
			signHereTab.put("anchorXOffset", "10");
			signHereTab.put("anchorYOffset", "10");
			signHereTab.put("anchorUnits", "pixels");
			signHereTab.put("tabId", "1");
			signHereTabs1.add(signHereTab);
			tab1.put("signHereTabs", new JSONArray(signHereTabs1));
			signer1.put("tabs", tab1);
			// singer2
			JSONObject signer2 = new JSONObject();
			signer2.put("email", "lyphuoccong2711@gmail.com");
			signer2.put("name", "cong");
			signer2.put("recipientId", "2");
			JSONObject tab2 = new JSONObject();
			List<JSONObject> signHereTabs2 = new ArrayList<JSONObject>();
			JSONObject signHereTab2 = new JSONObject();
			signHereTab2.put("anchorString", "/sn2/");
			signHereTab2.put("anchorXOffset", "10");
			signHereTab2.put("anchorYOffset", "10");
			signHereTab2.put("anchorUnits", "pixels");
			signHereTab2.put("tabId", "2");
			signHereTabs2.add(signHereTab2);
			tab2.put("signHereTabs", new JSONArray(signHereTabs2));
			signer2.put("tabs", tab2);
			// singerList
			singerList.add(signer1);
			singerList.add(signer2);
			objectRecipients.put("signers", new JSONArray(singerList));
			object.put("recipients", objectRecipients);
			object.put("status", "sent");
			HttpEntity<String> entity = new HttpEntity<String>(object.toString(), headers);
			// RestTemplate
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(
					"https://demo.docusign.net/restapi/v2.1/accounts/726c5825-36d7-4b6b-a1c0-f97527c2cac4/envelopes",
					HttpMethod.POST, entity, String.class);
			JsonNode root = objectMapper.readTree(response.getBody());

			return new ResponseEntity<Object>(root, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
		}
	}

	@RequestMapping(value = "/test/flask", method = RequestMethod.GET)
	public ResponseEntity<Object> testApiFlask() throws JsonMappingException, JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		// Y??u c???u tr??? v??? ?????nh d???ng JSON
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		ResponseEntity<String> response = restTemplate.exchange("http://localhost:5000/api/v1/hello/{a}",
				HttpMethod.GET, entity, String.class, 1);
//		JsonNode root = objectMapper.readTree(response.getBody());
		System.out.println(response);
		return new ResponseEntity<Object>(new ResponseDto<Object>("ac", HttpStatus.OK.value()), HttpStatus.OK);
	}

}
