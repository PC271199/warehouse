package com.example.Warehouse.repositories.bukkenService;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.bukkenService.InterestedBukken;

@Repository
public interface InterestedBukkenRepository extends JpaRepository<InterestedBukken, Integer> {
	@Query(value = "SELECT * FROM interestedbukken WHERE account_id = :account_id", nativeQuery = true)
	List<InterestedBukken> findByAccountId(@Param("account_id") int account_id);

	@Query(value = "SELECT * FROM interestedbukken WHERE account_id = :account_id", nativeQuery = true)
	Page<InterestedBukken> findByAccountIdNew(Pageable pageable, @Param("account_id") int account_id);

	@Query(value = "SELECT * FROM interestedbukken WHERE bukken_id= :bukken_id AND account_id = :account_id", nativeQuery = true)
	Optional<InterestedBukken> findByAccountIdBukkenId(@Param("bukken_id") int bukken_id,
			@Param("account_id") int account_id);
}
