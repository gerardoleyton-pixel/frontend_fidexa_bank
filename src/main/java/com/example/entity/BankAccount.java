package com.example.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/*
 Entidad que representa una cuenta bancaria.
 Cada cuenta está asociada a un usuario y tiene un saldo.
 */
@Entity
@Table(name = "bank_account")
public class BankAccount {

    /*
     ID único de la cuenta bancaria (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     Número de cuenta. Debe ser único y no nulo.
     */
    @Column(nullable = false, unique = true)
    private String accountNumber;

    /*
     Titular de la cuenta. Se asigna al momento de la creación.
     */
    @Column(name = "account_holder", nullable = false)
    private String accountHolder;

    /*
     Saldo actual de la cuenta. No puede ser nulo.
     */
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    /*
     Usuario propietario de la cuenta.
     Relación muchos-a-uno: varias cuentas pueden pertenecer a un mismo usuario.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Constructor vacío (obligatorio para JPA)
    public BankAccount() {}

    //Constructor con parámetros
    public BankAccount(String accountNumber, BigDecimal balance, User user) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.user = user;
    }

    //Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
