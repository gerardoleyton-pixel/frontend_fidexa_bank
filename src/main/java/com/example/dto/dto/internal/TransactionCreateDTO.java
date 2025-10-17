/*Archivo auxiliar para pruebas, ya no se usa en ningún controlador solo en el
 método create transaction internamente del servicio y en las pruebas
    TransactionServiceImplTest
 */

package com.example.dto.dto.internal;

import com.example.exception.message.ErrorMessages;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public class TransactionCreateDTO {

    @NotBlank(message = ErrorMessages.TRANSACTION_TYPE_REQUIRED)
    private String type;

    @NotNull(message = ErrorMessages.TRANSACTION_AMOUNT_REQUIRED)
    @DecimalMin(value = "0.01", inclusive = true, message = ErrorMessages.TRANSACTION_AMOUNT_MIN)
    private BigDecimal amount;

    @NotNull(message = ErrorMessages.TRANSACTION_ACCOUNT_ID_REQUIRED)
    private Long accountId;

    private Long toAccountId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type != null ? type.trim().toUpperCase() : null;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    /**
     * Validación manual para transferencias.
     * Se puede invocar desde el controlador si se desea validar antes de llamar al servicio.
     */
    public boolean isValidTransfer() {
        return "TRANSFER".equalsIgnoreCase(type) && toAccountId != null;
    }
}
