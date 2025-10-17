package com.example.service.impl;

import com.example.dto.dto.internal.TransactionCreateDTO;
import com.example.dto.response.TransactionDTO;
import com.example.entity.BankAccount;
import com.example.entity.Transaction;
import com.example.exception.business.InsufficientFundsException;
import com.example.exception.business.ResourceNotFoundException;
import com.example.mapper.TransactionMapper;
import com.example.repository.BankAccountRepository;
import com.example.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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

    @Test
    void shouldThrowExceptionWhenTransactionIdNotFound() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransactionById(99L));
        verify(transactionRepository).findById(99L);
    }

    @Test
    void shouldCreateTransactionWhenAccountExists() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(1L);
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setType("DEPOSIT");

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

    @Test
    void shouldThrowExceptionWhenAccountNotFoundForTransactionCreation() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(99L);
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setType("DEPOSIT");

        when(bankAccountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(dto));
        verify(bankAccountRepository).findById(99L);
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegativeInCreateTransaction() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(1L);
        dto.setAmount(BigDecimal.valueOf(-100));
        dto.setType("DEPOSIT");

        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    void shouldThrowExceptionWhenTransactionTypeIsInvalid() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(1L);
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setType("INVALID_TYPE");

        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(dto));
    }

    @Test
    void shouldDepositSuccessfullyWhenAccountExists() {
        BankAccount account = new BankAccount();
        account.setBalance(BigDecimal.valueOf(1000));

        Transaction transaction = new Transaction();
        BigDecimal amount = BigDecimal.valueOf(500);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(new TransactionDTO());

        TransactionDTO result = transactionService.deposit(1L, amount);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(1500), account.getBalance());
        verify(bankAccountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
        verify(transactionMapper).toDTO(transaction);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundForDeposit() {
        when(bankAccountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.deposit(99L, BigDecimal.valueOf(500)));
        verify(bankAccountRepository).findById(99L);
    }

    @Test
    void shouldWithdrawSuccessfullyWhenFundsAreSufficient() {
        BankAccount account = new BankAccount();
        account.setBalance(BigDecimal.valueOf(1000));

        Transaction transaction = new Transaction();
        BigDecimal amount = BigDecimal.valueOf(400);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(new TransactionDTO());

        TransactionDTO result = transactionService.withdraw(1L, amount);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(600), account.getBalance());
        verify(bankAccountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
        verify(transactionMapper).toDTO(transaction);
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        BankAccount account = new BankAccount();
        account.setBalance(BigDecimal.valueOf(100));

        BigDecimal amount = BigDecimal.valueOf(500);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> transactionService.withdraw(1L, amount));
        verify(bankAccountRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundForWithdraw() {
        when(bankAccountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.withdraw(99L, BigDecimal.valueOf(100)));
        verify(bankAccountRepository).findById(99L);
    }

    @Test
    void shouldTransferSuccessfullyBetweenAccounts() {
        BankAccount from = new BankAccount();
        from.setId(1L);
        from.setBalance(BigDecimal.valueOf(1000));

        BankAccount to = new BankAccount();
        to.setId(2L);
        to.setBalance(BigDecimal.valueOf(500));

        Transaction transaction = new Transaction();
        transaction.setId(3L);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(from));
        when(bankAccountRepository.findById(2L)).thenReturn(Optional.of(to));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(new TransactionDTO());

        TransactionDTO result = transactionService.transfer(1L, 2L, BigDecimal.valueOf(200));

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(800), from.getBalance());
        assertEquals(BigDecimal.valueOf(700), to.getBalance());
        verify(bankAccountRepository).save(from);
        verify(bankAccountRepository).save(to);
        verify(transactionRepository).save(any(Transaction.class));
        verify(transactionMapper).toDTO(transaction);
    }

    @Test
    void shouldThrowExceptionWhenDestinationAccountNotFound() {
        BankAccount from = new BankAccount();
        from.setId(1L);
        from.setBalance(BigDecimal.valueOf(1000));

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(from));
        when(bankAccountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.transfer(1L, 99L, BigDecimal.valueOf(200)));
        verify(bankAccountRepository).findById(1L);
        verify(bankAccountRepository).findById(99L);
    }
}
