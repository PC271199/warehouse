package com.example.Warehouse.repositories.accountService;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.accountService.VerificationToken;
@Repository
public interface VerificationRepository extends JpaRepository<VerificationToken, Integer> {
	Optional<VerificationToken> findByToken(String token);
//	Optional<VerificationToken> findbyAccount(Account account);
}
