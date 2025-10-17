package com.example.service;

import com.example.dto.dto.internal.TransactionCreateDTO;
import com.example.dto.response.TransactionDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    //Realizar un depósito en una cuenta
    TransactionDTO deposit(Long accountId, BigDecimal amount);

    //Realizar un retiro desde una cuenta
    TransactionDTO withdraw(Long accountId, BigDecimal amount);

    //Realizar una transferencia entre dos cuentas
    TransactionDTO transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);

    //Obtener todas las transacciones
    List<TransactionDTO> getAllTransactions();

    //Obtener una transacción por su ID
    TransactionDTO getTransactionById(Long id);

    //Obtener transacciones por ID de cuenta
    List<TransactionDTO> getTransactionsByAccountId(Long accountId);

    //Obtener transacciones por tipo (DEPOSIT, WITHDRAW, TRANSFER)
    List<TransactionDTO> getTransactionsByType(String type);

    //Obtener transacciones por rango de fechas
    List<TransactionDTO> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end);

    //Crear una transacción manualmente (no se usa en los controladores actuales)
    TransactionDTO createTransaction(TransactionCreateDTO dto);
}
