package com.example.Warehouse.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Warehouse.dtos.AccountDtoAdmin;
import com.example.Warehouse.entities.Account;
import com.example.Warehouse.entities.AuthProvider;
import com.example.Warehouse.entities.UserInfor;
import com.example.Warehouse.exceptions.AccountIsExistsException;
import com.example.Warehouse.exceptions.AccountNotFoundException;
import com.example.Warehouse.exceptions.EmailIsExistsException;
import com.example.Warehouse.exceptions.EmptyException;
import com.example.Warehouse.exceptions.ImportFailException;
import com.example.Warehouse.mapper.AccountAdminMapper;
import com.example.Warehouse.repositories.AccountRepository;
import com.example.Warehouse.repositories.UserRepository;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AccountService {
	@Autowired
	private AccountRepository accRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AccountAdminMapper accAdminMap;
	@Autowired
	private BCryptPasswordEncoder encoder;
	public Account changeAccount(Account oldaccount, Account newaccount) {
		oldaccount.getUserinfor().setName(newaccount.getUserinfor().getName());
		oldaccount.getUserinfor().setAddress(newaccount.getUserinfor().getAddress());
		oldaccount.getUserinfor().setYearOfBirth(newaccount.getUserinfor().getYearOfBirth());
		return oldaccount;
	}
	
	public List<Account> getAll() {
		List<Account> result= accRepo.findAll();
		if (result.size()>0) {
			return result;
		}
		else {
			throw new EmptyException();
		}
	}
	public Page<AccountDtoAdmin> getAllByPage(int pageIndex){
		Page<Account> result=accRepo.findAll(PageRequest.of(pageIndex, 5));
		Page<AccountDtoAdmin> resultDto=result.map(account->{
			AccountDtoAdmin accountDtoAdmin=accAdminMap.toAccountDtoAdmin(account);
			return accountDtoAdmin;
		});
		if(result.getSize()>0) {
			return resultDto;
		}
		else {
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
	public Account getByUserName(String username) {
		Optional<Account> account=this.accRepo.findByEmail(username);
		if(account.isPresent()) {
			return account.get();
		}
		else {
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
		accRepo.save(account);
		return account;
	}
	public Account updateAccount(Account account) {
		Optional<Account> oldaccount = this.accRepo.findById(account.getId());
		if (!oldaccount.isPresent()) {
			throw new AccountNotFoundException(account.getId());
		}
		Optional<Account> existAccount = this.accRepo.findByEmail(account.getEmail());
		if (existAccount.isPresent() && existAccount.get().getId() != account.getId()) {
			throw new AccountNotFoundException(account.getId());
		}
		Account thisaccount = oldaccount.get();
		Account result= this.changeAccount(thisaccount, account);
		accRepo.save(result);
		return result;
	}
	public void deleteAccount(int accountId) {
		Optional<Account> oldaccount = this.accRepo.findById(accountId);
		if (!oldaccount.isPresent()) {
			throw new AccountNotFoundException(accountId);
		}
		accRepo.deleteById(accountId);
	}
//	public void changePassWord(PasswordDto passwordDto) {
//		Optional<Account> oldaccount = this.accRepo.findById(passwordDto.getAccountId());
//    	if (!oldaccount.isPresent()) {
//    		throw new AccountNotFoundException(passwordDto.getAccountId());
//    	}
//    	Account accountEntityDB=oldaccount.get();
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        if(!bCryptPasswordEncoder.matches(passwordDto.getOldPassword(), accountEntityDB.getPassword())) {
//            throw new PasswordIsNotMatchException();
//        }
//        accountEntityDB.setPassword(bCryptPasswordEncoder.encode(passwordDto.getNewPassword()));
//        this.accRepo.save(accountEntityDB);
//    }
	public void importAccount(List<Account> accounts) {
		for (Account account : accounts) {
			if(this.accRepo.findByEmail(account.getEmail()).isPresent()) {
				throw new AccountIsExistsException();
			}
			account.getUserinfor().setAccount(account);
			account.setPassword(encoder.encode(account.getPassword()));
		}
		List<Account> result=null;
		result=accRepo.saveAll(accounts);
		if(result==null) {
			throw new ImportFailException();
		}
	}

}
