package com.example.dto.request;

import com.example.exception.message.ErrorMessages;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/*
 DTO para actualizar una cuenta bancaria existente.
 Permite modificar el titular y el saldo.
 */
public class BankAccountUpdateDTO {

    @NotBlank(message = ErrorMessages.ACCOUNT_HOLDER_REQUIRED)
    private String accountHolder;

    @NotNull(message = ErrorMessages.INITIAL_BALANCE_REQUIRED)
    @DecimalMin(value = "0.0", inclusive = false, message = ErrorMessages.INITIAL_BALANCE_MIN)
    private BigDecimal balance;

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
