package com.example.Warehouse.repositories.scheduleService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.scheduleService.EventSchedule;

@Repository
public interface EventScheduleRepository extends JpaRepository<EventSchedule, Integer> {
}
