package com.example.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para transacciones.
 * Contiene los datos relevantes que se devuelven al cliente.
 */
public class TransactionDTO {

    /**
     * ID único de la transacción.
     */
    private Long id;

    /**
     * Tipo de transacción realizada.
     * Puede ser: DEPOSIT, WITHDRAW o TRANSFER.
     */
    private String type;

    /**
     * Monto involucrado en la transacción.
     */
    private BigDecimal amount;

    /**
     * Fecha y hora en que se registró la transacción.
     */
    private LocalDateTime timestamp;

    /**
     * ID de la cuenta bancaria asociada.
     */
    private Long accountId;

    /**
     * Descripción opcional de la transacción (usada en transferencias).
     */
    private String description;

    // 🔹 Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
