package com.example.exception.business;

/**
 * Excepción lanzada cuando una cuenta no tiene fondos suficientes
 * para realizar un retiro o transferencia.
 * Se utiliza en operaciones que requieren validación de saldo.
 */
public class InsufficientFundsException extends RuntimeException {

    private final Long accountId;

    /**
     * Constructor que genera un mensaje personalizado para la cuenta sin fondos.
     *
     * @param accountId ID de la cuenta que no tiene saldo suficiente
     */
    public InsufficientFundsException(Long accountId) {
        super(String.format("La cuenta con ID %d no tiene fondos suficientes.", accountId));
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }
}
