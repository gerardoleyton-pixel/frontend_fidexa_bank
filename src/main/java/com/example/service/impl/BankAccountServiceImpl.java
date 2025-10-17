package com.example.service.impl;

import com.example.dto.request.BankAccountCreateDTO;
import com.example.dto.request.BankAccountUpdateDTO;
import com.example.dto.response.BankAccountDTO;
import com.example.entity.BankAccount;
import com.example.entity.User;
import com.example.exception.business.DuplicateEntityException;
import com.example.exception.business.ResourceNotFoundException;
import com.example.exception.message.ErrorMessages;
import com.example.mapper.BankAccountMapper;
import com.example.repository.BankAccountRepository;
import com.example.repository.UserRepository;
import com.example.service.BankAccountService;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 Implementación del servicio de cuentas bancarias.
 Maneja operaciones de creación, consulta, actualización y eliminación.
 Aplica validaciones de existencia y duplicación.
 */
@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final BankAccountMapper bankAccountMapper;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository,
                                  UserRepository userRepository,
                                  BankAccountMapper bankAccountMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public BankAccountDTO createAccount(BankAccountCreateDTO dto) {
        if (bankAccountRepository.findByAccountNumber(dto.getAccountNumber()).isPresent()) {
            throw new DuplicateEntityException("Cuenta", "número de cuenta", dto.getAccountNumber());
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getUserId()));

        BankAccount account = bankAccountMapper.toEntity(dto, user);
        BankAccount saved = bankAccountRepository.save(account);
        return bankAccountMapper.toDTO(saved);
    }

    @Override
    public List<BankAccountDTO> getAllAccounts() {
        return bankAccountRepository.findAll().stream()
                .map(bankAccountMapper::toDTO)
                .toList();
    }

    @Override
    public BankAccountDTO getAccountById(Long id) {
        return bankAccountRepository.findById(id)
                .map(bankAccountMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta", id));
    }

    @Override
    public void deleteAccount(Long id) {
        if (!bankAccountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cuenta", id);
        }
        bankAccountRepository.deleteById(id);
    }

    @Override
    public List<BankAccountDTO> getAccountsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario", userId);
        }

        return bankAccountRepository.findByUserId(userId).stream()
                .map(bankAccountMapper::toDTO)
                .toList();
    }

    @Override
    public BankAccountDTO updateAccount(Long id, BankAccountUpdateDTO dto) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ACCOUNT_NOT_FOUND, id));

        account.setAccountHolder(dto.getAccountHolder());
        account.setBalance(dto.getBalance());

        BankAccount updated = bankAccountRepository.save(account);
        return bankAccountMapper.toDTO(updated);
    }
}
