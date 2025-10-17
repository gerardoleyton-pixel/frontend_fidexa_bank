package com.example.controller;

import com.example.dto.request.BankAccountCreateDTO;
import com.example.dto.response.BankAccountDTO;
import com.example.exception.business.DuplicateEntityException;
import com.example.exception.business.ResourceNotFoundException;
import com.example.service.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankAccountController.class)
@ExtendWith(SpringExtension.class)
class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateAccountSuccessfully() throws Exception {
        BankAccountCreateDTO request = new BankAccountCreateDTO();
        request.setUserId(1L);
        request.setAccountNumber("ACC123456");
        request.setAccountHolder("John Doe");
        request.setInitialBalance(new BigDecimal("1000.00"));

        BankAccountDTO response = new BankAccountDTO();
        response.setId(10L);
        response.setAccountNumber("ACC123456");
        response.setAccountHolder("John Doe");
        response.setBalance(new BigDecimal("1000.00"));

        Mockito.when(bankAccountService.createAccount(any(BankAccountCreateDTO.class))).thenReturn(response);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$.accountHolder").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    void shouldReturn400WhenCreatingAccountWithInvalidData() throws Exception {
        String payload = """
            {
                "userId": null,
                "accountNumber": "",
                "accountHolder": "",
                "initialBalance": -100
            }
            """;

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error de validación en los campos."))
                .andExpect(jsonPath("$.details.userId").value("El ID del usuario es obligatorio."))
                .andExpect(jsonPath("$.details.accountNumber").value("El número de cuenta es obligatorio."))
                .andExpect(jsonPath("$.details.accountHolder").value("El titular de la cuenta es obligatorio."))
                .andExpect(jsonPath("$.details.initialBalance").value("El saldo debe ser un valor positivo."));
    }

    @Test
    void shouldReturn409WhenCreatingAccountWithDuplicateNumber() throws Exception {
        BankAccountCreateDTO request = new BankAccountCreateDTO();
        request.setUserId(1L);
        request.setAccountNumber("ACC123456");
        request.setAccountHolder("John Doe");
        request.setInitialBalance(new BigDecimal("1000.00"));

        Mockito.when(bankAccountService.createAccount(any()))
                .thenThrow(new DuplicateEntityException("Cuenta", "número de cuenta", "ACC123456"));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Cuenta con número de cuenta 'ACC123456' ya existe."));
    }

    @Test
    void shouldReturnAccountById() throws Exception {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(1L);
        dto.setAccountNumber("ACC987654");
        dto.setAccountHolder("John Doe");
        dto.setBalance(new BigDecimal("500.00"));

        Mockito.when(bankAccountService.getAccountById(1L)).thenReturn(dto);

        mockMvc.perform(get("/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("ACC987654"))
                .andExpect(jsonPath("$.accountHolder").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(500.00));
    }

    @Test
    void shouldReturn404WhenAccountNotFound() throws Exception {
        Mockito.when(bankAccountService.getAccountById(99L))
                .thenThrow(new ResourceNotFoundException("Cuenta", 99L));

        mockMvc.perform(get("/accounts/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cuenta con ID 99 no existe."));
    }

    @Test
    void shouldDeleteAccountSuccessfully() throws Exception {
        Mockito.doNothing().when(bankAccountService).deleteAccount(1L);

        mockMvc.perform(delete("/accounts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenDeletingNonexistentAccount() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Cuenta", 99L)).when(bankAccountService).deleteAccount(99L);

        mockMvc.perform(delete("/accounts/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cuenta con ID 99 no existe."));
    }

    @Test
    void shouldReturnAccountsByUserId() throws Exception {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(1L);
        dto.setAccountNumber("ACC111222");
        dto.setAccountHolder("John Doe");
        dto.setBalance(new BigDecimal("750.00"));

        Mockito.when(bankAccountService.getAccountsByUserId(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/accounts/by-user")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].accountNumber").value("ACC111222"))
                .andExpect(jsonPath("$[0].accountHolder").value("John Doe"))
                .andExpect(jsonPath("$[0].balance").value(750.00));
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoAccounts() throws Exception {
        Mockito.when(bankAccountService.getAccountsByUserId(99L)).thenReturn(List.of());

        mockMvc.perform(get("/accounts/by-user")
                        .param("userId", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
