package com.example.Warehouse.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.Warehouse.dtos.RegisterDto;
import com.example.Warehouse.entities.Account;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
	Account toAccountEntity(RegisterDto registerDto);
}
