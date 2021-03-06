package com.example.Warehouse.repositories.accountService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.accountService.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	@Query(value = "Select * from account where code_id = :codeId", nativeQuery = true)
	List<Account> findByCodeId(@Param("codeId") String codeId);

	@Query(value = "Select * from account where role_id = :roleId", nativeQuery = true)
	List<Account> findByRoleId(@Param("roleId") int roleId);
	
	@Query(value = "Select * from account where role_id = 1 and id != :accountId", nativeQuery = true)
	List<Account> findRoleUser_NotMine(@Param("accountId") int accountId);

	Optional<Account> findByEmail(String email);

	Boolean existsByEmail(String email);

	Page<Account> findAll(Pageable pageable);
}
