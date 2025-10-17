package com.example.dto.request;

import com.example.exception.message.ErrorMessages;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO para la creación de cuentas bancarias.
 * Aplica validaciones para asegurar que los datos sean correctos.
 *
 * Validaciones incluidas:
 * - Número de cuenta obligatorio
 * - Titular obligatorio
 * - Saldo inicial obligatorio y mayor a cero
 * - ID de usuario obligatorio
 */
public class BankAccountCreateDTO {

    /**
     * Número de cuenta asignado.
     * Debe ser único y no estar vacío.
     */
    @NotBlank(message = ErrorMessages.ACCOUNT_NUMBER_REQUIRED)
    private String accountNumber;

    /**
     * Nombre del titular de la cuenta.
     */
    @NotBlank(message = ErrorMessages.ACCOUNT_HOLDER_REQUIRED)
    private String accountHolder;

    /**
     * Saldo inicial de la cuenta.
     * Debe ser mayor a cero.
     */
    @NotNull(message = ErrorMessages.INITIAL_BALANCE_REQUIRED)
    @DecimalMin(value = "0.01", inclusive = true, message = ErrorMessages.INITIAL_BALANCE_MIN)
    private BigDecimal initialBalance;

    /**
     * ID del usuario asociado a la cuenta.
     */
    @NotNull(message = ErrorMessages.USER_ID_REQUIRED)
    private Long userId;

    // 🔹 Getters y setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber != null ? accountNumber.trim() : null;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder != null ? accountHolder.trim() : null;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
