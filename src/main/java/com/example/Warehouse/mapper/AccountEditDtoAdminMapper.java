package com.example.Warehouse.mapper;

import org.mapstruct.Mapper;

import com.example.Warehouse.dtos.accountService.AccountEditDtoAdmin;
import com.example.Warehouse.dtos.accountService.RegisterDto;
import com.example.Warehouse.entities.accountService.Account;

@Mapper(componentModel = "spring")
public interface AccountEditDtoAdminMapper {
	Account toAccountEntity(AccountEditDtoAdmin accountEditDtoAdmin);
	
}
