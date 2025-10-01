package com.example.service.impl;

import com.example.dto.request.BankAccountCreateDTO;
import com.example.dto.response.BankAccountDTO;
import com.example.entity.BankAccount;
import com.example.entity.User;
import com.example.exception.business.ResourceNotFoundException;
import com.example.mapper.BankAccountMapper;
import com.example.repository.BankAccountRepository;
import com.example.repository.UserRepository;
import com.example.service.BankAccountService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró el usuario con ID " + dto.getUserId() + " para crear la cuenta."));
        BankAccount account = bankAccountMapper.toEntity(dto, user);
        return bankAccountMapper.toDTO(bankAccountRepository.save(account));
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
                .orElseThrow(() ->
                        new ResourceNotFoundException("La cuenta con ID " + id + " no existe."));
    }

    @Override
    public void deleteAccount(Long id) {
        if (!bankAccountRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. La cuenta con ID " + id + " no existe.");
        }
        bankAccountRepository.deleteById(id);
    }

    @Override
    public List<BankAccountDTO> getAccountsByUserId(Long userId) {
        return bankAccountRepository.findByUserId(userId).stream()
                .map(bankAccountMapper::toDTO)
                .toList();
    }
}
