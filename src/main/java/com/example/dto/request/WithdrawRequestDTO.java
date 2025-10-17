package com.example.dto.request;

import com.example.exception.message.ErrorMessages;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/*
 DTO para realizar retiros desde una cuenta bancaria.
 Solo requiere el ID de la cuenta y el monto a retirar.
 */
public class WithdrawRequestDTO {

    @NotNull(message = ErrorMessages.TRANSACTION_ACCOUNT_ID_REQUIRED)
    private Long accountId;

    @NotNull(message = ErrorMessages.TRANSACTION_AMOUNT_REQUIRED)
    @DecimalMin(value = "0.01", inclusive = true, message = ErrorMessages.TRANSACTION_AMOUNT_MIN)
    private BigDecimal amount;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
