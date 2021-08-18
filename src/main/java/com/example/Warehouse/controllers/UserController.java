package com.example.Warehouse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Warehouse.entities.Account;
import com.example.Warehouse.exceptions.ResourceNotFoundException;
import com.example.Warehouse.repositories.AccountRepository;
import com.example.Warehouse.security.CurrentUser;
import com.example.Warehouse.security.UserPrincipal;

@RestController
public class UserController {

    @Autowired
    private AccountRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public Account getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
