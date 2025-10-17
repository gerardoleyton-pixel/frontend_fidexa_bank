package com.example.controller;

import com.example.dto.request.DepositRequestDTO;
import com.example.dto.request.TransferRequestDTO;
import com.example.dto.request.WithdrawRequestDTO;
import com.example.dto.response.TransactionDTO;
import com.example.exception.business.ResourceNotFoundException;
import com.example.service.TransactionService;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@ExtendWith(SpringExtension.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnAllTransactions() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(1L);
        dto.setType("DEPOSIT");
        dto.setAmount(BigDecimal.valueOf(500));
        dto.setAccountId(1L);

        Mockito.when(transactionService.getAllTransactions()).thenReturn(List.of(dto));

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].type").value("DEPOSIT"))
                .andExpect(jsonPath("$[0].amount").value(500))
                .andExpect(jsonPath("$[0].accountId").value(1));
    }

    @Test
    void shouldReturnTransactionById() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(1L);
        dto.setType("WITHDRAW");
        dto.setAmount(BigDecimal.valueOf(300));
        dto.setAccountId(1L);

        Mockito.when(transactionService.getTransactionById(1L)).thenReturn(dto);

        mockMvc.perform(get("/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("WITHDRAW"))
                .andExpect(jsonPath("$.amount").value(300))
                .andExpect(jsonPath("$.accountId").value(1));
    }

    @Test
    void shouldReturn404WhenTransactionNotFound() throws Exception {
        Mockito.when(transactionService.getTransactionById(99L))
                .thenThrow(new ResourceNotFoundException("Transacción", 99L));

        mockMvc.perform(get("/transactions/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Transacción con ID 99 no existe."));
    }

    @Test
    void shouldDepositSuccessfully() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(1L);
        dto.setType("DEPOSIT");
        dto.setAmount(BigDecimal.valueOf(500));
        dto.setAccountId(1L);

        Mockito.when(transactionService.deposit(1L, BigDecimal.valueOf(500))).thenReturn(dto);

        DepositRequestDTO request = new DepositRequestDTO();
        request.setAmount(BigDecimal.valueOf(500));
        request.setAccountId(1L);

        mockMvc.perform(post("/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("DEPOSIT"))
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.accountId").value(1));
    }

    @Test
    void shouldReturn404WhenDepositingToNonexistentAccount() throws Exception {
        Mockito.when(transactionService.deposit(99L, BigDecimal.valueOf(500)))
                .thenThrow(new ResourceNotFoundException("Cuenta", 99L));

        DepositRequestDTO request = new DepositRequestDTO();
        request.setAmount(BigDecimal.valueOf(500));
        request.setAccountId(99L);

        mockMvc.perform(post("/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cuenta con ID 99 no existe."));
    }

    @Test
    void shouldWithdrawSuccessfully() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(2L);
        dto.setType("WITHDRAW");
        dto.setAmount(BigDecimal.valueOf(300));
        dto.setAccountId(1L);

        Mockito.when(transactionService.withdraw(1L, BigDecimal.valueOf(300))).thenReturn(dto);

        WithdrawRequestDTO request = new WithdrawRequestDTO();
        request.setAmount(BigDecimal.valueOf(300));
        request.setAccountId(1L);

        mockMvc.perform(post("/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.type").value("WITHDRAW"))
                .andExpect(jsonPath("$.amount").value(300))
                .andExpect(jsonPath("$.accountId").value(1));
    }
    @Test
    void shouldReturn404WhenWithdrawingFromNonexistentAccount() throws Exception {
        Mockito.when(transactionService.withdraw(99L, BigDecimal.valueOf(300)))
                .thenThrow(new ResourceNotFoundException("Cuenta", 99L));

        WithdrawRequestDTO request = new WithdrawRequestDTO();
        request.setAmount(BigDecimal.valueOf(300));
        request.setAccountId(99L);

        mockMvc.perform(post("/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cuenta con ID 99 no existe."));
    }

    @Test
    void shouldReturnTransactionsByAccountId() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(1L);
        dto.setAccountId(1L);

        Mockito.when(transactionService.getTransactionsByAccountId(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/transactions/by-account")
                        .param("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].accountId").value(1));
    }

    @Test
    void shouldReturnTransactionsByType() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(1L);
        dto.setType("DEPOSIT");

        Mockito.when(transactionService.getTransactionsByType("DEPOSIT")).thenReturn(List.of(dto));

        mockMvc.perform(get("/transactions/by-type")
                        .param("type", "DEPOSIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].type").value("DEPOSIT"));
    }

    @Test
    void shouldReturnTransactionsByDateRange() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(1L);
        dto.setTimestamp(LocalDateTime.of(2025, 1, 15, 10, 0));

        Mockito.when(transactionService.getTransactionsByDateRange(
                        LocalDateTime.of(2025, 1, 1, 0, 0),
                        LocalDateTime.of(2025, 12, 31, 23, 59, 59)))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/transactions/by-date-range")
                        .param("start", "2025-01-01")
                        .param("end", "2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldTransferSuccessfully() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(3L);
        dto.setType("TRANSFER");
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setAccountId(1L);
        dto.setDescription("Transferencia de cuenta 1 a 2");

        Mockito.when(transactionService.transfer(1L, 2L, BigDecimal.valueOf(100))).thenReturn(dto);

        TransferRequestDTO request = new TransferRequestDTO();
        request.setAmount(BigDecimal.valueOf(100));
        request.setFromAccountId(1L);
        request.setToAccountId(2L);

        mockMvc.perform(post("/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.type").value("TRANSFER"))
                .andExpect(jsonPath("$.amount").value(100))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.description").value("Transferencia de cuenta 1 a 2"));
    }

    @Test
    void shouldReturn404WhenTransferFailsDueToMissingAccount() throws Exception {
        Mockito.when(transactionService.transfer(1L, 99L, BigDecimal.valueOf(100)))
                .thenThrow(new ResourceNotFoundException("Cuenta destino", 99L));

        TransferRequestDTO request = new TransferRequestDTO();
        request.setAmount(BigDecimal.valueOf(100));
        request.setFromAccountId(1L);
        request.setToAccountId(99L);

        mockMvc.perform(post("/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cuenta destino con ID 99 no existe."));
    }

    @Test
    void shouldReturn400WhenWithdrawFailsDueToInsufficientFunds() throws Exception {
        Mockito.when(transactionService.withdraw(1L, BigDecimal.valueOf(9999)))
                .thenThrow(new com.example.exception.business.InsufficientFundsException(1L));

        WithdrawRequestDTO request = new WithdrawRequestDTO();
        request.setAmount(BigDecimal.valueOf(9999));
        request.setAccountId(1L);

        mockMvc.perform(post("/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("La cuenta con ID 1 no tiene fondos suficientes."));
    }

    @Test
    void shouldReturn400WhenTransferFailsDueToInsufficientFunds() throws Exception {
        Mockito.when(transactionService.transfer(1L, 2L, BigDecimal.valueOf(9999)))
                .thenThrow(new com.example.exception.business.InsufficientFundsException(1L));

        TransferRequestDTO request = new TransferRequestDTO();
        request.setAmount(BigDecimal.valueOf(9999));
        request.setFromAccountId(1L);
        request.setToAccountId(2L);

        mockMvc.perform(post("/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("La cuenta con ID 1 no tiene fondos suficientes."));
    }

    @Test
    void shouldReturn400WhenDepositAmountIsInvalid() throws Exception {
        DepositRequestDTO request = new DepositRequestDTO();
        request.setAmount(BigDecimal.valueOf(-100));
        request.setAccountId(1L);

        mockMvc.perform(post("/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.amount").value("El monto debe ser un valor positivo."));
    }

    @Test
    void shouldReturn400WhenWithdrawAmountIsInvalid() throws Exception {
        WithdrawRequestDTO request = new WithdrawRequestDTO();
        request.setAmount(BigDecimal.valueOf(-50));
        request.setAccountId(1L);

        mockMvc.perform(post("/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.amount").value("El monto debe ser un valor positivo."));
    }

    @Test
    void shouldReturn400WhenTransferAmountIsInvalid() throws Exception {
        TransferRequestDTO request = new TransferRequestDTO();
        request.setAmount(BigDecimal.valueOf(-200));
        request.setFromAccountId(1L);
        request.setToAccountId(2L);

        mockMvc.perform(post("/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.amount").value("El monto debe ser un valor positivo."));
    }
}
