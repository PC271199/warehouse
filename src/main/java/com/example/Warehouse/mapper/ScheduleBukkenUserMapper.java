package com.example.Warehouse.mapper;


import org.mapstruct.Mapper;

import com.example.Warehouse.dtos.scheduleService.ScheduleBukkenUserDto;
import com.example.Warehouse.entities.scheduleService.ScheduleBukkenUser;




@Mapper(componentModel = "spring")

public interface ScheduleBukkenUserMapper {


	ScheduleBukkenUser toScheduleBukkenUserEntity(ScheduleBukkenUserDto scheduleBukkenUserDto);
   
	ScheduleBukkenUserDto toScheduleBukkenUserDTO(ScheduleBukkenUser scheduleBukkenUser);

}
