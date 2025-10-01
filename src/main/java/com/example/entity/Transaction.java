package com.example.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private BankAccount bankAccount;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String type; // "DEPOSIT" o "WITHDRAWAL"

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    public Transaction() {}

    public Transaction(BankAccount bankAccount, BigDecimal amount, String type) {
        this.bankAccount = bankAccount;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    // Agregado para pruebas unitarias
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
}
