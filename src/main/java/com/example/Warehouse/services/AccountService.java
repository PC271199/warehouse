package com.example.Warehouse.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Warehouse.dtos.accountService.AccountDtoAdmin;
import com.example.Warehouse.dtos.accountService.AccountEditDto;
import com.example.Warehouse.dtos.accountService.PasswordDto;
import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.accountService.AuthProvider;
import com.example.Warehouse.entities.accountService.Permission;
import com.example.Warehouse.entities.accountService.Role;
import com.example.Warehouse.entities.accountService.UserInfor;
import com.example.Warehouse.entities.accountService.VerificationToken;
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.bukkenService.InterestedBukken;
import com.example.Warehouse.exceptions.accountService.AccountIsExistsException;
import com.example.Warehouse.exceptions.accountService.AccountNotFoundException;
import com.example.Warehouse.exceptions.accountService.EmptyException;
import com.example.Warehouse.exceptions.accountService.ImportFailException;
import com.example.Warehouse.exceptions.accountService.PasswordIsNotMatchException;
import com.example.Warehouse.exceptions.accountService.TokenIsExpireException;
import com.example.Warehouse.exceptions.common.NullException;
import com.example.Warehouse.mapper.AccountAdminMapper;
import com.example.Warehouse.pojo.Mail;
import com.example.Warehouse.repositories.accountService.AccountRepository;
import com.example.Warehouse.repositories.accountService.PermissionRepository;
import com.example.Warehouse.repositories.accountService.RoleRepository;
import com.example.Warehouse.repositories.accountService.UserRepository;
import com.example.Warehouse.repositories.accountService.VerificationRepository;
import com.example.Warehouse.util.PasswordUtil;

@Service
public class AccountService {
	private static final String MAIL_FROM = "phuoccong99@gmail.com";
	@Autowired
	private AccountRepository accRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private AccountAdminMapper accAdminMap;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private VerificationRepository verifyRepo;
	@Autowired
	MailServiceImpl mailServiceImpl;

	public void saveAccount(Account account) {
		accRepo.save(account);
	}

	public Account changeAccount(Account oldaccount, Account newaccount) {
		oldaccount.getUserinfor().setFullname(newaccount.getUserinfor().getFullname());
		oldaccount.getUserinfor().setAddress(newaccount.getUserinfor().getAddress());
		oldaccount.getUserinfor().setDateOfBirth(newaccount.getUserinfor().getDateOfBirth());
		oldaccount.getUserinfor().setCmnd(newaccount.getUserinfor().getCmnd());
		return oldaccount;
	}

	public List<Account> getAll() {
		List<Account> result = accRepo.findAll();
		if (result.size() > 0) {
			return result;
		} else {
//			throw new EmptyException();
			return result;
		}
	}

	public Page<AccountDtoAdmin> getAllByPage(int pageIndex) {
		Page<Account> result = accRepo.findAll(PageRequest.of(pageIndex, 10));
		Page<AccountDtoAdmin> resultDto = result.map(account -> {
			AccountDtoAdmin accountDtoAdmin = accAdminMap.toAccountDtoAdmin(account);
			return accountDtoAdmin;
		});
		if (result.getSize() > 0) {
			return resultDto;
		} else {
			throw new EmptyException();
		}
	}

	public Account getById(int accountId) {
		Optional<Account> result = accRepo.findById(accountId);
		if (result.isPresent()) {
			return result.get();
		} else {
			throw new AccountNotFoundException(accountId);
		}
	}

