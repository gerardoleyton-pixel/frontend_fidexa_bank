package com.example.dto.response;

import java.math.BigDecimal;

/**
 * DTO de respuesta para cuentas bancarias.
 * Contiene los datos relevantes que se devuelven al cliente.
 */
public class BankAccountDTO {

    /**
     * ID único de la cuenta bancaria.
     */
    private Long id;

    /**
     * Número de cuenta asignado al cliente.
     */
    private String accountNumber;

    /**
     * Nombre completo del titular de la cuenta.
     */
    private String accountHolder;

    /**
     * Saldo actual disponible en la cuenta.
     */
    private BigDecimal balance;

    /**
     * ID del usuario asociado a la cuenta.
     */
    private Long userId;

    /**
     * Nombre completo del usuario asociado.
     */
    private String userName;

    // 🔹 Getters y setters
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
