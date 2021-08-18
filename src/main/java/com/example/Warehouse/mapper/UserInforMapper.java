package com.example.Warehouse.mapper;


import org.mapstruct.Mapper;

import com.example.Warehouse.dtos.UserInforDto;
import com.example.Warehouse.entities.UserInfor;



@Mapper(componentModel = "spring")

public interface UserInforMapper {


	UserInfor toUserEntity(UserInforDto userDto);
   
	UserInforDto toUserDTO(UserInfor userInfor);

}
