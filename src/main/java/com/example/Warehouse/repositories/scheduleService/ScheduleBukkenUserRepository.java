package com.example.Warehouse.repositories.scheduleService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.bukkenService.Bukken;
import com.example.Warehouse.entities.scheduleService.ScheduleBukkenUser;

@Repository
public interface ScheduleBukkenUserRepository extends JpaRepository<ScheduleBukkenUser, Integer> {
	@Query(value = "Select * from schedulebukkenuser where status_id > 0 ORDER BY create_at DESC", nativeQuery = true)
	Page<ScheduleBukkenUser> findAllAtAdminPage(Pageable pageable);

	@Query(value = "Select * from schedulebukkenuser where account_id = :account_id AND schedulebukken_id = :schedulebukken_id ORDER BY create_at DESC", nativeQuery = true)
	Page<ScheduleBukkenUser> findByAccountIdBukkenId(Pageable pageable, @Param("account_id") int account_id,
			@Param("schedulebukken_id") int schedulebukken_id);

	@Query(value = "Select * from schedulebukkenuser where account_id = :account_id ORDER BY create_at DESC", nativeQuery = true)
	Page<ScheduleBukkenUser> findByAccountId(Pageable pageable, @Param("account_id") int account_id);
	
	@Query(value = "Select COUNT(id) from schedulebukkenuser where account_id = :account_id", nativeQuery = true)
	Long countByAccountId(@Param("account_id") int account_id);
}
