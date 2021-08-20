package com.example.Warehouse.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.Warehouse.entities.UserInfor;


@Transactional
@Repository
public interface UserRepository extends JpaRepository<UserInfor, Integer> {
	@Query(value = "Select * from users where account_id = :accountId",nativeQuery = true)
	public UserInfor findByAccountId(@Param("accountId") int accountId);
		
	@Modifying
	@Query(value = "Delete from users where account_id = :accountId",nativeQuery = true)
	public void deleteByAccountId(@Param("accountId") int accountId);
}
