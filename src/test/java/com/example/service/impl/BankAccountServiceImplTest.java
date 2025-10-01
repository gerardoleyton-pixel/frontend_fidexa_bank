package com.example.service.impl;

import com.example.dto.request.BankAccountCreateDTO;
import com.example.dto.response.BankAccountDTO;
import com.example.entity.BankAccount;
import com.example.entity.User;
import com.example.exception.business.ResourceNotFoundException;
import com.example.mapper.BankAccountMapper;
import com.example.repository.BankAccountRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    // Caso exitoso: obtener cuenta por ID
    @Test
    void shouldReturnAccountDTOWhenIdExists() {
        BankAccount account = new BankAccount();
        account.setId(1L);

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(bankAccountMapper.toDTO(account)).thenReturn(new BankAccountDTO());

        BankAccountDTO result = bankAccountService.getAccountById(1L);

        assertNotNull(result);
        verify(bankAccountRepository).findById(1L);
        verify(bankAccountMapper).toDTO(account);
    }

    // Caso fallido: cuenta no existe
    @Test
    void shouldThrowExceptionWhenAccountIdNotFound() {
        when(bankAccountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bankAccountService.getAccountById(99L));
        verify(bankAccountRepository).findById(99L);
    }

    //Caso exitoso: crear cuenta cuando el usuario existe
    @Test
    void shouldCreateAccountWhenUserExists() {
        BankAccountCreateDTO dto = new BankAccountCreateDTO();
        dto.setUserId(1L);

        User user = new User();
        BankAccount account = new BankAccount();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bankAccountMapper.toEntity(dto, user)).thenReturn(account);
        when(bankAccountRepository.save(account)).thenReturn(account);
        when(bankAccountMapper.toDTO(account)).thenReturn(new BankAccountDTO());

        BankAccountDTO result = bankAccountService.createAccount(dto);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(bankAccountMapper).toEntity(dto, user);
        verify(bankAccountRepository).save(account);
        verify(bankAccountMapper).toDTO(account);
    }

    // Caso fallido: usuario no existe
    @Test
    void shouldThrowExceptionWhenUserNotFoundForAccountCreation() {
        BankAccountCreateDTO dto = new BankAccountCreateDTO();
        dto.setUserId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bankAccountService.createAccount(dto));
        verify(userRepository).findById(99L);
    }
}
