package com.example.Warehouse.repositories.scheduleService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.fileService.File;
import com.example.Warehouse.entities.scheduleService.ScheduleBukken;

@Repository
public interface ScheduleBukkenRepository extends JpaRepository<ScheduleBukken, Integer> {
	
}
