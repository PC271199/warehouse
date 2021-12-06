package com.example.Warehouse.mapper;

import java.util.List;

import com.example.Warehouse.dtos.accountService.AccountDto;
import com.example.Warehouse.entities.accountService.Account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel  = "spring", uses = {UserInforMapper.class})
public interface AccountMapper {
	
    @Mapping(source = "accountDto.userInforDto", target = "userinfor")
    Account toAccountEntity(AccountDto accountDto);
    @Mapping(source="account.id",target = "id")
    @Mapping(source = "account.userinfor",target = "userInforDto")
    AccountDto toAccountDTO(Account account);
    
    List<AccountDto> toAccountDTOs(List<Account> accounts);
}
