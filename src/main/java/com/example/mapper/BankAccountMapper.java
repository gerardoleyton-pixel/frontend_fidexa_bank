package com.example.mapper;

import com.example.dto.request.BankAccountCreateDTO;
import com.example.dto.response.BankAccountDTO;
import com.example.entity.BankAccount;
import com.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    @Mapping(target = "user", source = "user")
    BankAccount toEntity(BankAccountCreateDTO dto, User user);

    @Mapping(source = "user.fullName", target = "accountHolder")
    BankAccountDTO toDTO(BankAccount account);
}
