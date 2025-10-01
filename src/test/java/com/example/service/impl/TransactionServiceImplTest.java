package com.example.service.impl;

import com.example.dto.request.TransactionCreateDTO;
import com.example.dto.response.TransactionDTO;
import com.example.entity.BankAccount;
import com.example.entity.Transaction;
import com.example.exception.business.ResourceNotFoundException;
import com.example.mapper.TransactionMapper;
import com.example.repository.BankAccountRepository;
import com.example.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    // Caso exitoso: obtener transacción por ID
    @Test
    void shouldReturnTransactionDTOWhenIdExists() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDTO(transaction)).thenReturn(new TransactionDTO());

        TransactionDTO result = transactionService.getTransactionById(1L);

        assertNotNull(result);
        verify(transactionRepository).findById(1L);
        verify(transactionMapper).toDTO(transaction);
    }

    // Caso fallido: transacción no existe
    @Test
    void shouldThrowExceptionWhenTransactionIdNotFound() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransactionById(99L));
        verify(transactionRepository).findById(99L);
    }

    // Caso exitoso: crear transacción cuando la cuenta existe
    @Test
    void shouldCreateTransactionWhenAccountExists() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(1L);

        BankAccount account = new BankAccount();
        Transaction transaction = new Transaction();

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionMapper.toEntity(dto, account)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(new TransactionDTO());

        TransactionDTO result = transactionService.createTransaction(dto);

        assertNotNull(result);
        verify(bankAccountRepository).findById(1L);
        verify(transactionMapper).toEntity(dto, account);
        verify(transactionRepository).save(transaction);
        verify(transactionMapper).toDTO(transaction);
    }

    // Caso fallido: cuenta no existe
    @Test
    void shouldThrowExceptionWhenAccountNotFoundForTransactionCreation() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(99L);

        when(bankAccountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(dto));
        verify(bankAccountRepository).findById(99L);
    }
}
