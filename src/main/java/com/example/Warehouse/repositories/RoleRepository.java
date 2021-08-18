package com.example.Warehouse.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.Warehouse.entities.Account;
import com.example.Warehouse.entities.Role;
import com.example.Warehouse.entities.UserInfor;


@Transactional
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findById(Integer id);
}
