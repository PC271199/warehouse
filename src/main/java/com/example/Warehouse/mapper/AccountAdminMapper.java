package com.example.Warehouse.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.example.Warehouse.dtos.AccountDtoAdmin;
import com.example.Warehouse.entities.Account;

@Mapper(componentModel = "spring", uses = {UserInforMapper.class})
public interface AccountAdminMapper {
	@Mapping(source="accountDtoAdmin.userinforDto",target = "userinfor")
	Account toAccountEntity(AccountDtoAdmin accountDtoAdmin);
	
	@Mapping(source="account.userinfor",target = "userinforDto")
	AccountDtoAdmin toAccountDtoAdmin (Account account);
	
	List<AccountDtoAdmin> toAccountDtoAdmins(List<Account> accounts);
	
	List<Account> toAccountEntities(List<AccountDtoAdmin> accounts);
	
}
