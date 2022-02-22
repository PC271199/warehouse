package com.example.Warehouse.repositories.bukkenService;

import java.util.Date;
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

	@Query(value = "Select * from bukken where account_id = :account_id", nativeQuery = true)
//	@Query(value = "SELECT *\n" + "FROM bukken as b\n" + "INNER JOIN account_bukken as a_b\n"
//			+ "    ON b.id = a_b.bukken_id\n" + "WHERE a_b.account_id = :account_id", nativeQuery = true)
	Page<Bukken> findByAccountId(Pageable pageable, @Param("account_id") int account_id);

	@Query(value = "Select * from bukken where account_id = :account_id", nativeQuery = true)
	List<Bukken> findByAccountId(@Param("account_id") int account_id);

	@Query(value = "Select * from bukken where account_id = :account_id and UPPER(name) LIKE (CONCAT('%',UPPER(:bukkenName),'%'))", nativeQuery = true)
	Page<Bukken> findByAccountId_BukkenName(Pageable pageable, @Param("account_id") int account_id,
			@Param("bukkenName") String bukkenName);

	@Query(value = "Select * from bukken where account_id = :account_id and UPPER(name) LIKE (CONCAT('%',UPPER(:bukkenName),'%'))", nativeQuery = true)
	List<Bukken> findByAccountId_BukkenName(@Param("account_id") int account_id,
			@Param("bukkenName") String bukkenName);

	@Query(value = "SELECT * FROM bukken WHERE UPPER(name) LIKE (CONCAT('%',UPPER(:bukkenName),'%')) ", nativeQuery = true)
	Page<Bukken> findByLikeBukkenNameByPage(Pageable pageable, @Param("bukkenName") String bukkenName);

	@Query(value = "SELECT * FROM bukken WHERE UPPER(name) LIKE (CONCAT('%',UPPER(:bukkenName),'%')) ", nativeQuery = true)
	List<Bukken> findByLikeBukkenName(@Param("bukkenName") String bukkenName);

	@Query(value = "SELECT * FROM bukken WHERE status = 'NotCompleteLaunching' OR status = 'CompleteLaunching' ", nativeQuery = true)
	List<Bukken> findByOpening();

	@Query(value = "SELECT * FROM bukken WHERE status = 'CompleteNotLaunching' OR status = 'CompleteLaunching' ", nativeQuery = true)
	List<Bukken> findByComplete();
	
	@Query(value = "SELECT * FROM bukken WHERE status = 'CompleteNotLaunching' OR status = 'NotCompleteNotLaunching' ", nativeQuery = true)
	List<Bukken> findByNotOpening();
	
	@Query(value = "SELECT * FROM bukken WHERE status = 'NotCompleteLaunching' OR status = 'NotCompleteNotLaunching' ", nativeQuery = true)
	List<Bukken> findByNotComplete();
	
	@Query(value = "SELECT * FROM bukken WHERE configured = true ", nativeQuery = true)
	List<Bukken> findByConfigured();
	
	@Query(value = "SELECT * FROM bukken WHERE vr = true ", nativeQuery = true)
	List<Bukken> findByVR();

	@Query(value = "SELECT * FROM bukken WHERE number_of_floors >= :minFloor ", nativeQuery = true)
	List<Bukken> findByMinFloor(int minFloor);

	@Query(value = "SELECT * FROM bukken WHERE number_of_floors <= :maxFloor ", nativeQuery = true)
	List<Bukken> findByMaxFloor(int maxFloor);

	@Query(value = "SELECT * FROM bukken WHERE rent_fee >= :minFee ", nativeQuery = true)
	List<Bukken> findByMinFee(int minFee);

	@Query(value = "SELECT * FROM bukken WHERE rent_fee <= :maxFee ", nativeQuery = true)
	List<Bukken> findByMaxFee(int maxFee);

	@Query(value = "SELECT * FROM bukken WHERE area >= :minArea ", nativeQuery = true)
	List<Bukken> findByMinArea(int minArea);

	@Query(value = "SELECT * FROM bukken WHERE area <= :maxArea ", nativeQuery = true)
	List<Bukken> findByMaxArea(int maxArea);
	
	@Query(value = "SELECT * FROM bukken WHERE delivery_date >= :delivery_date ", nativeQuery = true)
	List<Bukken> findByDeliveryDate(Date delivery_date);

	@Query(value = "SELECT COUNT(id) FROM bukken WHERE UPPER(name) LIKE (CONCAT('%',UPPER(:bukkenName),'%')) ", nativeQuery = true)
	int countByLikeBukkenName(@Param("bukkenName") String bukkenName);
}
