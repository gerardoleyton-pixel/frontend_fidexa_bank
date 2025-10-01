package com.example.mapper;

import com.example.dto.request.UserCreateDTO;
import com.example.dto.response.UserDTO;
import com.example.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateDTO dto);
    UserDTO toDTO(User user);
}
