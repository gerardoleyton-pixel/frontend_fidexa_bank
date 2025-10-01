package com.example.exception.business;

import java.math.BigDecimal;

/**
 * Se lanza cuando se intenta hacer un retiro y no hay fondos suficientes.
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(BigDecimal attemptedAmount) {
        super("Fondos insuficientes para retirar $" + attemptedAmount + ". Verifica el saldo disponible.");
    }
}
