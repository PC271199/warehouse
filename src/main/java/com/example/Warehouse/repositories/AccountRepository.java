package com.example.Warehouse.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Warehouse.entities.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByEmail(String email);

    Boolean existsByEmail(String email);


    Page<Account> findAll(Pageable pageable);
}
