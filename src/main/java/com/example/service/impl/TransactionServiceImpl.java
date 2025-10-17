package com.example.service.impl;

import com.example.dto.dto.internal.TransactionCreateDTO;
import com.example.dto.response.TransactionDTO;
import com.example.entity.BankAccount;
import com.example.entity.Transaction;
import com.example.exception.business.InsufficientFundsException;
import com.example.exception.business.ResourceNotFoundException;
import com.example.exception.message.ErrorMessages;
import com.example.mapper.TransactionMapper;
import com.example.repository.BankAccountRepository;
import com.example.repository.TransactionRepository;
import com.example.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/*
 Implementación del servicio de transacciones.
 Maneja depósitos, retiros, transferencias y consultas.
 Aplica validaciones de monto, existencia y fondos.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final List<String> VALID_TYPES = List.of("DEPOSIT", "WITHDRAW", "TRANSFER");

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
        validateAmount(amount);

        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta", accountId));

        account.setBalance(account.getBalance().add(amount));
        bankAccountRepository.save(account);

        Transaction transaction = new Transaction(account, amount, "DEPOSIT");
        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public TransactionDTO withdraw(Long accountId, BigDecimal amount) {
        validateAmount(amount);

        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta", accountId));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountId);
        }

        account.setBalance(account.getBalance().subtract(amount));
        bankAccountRepository.save(account);

        Transaction transaction = new Transaction(account, amount, "WITHDRAW");
        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public TransactionDTO transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        validateAmount(amount);

        BankAccount from = bankAccountRepository.findById(fromAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta origen", fromAccountId));

        BankAccount to = bankAccountRepository.findById(toAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta destino", toAccountId));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(fromAccountId);
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        bankAccountRepository.save(from);
        bankAccountRepository.save(to);

        Transaction transaction = new Transaction(from, amount, "TRANSFER");
        transaction.setDescription("Transferencia de cuenta " + fromAccountId + " a " + toAccountId);

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
                .orElseThrow(() -> new ResourceNotFoundException("Transacción", id));
    }

    @Override
    public List<TransactionDTO> getTransactionsByAccountId(Long accountId) {
        if (!bankAccountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("Cuenta", accountId);
        }

        return transactionRepository.findByBankAccountId(accountId).stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    @Override
    public List<TransactionDTO> getTransactionsByType(String type) {
        if (!VALID_TYPES.contains(type.toUpperCase())) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_TRANSACTION_TYPE);
        }

        return transactionRepository.findByType(type.toUpperCase()).stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    @Override
    public List<TransactionDTO> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("El rango de fechas es inválido. La fecha inicial debe ser anterior a la final.");
        }

        return transactionRepository.findByTimestampBetween(start, end).stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionCreateDTO dto) {
        validateAmount(dto.getAmount());

        if (!VALID_TYPES.contains(dto.getType().toUpperCase())) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_TRANSACTION_TYPE);
        }

        BankAccount account = bankAccountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta", dto.getAccountId()));

        Transaction transaction = transactionMapper.toEntity(dto, account);
        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_AMOUNT);
        }
    }
}
