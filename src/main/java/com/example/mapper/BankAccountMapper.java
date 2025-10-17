package com.example.mapper;

import com.example.dto.request.BankAccountCreateDTO;
import com.example.dto.response.BankAccountDTO;
import com.example.entity.BankAccount;
import com.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Mapper para convertir entre entidades BankAccount y sus DTOs.
 * Incluye datos del usuario para que el frontend pueda mostrar ID y nombre.
 */
@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    @Mappings({
        @Mapping(target = "user", source = "user"),
        @Mapping(target = "balance", source = "dto.initialBalance"),
        @Mapping(target = "accountHolder", source = "dto.accountHolder")
    })
    BankAccount toEntity(BankAccountCreateDTO dto, User user);

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "account.accountNumber", target = "accountNumber"),
        @Mapping(source = "account.accountHolder", target = "accountHolder"),
        @Mapping(source = "account.balance", target = "balance"),
        @Mapping(source = "account.user.id", target = "userId"),
        @Mapping(source = "account.user.fullName", target = "userName")
    })
    BankAccountDTO toDTO(BankAccount account);

    List<BankAccountDTO> toDTOList(List<BankAccount> accounts);
}
