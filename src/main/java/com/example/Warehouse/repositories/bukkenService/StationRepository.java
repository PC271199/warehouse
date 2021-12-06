package com.example.Warehouse.repositories.bukkenService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.bukkenService.Station;


@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {
    
}
