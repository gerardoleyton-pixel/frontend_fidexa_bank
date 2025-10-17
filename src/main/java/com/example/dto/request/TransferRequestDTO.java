package com.example.dto.request;

import com.example.exception.message.ErrorMessages;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/*
 DTO para realizar transferencias entre cuentas bancarias.
 Requiere la cuenta origen, la cuenta destino y el monto.
 */
public class TransferRequestDTO {

    @NotNull(message = ErrorMessages.TRANSACTION_ACCOUNT_ID_REQUIRED)
    private Long fromAccountId;

    @NotNull(message = ErrorMessages.TRANSACTION_TO_ACCOUNT_ID_REQUIRED)
    private Long toAccountId;

    @NotNull(message = ErrorMessages.TRANSACTION_AMOUNT_REQUIRED)
    @DecimalMin(value = "0.01", inclusive = true, message = ErrorMessages.TRANSACTION_AMOUNT_MIN)
    private BigDecimal amount;

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
