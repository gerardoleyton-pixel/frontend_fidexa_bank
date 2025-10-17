package com.example.dto.request;

import com.example.exception.message.ErrorMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para la creación de usuarios.
 * Contiene validaciones para asegurar que los datos sean correctos y completos.
 */
public class UserCreateDTO {

    @NotBlank(message = ErrorMessages.USERNAME_REQUIRED)
    @Size(min = 4, max = 20, message = ErrorMessages.USERNAME_LENGTH)
    private String username;

    @NotBlank(message = ErrorMessages.EMAIL_REQUIRED)
    @Email(message = ErrorMessages.EMAIL_FORMAT)
    private String email;

    @NotBlank(message = ErrorMessages.FULLNAME_REQUIRED)
    @Size(min = 3, max = 50, message = ErrorMessages.FULLNAME_LENGTH)
    private String fullName;

    @NotBlank(message = ErrorMessages.PASSWORD_REQUIRED)
    @Size(min = 6, max = 100, message = ErrorMessages.PASSWORD_LENGTH)
    private String password;

    // 🔹 Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username != null ? username.trim() : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName != null ? fullName.trim() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
