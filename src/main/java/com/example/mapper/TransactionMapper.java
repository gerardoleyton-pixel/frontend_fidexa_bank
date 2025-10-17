package com.example.mapper;

import com.example.dto.dto.internal.TransactionCreateDTO;
import com.example.dto.response.TransactionDTO;
import com.example.entity.Transaction;
import com.example.entity.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/*
 Mapper para convertir entre entidades Transaction y sus DTOs.
 Utiliza MapStruct para generar el código automáticamente.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {


    @Mapping(target = "bankAccount", source = "account")
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "description", expression = "java(dto.getType() + \" de $\" + dto.getAmount())")
    Transaction toEntity(TransactionCreateDTO dto, BankAccount account);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "accountId", source = "bankAccount.id")
    TransactionDTO toDTO(Transaction transaction);


    List<TransactionDTO> toDTOList(List<Transaction> transactions);
}
