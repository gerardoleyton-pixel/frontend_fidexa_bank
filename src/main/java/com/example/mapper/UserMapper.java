package com.example.mapper;

import com.example.dto.request.UserCreateDTO;
import com.example.dto.response.UserDTO;
import com.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 Mapper para convertir entre entidades User y sus DTOs.
 Utiliza MapStruct para generar el código automáticamente.
 */
@Mapper(componentModel = "spring", uses = BankAccountMapper.class)
public interface UserMapper {


    User toEntity(UserCreateDTO dto);

   @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "accounts", target = "accounts")
    })
    UserDTO toDTO(User user);


    List<UserDTO> toDTOList(List<User> users);
}
