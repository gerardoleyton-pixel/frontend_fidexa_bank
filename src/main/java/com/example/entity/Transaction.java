package com.example.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa una transacción bancaria.
 * Puede ser de tipo DEPOSIT, WITHDRAW o TRANSFER.
 */
@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    /**
     * ID único de la transacción (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cuenta bancaria asociada a la transacción.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    /**
     * Monto de la transacción.
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * Tipo de transacción: DEPOSIT, WITHDRAW o TRANSFER.
     */
    @Column(nullable = false)
    private String type;

    /**
     * Fecha y hora en que se realizó la transacción.
     */
    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Descripción de la transacción (usada especialmente en transferencias).
     * Se permite null para evitar problemas con migraciones y para registros antiguos.
     */
    @Column(nullable = true)
    private String description;

    // 🔹 Constructor vacío requerido por JPA
    public Transaction() {}

    // 🔹 Constructor usado en depósito y retiro
    public Transaction(BankAccount bankAccount, BigDecimal amount, String type) {
        this.bankAccount = bankAccount;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.description = type + " de $" + amount;
    }

    // 🔹 Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
