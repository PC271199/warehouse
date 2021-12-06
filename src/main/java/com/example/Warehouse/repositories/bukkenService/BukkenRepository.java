package com.example.Warehouse.repositories.bukkenService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.bukkenService.Bukken;

@Repository
public interface BukkenRepository extends JpaRepository<Bukken, Integer> {
	Page<Bukken> findAll(Pageable pageable);

//    @Query(value = "Select * from bukken where account_id = :account_id", nativeQuery = true)
	@Query(value = "SELECT *\n" + "FROM bukken as b\n" + "INNER JOIN account_bukken as a_b\n"
			+ "    ON b.id = a_b.bukken_id\n" + "WHERE a_b.account_id = :account_id", nativeQuery = true)
	Page<Bukken> findByAccountId(Pageable pageable, @Param("account_id") int account_id);

	@Query(value = "SELECT *\n" + "FROM bukken as b\n" + "INNER JOIN account_bukken as a_b\n"
			+ "    ON b.id = a_b.bukken_id\n" + "WHERE a_b.account_id = :account_id", nativeQuery = true)
	List<Bukken> findByAccountId(@Param("account_id") int account_id);
}