	public Account getByCodeId(String codeId) {
		List<Account> result = accRepo.findByCodeId(codeId);
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			throw new AccountNotFoundException("");
		}
	}

	public long countAll() {
		return accRepo.count();
	}

	public Account getByUserName(String username) {
		Optional<Account> account = this.accRepo.findByEmail(username);
		if (account.isPresent()) {
			return account.get();
		} else {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
	}

	public Account createAccount(Account account) {
		Optional<Account> oldaccount = this.accRepo.findByEmail(account.getEmail());
		if (oldaccount.isPresent()) {
			throw new AccountIsExistsException(account.getEmail());
		}
		account.setProvider(AuthProvider.local);
		account.getUserinfor().setAccount(account);
		account.setPassword(encoder.encode(account.getPassword()));
		account.setEnabled(true);
		accRepo.save(account);
		return account;
	}

	public Account add(Account account) {
		Optional<Account> accountEntityFromDataBase = this.accRepo.findByEmail(account.getEmail());
		if (accountEntityFromDataBase.isPresent()) {
			throw new AccountIsExistsException();
		}

		account.setProvider(AuthProvider.local);
		account.setPassword(encoder.encode(account.getPassword()));
		UserInfor userInfor = new UserInfor();
		userInfor.setAccount(account);
		Role role = roleRepository.findById(1).get();
		Set<Permission> permissions = new HashSet<>();
		Permission permission1 = permissionRepository.findById(1).get();
		Permission permission2 = permissionRepository.findById(2).get();
		permissions.add(permission1);
		permissions.add(permission2);
		account.setUserinfor(userInfor);
		account.setRole(role);
		account.setPermissions(permissions);
		String codeId = PasswordUtil.generatePswd(40, 50, 10, 30, 0);
		account.setCodeId(codeId);
		String verifyToken = UUID.randomUUID().toString();
		VerificationToken verificationToken = createVerificationToken(verifyToken);
		verificationToken.setAccount(account);
		account.setVerificationToken(verificationToken);
		Account thisaccount = accRepo.save(account);
		Mail mail = new Mail();

		mail.setMailFrom(MAIL_FROM);
		mail.setMailTo(account.getEmail());
		mail.setMailContent("Click the following link to complete your registration" + "\n"
				+ "http://localhost:3000/verifyAccount/" + verifyToken);

		try {
			MailService mailService = (MailService) mailServiceImpl;
			mailService.sendEmailPassword(mail);
		} catch (Exception e) {
			throw e;
		}
		return thisaccount;
	}

	public Account updateAccount(Account account) {
		Optional<Account> oldaccount = this.accRepo.findByEmail(account.getEmail());
		if (!oldaccount.isPresent()) {
			throw new AccountNotFoundException(account.getEmail());
		}
		Account thisaccount = oldaccount.get();
		thisaccount.setRole(account.getRole());
		thisaccount.setPermissions(account.getPermissions());
//		Account result = this.changeAccount(thisaccount, account);
		accRepo.save(thisaccount);
		return thisaccount;
	}

	public void toggleEnabledAccount(int accountId) {
		Optional<Account> oldaccount = this.accRepo.findById(accountId);
		if (!oldaccount.isPresent()) {
			throw new AccountNotFoundException(accountId);
		}
		Account thisaccount = oldaccount.get();
		thisaccount.setEnabled(!thisaccount.isEnabled());
		accRepo.save(thisaccount);
	}

	public void deleteAccount(int accountId) {
		Optional<Account> oldaccount = this.accRepo.findById(accountId);
		if (!oldaccount.isPresent()) {
			throw new AccountNotFoundException(accountId);
		}
		accRepo.deleteById(accountId);
	}

	public void deleteAll() {
		accRepo.deleteAll();
	}

	public void changePassWord(PasswordDto passwordDto) {
		Optional<Account> oldaccount = this.accRepo.findByEmail(passwordDto.getEmail());
		if (!oldaccount.isPresent()) {
			throw new AccountNotFoundException(passwordDto.getEmail());
		}
		Account accountEntityDB = oldaccount.get();
		if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
			throw new PasswordIsNotMatchException();
		}
		accountEntityDB.setPassword(encoder.encode(passwordDto.getNewPassword()));
		this.accRepo.save(accountEntityDB);
	}

