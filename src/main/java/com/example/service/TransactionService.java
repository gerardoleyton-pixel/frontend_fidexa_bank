package com.example.service;

import com.example.dto.request.TransactionCreateDTO;
import com.example.dto.response.TransactionDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    TransactionDTO deposit(Long accountId, BigDecimal amount);
    TransactionDTO withdraw(Long accountId, BigDecimal amount);
    List<TransactionDTO> getAllTransactions();
    TransactionDTO getTransactionById(Long id);
    List<TransactionDTO> getTransactionsByAccountId(Long accountId);
    List<TransactionDTO> getTransactionsByType(String type);
    List<TransactionDTO> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end);

    // Agregado para que el método en la implementación compile
    TransactionDTO createTransaction(TransactionCreateDTO dto);
}
