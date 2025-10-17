package com.example.dto.response;

import java.util.List;

/**
 * DTO de respuesta para usuarios.
 * Contiene los datos relevantes que se devuelven al cliente.
 */
public class UserDTO {

    /**
     * ID único del usuario.
     */
    private Long id;

    /**
     * Nombre de usuario.
     */
    private String username;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Nombre completo del usuario.
     */
    private String fullName;

    /**
     * Lista de cuentas bancarias asociadas al usuario.
     */
    private List<BankAccountDTO> accounts;

    // 🔹 Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<BankAccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccountDTO> accounts) {
        this.accounts = accounts;
    }
}