//	public void changePassWordInitial(PasswordDto passwordDto) {
//		List<Account> oldaccount = this.accRepo.findByCodeId(passwordDto.getCodeId());
//		if (oldaccount.size() == 0) {
//			throw new AccountNotFoundException("");
//		}
//		Account accountEntityDB = oldaccount.get(0);
//		if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
//			throw new PasswordIsNotMatchException();
//		}
//		accountEntityDB.setPassword(encoder.encode(passwordDto.getNewPassword()));
//		this.accRepo.save(accountEntityDB);
//	}

	public void importAccount(List<Account> accounts) {
		for (Account account : accounts) {
			if (this.accRepo.findByEmail(account.getEmail()).isPresent()) {
				throw new AccountIsExistsException();
			}
			account.getUserinfor().setAccount(account);
			account.setPassword(encoder.encode(account.getPassword()));
		}
		List<Account> result = null;
		result = accRepo.saveAll(accounts);
		if (result == null) {
			throw new ImportFailException();
		}
	}

	public Account edit(AccountEditDto accountEditDto) {
		Optional<Account> oldaccount = this.accRepo.findByEmail(accountEditDto.getEmail());
		if (!oldaccount.isPresent()) {
			throw new AccountNotFoundException(accountEditDto.getEmail());
		}
		Account accountEntityDB = oldaccount.get();
		accountEntityDB.getUserinfor().setFullname(accountEditDto.getFullname());
		accountEntityDB.getUserinfor().setAddress(accountEditDto.getAddress());
		accountEntityDB.getUserinfor().setDateOfBirth(accountEditDto.getDateOfBirth());
		accountEntityDB.getUserinfor().setFirstname(accountEditDto.getFirstname());
		accountEntityDB.getUserinfor().setLastname(accountEditDto.getLastname());
		accountEntityDB.getUserinfor().setPhonenumber(accountEditDto.getPhonenumber());
		accountEntityDB.getUserinfor().setCompanyname(accountEditDto.getCompanyname());
		accountEntityDB.getUserinfor().setProvincecode(accountEditDto.getProvincecode());
		accountEntityDB.getUserinfor().setExp(accountEditDto.getExp());
		accountEntityDB.getUserinfor().setAdddetail(accountEditDto.getAdddetail());
		Account result = accRepo.save(accountEntityDB);
		return result;
	}

	public Account updateImgAva(String imgURL) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account accountEntity = thisAccount.get();
		accountEntity.getUserinfor().setImgURL(imgURL);
		return accRepo.save(accountEntity);
	}

	public Account getAccount(String verificationToken) {
		Account account = verifyRepo.findByToken(verificationToken).get().getAccount();
		return account;
	}

	public VerificationToken getVerificationToken(String VerificationToken) {
		return verifyRepo.findByToken(VerificationToken).get();
	}

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}

	public VerificationToken createVerificationToken(String token) {
		VerificationToken myToken = new VerificationToken(token);
		myToken.setExpiryDate(calculateExpiryDate(5));
		return myToken;
	}

	public Account verifyAccount(String token) {
		Optional<VerificationToken> myToken = verifyRepo.findByToken(token);
		if (!myToken.isPresent()) {
			throw new AccountNotFoundException();
		}
		Account result = (myToken.get().getAccount());

		if (result != null) {
			Calendar cal = Calendar.getInstance();
			if ((myToken.get().getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
				throw new TokenIsExpireException();
			}
			result.setEnabled(true);
			accRepo.save(result);
			return result;
		} else {
			throw new AccountNotFoundException();
		}
	}

	public boolean checkaction(Set<Permission> permissions, String activity) {
		boolean flag = false;
		for (Permission permission : permissions) {
			if (permission.getActname().contains(activity)) {
				flag = true;
			}
		}
		return flag == true ? true : false;
	}

	// list user id have the most similarity
	public List<Integer> getUsersSimilarity() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> optionalAccount = accRepo.findByEmail(authentication.getName());
		if (optionalAccount.isPresent() == false) {
			throw new NullException();
		}
		Account thisAccount = optionalAccount.get();

		List<Account> accountList = accRepo.findRoleUser_NotMine(thisAccount.getId());
		Map<Integer, Integer> myMatrix = thisAccount.getMatrix();
		Map<Integer, Integer> resultMatrix = new HashMap<Integer, Integer>();
		for (Account account : accountList) {
			if (account.getMatrix() != null && account.getMatrix().size() != 0) {
				Map<Integer, Integer> thisMatrix = account.getMatrix();
				int sum = 0;
				for (int key : myMatrix.keySet().stream().collect(Collectors.toList())) {
					sum += myMatrix.get(key) * thisMatrix.get(key);
				}
				resultMatrix.put(account.getId(), sum);
			}
		}
		LinkedHashMap<Integer, Integer> sortedMap = new LinkedHashMap<>();

		resultMatrix.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

		List<Integer> firstNElementsList = sortedMap.keySet().stream().collect(Collectors.toList()).stream().limit(5)
				.collect(Collectors.toList());
		return firstNElementsList;
	}

	// get bukken of users similarity
	public List<Bukken> getBukkensSimilarity(List<Integer> userIds) {
		List<Bukken> result = new ArrayList<Bukken>();
		for (Integer userId : userIds) {
			Account thisAccount = getById(userId);
			if (thisAccount.getInterestedBukkens() != null && thisAccount.getInterestedBukkens().size() > 0) {
				List<InterestedBukken> interestedBukkens = thisAccount.getInterestedBukkens().stream()
						.collect(Collectors.toList());
				for (InterestedBukken interestedBukken : interestedBukkens) {
					result.add(interestedBukken.getBukken());
				}
			}
		}
		Set<Bukken> uniqueBukkens = new HashSet<Bukken>(result);
		List<Bukken> finalResult = new ArrayList<Bukken>(uniqueBukkens);
		return finalResult;
	}

	// get bukken recommendation
	public List<Bukken> getBukkensRecommendation(List<Bukken> bukkens) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Account> thisAccount = accRepo.findByEmail(authentication.getName());
		if (thisAccount.isPresent() == false) {
			throw new NullException();
		}
		Account account = thisAccount.get();
		List<Bukken> myInterestedBukken = new ArrayList<Bukken>();
		if (account.getInterestedBukkens() != null && account.getInterestedBukkens().size() > 0) {
			List<InterestedBukken> interestedBukkens = account.getInterestedBukkens().stream()
					.collect(Collectors.toList());
			for (InterestedBukken interestedBukken : interestedBukkens) {
				myInterestedBukken.add(interestedBukken.getBukken());
			}
		}
		bukkens.removeAll(myInterestedBukken);
		if (bukkens.size() <= 5) {
			return bukkens;
		} else {
			return bukkens.stream().limit(5).collect(Collectors.toList());
		}

	}

	public Map<Integer, Integer> sortMap(Map<Integer, Integer> myMatrix) {
		List<Map.Entry<Integer, Integer>> list = new ArrayList<>(myMatrix.entrySet());
		list.sort(Map.Entry.comparingByKey());
		LinkedHashMap<Integer, Integer> sortedMap = new LinkedHashMap<>();
		list.forEach(e -> sortedMap.put(e.getKey(), e.getValue()));
		return sortedMap;
	}
}
