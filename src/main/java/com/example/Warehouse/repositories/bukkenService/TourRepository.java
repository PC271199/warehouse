package com.example.Warehouse.repositories.bukkenService;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.bukkenService.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
	@Query(value = "SELECT * FROM tour WHERE bukken_id = :bukken_id", nativeQuery = true)
	List<Tour> findByBukkenId(@Param("bukken_id") int bukken_id);
}
