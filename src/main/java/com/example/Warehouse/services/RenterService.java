package com.example.Warehouse.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.accountService.Renter;
import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.repositories.accountService.RenterRepository;
import com.example.Warehouse.repositories.bukkenService.BukkenRepository;

@Service
public class RenterService {
	@Autowired
	private RenterRepository renterRepo;

	@Autowired
	private BukkenService bukkenser;

	@Autowired
	private AccountService accser;
	
	public void save(Renter renter) {
		renterRepo.save(renter);
	}

	public Renter findbyAccountId_BukkenId(int accountId, int bukkenId) {
		return renterRepo.findbyAccountId_BukkenId(accountId, bukkenId);
	}

	public List<Renter> getRenterByBukken(int bukkenId) {
		List<Renter> result = new ArrayList<Renter>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if ("ROLE_ADMIN".equals(authentication.getAuthorities().toArray()[0].toString())) {
			result = renterRepo.findby_BukkenId(bukkenId);
			return result;
		} else if ("ROLE_OWNER".equals(authentication.getAuthorities().toArray()[0].toString())) {
			Bukken thisBukken = bukkenser.getById(bukkenId);
			Account owner = accser.getByUserName(authentication.getName());
			if (thisBukken.getAccount() == owner) {
				result = renterRepo.findby_BukkenId(bukkenId);
			}
			return result;
		}
		else return result;
	}
}
