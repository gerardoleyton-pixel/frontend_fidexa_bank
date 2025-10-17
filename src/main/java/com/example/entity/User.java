package com.example.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un usuario del sistema.
 * Cada usuario puede tener múltiples cuentas bancarias asociadas.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * ID único del usuario (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario. Debe ser único y no nulo.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Contraseña del usuario. No se debe exponer en respuestas.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Correo electrónico del usuario. Debe ser único y no nulo.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Nombre completo del usuario.
     */
    @Column
    private String fullName;

    /**
     * Lista de cuentas bancarias asociadas al usuario.
     * Se eliminan automáticamente si el usuario es eliminado.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccount> accounts = new ArrayList<>();

    // 🔹 Constructor vacío (obligatorio para JPA)
    public User() {}

    // 🔹 Constructor con parámetros
    public User(String username, String password, String email, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }

    // 🔹 Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) { // útil para pruebas unitarias
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<BankAccount> accounts) {
        this.accounts = accounts;
    }
}
