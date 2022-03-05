package com.example.Warehouse.repositories.scheduleService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.fileService.File;
import com.example.Warehouse.entities.scheduleService.ScheduleBukken;

@Repository
public interface ScheduleBukkenRepository extends JpaRepository<ScheduleBukken, Integer> {
	@Query(value = "Select bukken_id from schedulebukken where id = :id", nativeQuery = true)
	int getBukkenIdByScheduleBukkenId(@Param("id") int id);
}
