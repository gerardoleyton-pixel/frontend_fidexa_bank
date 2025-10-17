package com.example.service;

import com.example.dto.request.BankAccountCreateDTO;
import com.example.dto.request.BankAccountUpdateDTO;
import com.example.dto.response.BankAccountDTO;

import java.util.List;

public interface BankAccountService {
    BankAccountDTO createAccount(BankAccountCreateDTO dto);
    List<BankAccountDTO> getAllAccounts();
    BankAccountDTO getAccountById(Long id);
    void deleteAccount(Long id);
    List<BankAccountDTO> getAccountsByUserId(Long userId);
    BankAccountDTO updateAccount(Long id, BankAccountUpdateDTO dto); // ✅ Nuevo método
}
