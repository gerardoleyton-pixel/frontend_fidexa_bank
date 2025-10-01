package com.example.mapper;

import com.example.dto.request.TransactionCreateDTO;
import com.example.dto.response.TransactionDTO;
import com.example.entity.Transaction;
import com.example.entity.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "bankAccount", source = "account")
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    Transaction toEntity(TransactionCreateDTO dto, BankAccount account);

    @Mapping(target = "accountId", source = "bankAccount.id")
    TransactionDTO toDTO(Transaction transaction);
}
