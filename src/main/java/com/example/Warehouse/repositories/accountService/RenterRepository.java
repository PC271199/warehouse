package com.example.Warehouse.repositories.accountService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.accountService.Renter;

import java.util.List;
import java.util.Optional;

@Repository
public interface RenterRepository extends JpaRepository<Renter, Integer> {
	@Query(value = "Select * from renter where account_id = :accountId and bukken_id = :bukkenId", nativeQuery = true)
	Renter findbyAccountId_BukkenId(@Param("accountId") int accountId,@Param("bukkenId") int bukkenId);
	
	@Query(value = "Select * from renter where bukken_id = :bukkenId", nativeQuery = true)
	List<Renter> findby_BukkenId(@Param("bukkenId") int bukkenId);
}
