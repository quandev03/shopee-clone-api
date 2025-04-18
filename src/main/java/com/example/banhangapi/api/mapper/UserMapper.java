package com.example.banhangapi.api.mapper;

import com.example.banhangapi.api.dto.UserAdminDTO;
import com.example.banhangapi.api.entity.User;
import com.example.banhangapi.api.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto toDto(User user);
    User toEntity(UserDto userDto);

    UserAdminDTO toUserAdminDTO(User user);
}

