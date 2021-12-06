package com.example.Warehouse.repositories.systemService;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.fileService.File;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<File, String> {
	@Query(value = "Select * from file where bukken_id = :bukkenId", nativeQuery = true)
	List<File> findByBukkenId(@Param("bukkenId") int bukkenId);
	Optional<File> findById(String id);
}
