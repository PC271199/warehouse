package com.example.Warehouse.repositories.accountService;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.Warehouse.entities.accountService.Account;
import com.example.Warehouse.entities.accountService.Role;
import com.example.Warehouse.entities.accountService.UserInfor;


@Transactional
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findById(Integer id);
}
