package com.example.service.impl;

import com.example.dto.request.TransactionCreateDTO;
import com.example.dto.response.TransactionDTO;
import com.example.entity.BankAccount;
import com.example.entity.Transaction;
import com.example.exception.business.InsufficientFundsException;
import com.example.exception.business.ResourceNotFoundException;
import com.example.mapper.TransactionMapper;
import com.example.repository.BankAccountRepository;
import com.example.repository.TransactionRepository;
import com.example.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  BankAccountRepository bankAccountRepository,
                                  TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public TransactionDTO deposit(Long accountId, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró la cuenta con ID " + accountId + " para realizar el depósito."));
        account.setBalance(account.getBalance().add(amount));
        bankAccountRepository.save(account);

        Transaction transaction = new Transaction(account, amount, "DEPOSIT");
        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public TransactionDTO withdraw(Long accountId, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró la cuenta con ID " + accountId + " para realizar el retiro."));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(amount);
        }

        account.setBalance(account.getBalance().subtract(amount));
        bankAccountRepository.save(account);

        Transaction transaction = new Transaction(account, amount, "WITHDRAW");
        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    @Override
    public TransactionDTO getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::toDTO)
                .orElseThrow(() ->
                        new ResourceNotFoundException("La transacción con ID " + id + " no existe."));
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByBankAccountId(accountId).stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    @Override
    public List<TransactionDTO> getTransactionsByType(String type) {
        return transactionRepository.findByType(type).stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    @Override
    public List<TransactionDTO> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTimestampBetween(start, end).stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    // Método agregado para pruebas unitarias y arquitectura clara
    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionCreateDTO dto) {
        BankAccount account = bankAccountRepository.findById(dto.getAccountId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("La cuenta con ID " + dto.getAccountId() + " no existe."));

        Transaction transaction = transactionMapper.toEntity(dto, account);
        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }
}
