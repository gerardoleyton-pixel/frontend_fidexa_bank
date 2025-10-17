package com.example.exception.message;

/*
 Mensajes de error reutilizables en toda la aplicación.
 Se utilizan en excepciones personalizadas y validaciones de negocio.
 */
public class ErrorMessages {

    // Fondos insuficientes por monto específico
    public static final String INSUFFICIENT_FUNDS_BY_AMOUNT =
            "Fondos insuficientes para retirar $%s. Verifica el saldo disponible.";

    // Fondos insuficientes por cuenta
    public static final String INSUFFICIENT_FUNDS_BY_ACCOUNT =
            "La cuenta con ID %d no tiene fondos suficientes.";

    // Cuenta no encontrada
    public static final String ACCOUNT_NOT_FOUND =
            "La cuenta con ID %d no existe.";

    // Transacción no encontrada
    public static final String TRANSACTION_NOT_FOUND =
            "La transacción con ID %d no existe.";

    // Monto inválido
    public static final String INVALID_AMOUNT =
            "El monto debe ser un valor positivo.";

    // Tipo de transacción inválido
    public static final String INVALID_TRANSACTION_TYPE =
            "Tipo de transacción inválido. Usa: DEPOSIT, WITHDRAW o TRANSFER.";

    // Entidad duplicada (por campo único)
    public static final String DUPLICATE_ENTITY =
            "%s con %s '%s' ya existe.";

    // Formato de fecha inválido
    public static final String INVALID_DATE_FORMAT =
            "Formato de fecha inválido. Usa el formato: yyyy-MM-dd.";

    // Validaciones de usuario
    public static final String USERNAME_REQUIRED =
            "El nombre de usuario es obligatorio.";
    public static final String USERNAME_LENGTH =
            "El nombre de usuario debe tener entre 4 y 20 caracteres.";
    public static final String EMAIL_REQUIRED =
            "El correo electrónico es obligatorio.";
    public static final String EMAIL_FORMAT =
            "El correo electrónico debe tener un formato válido.";
    public static final String FULLNAME_REQUIRED =
            "El nombre completo es obligatorio.";
    public static final String FULLNAME_LENGTH =
            "El nombre completo debe tener entre 3 y 50 caracteres.";
    public static final String PASSWORD_REQUIRED =
            "La contraseña es obligatoria.";
    public static final String PASSWORD_LENGTH =
            "La contraseña debe tener entre 6 y 100 caracteres.";

    // Validaciones de cuenta bancaria
    public static final String ACCOUNT_NUMBER_REQUIRED =
            "El número de cuenta es obligatorio.";
    public static final String ACCOUNT_HOLDER_REQUIRED =
            "El titular de la cuenta es obligatorio.";
    public static final String INITIAL_BALANCE_REQUIRED =
            "El saldo inicial es obligatorio.";
    public static final String INITIAL_BALANCE_MIN =
            "El saldo debe ser un valor positivo.";
    public static final String USER_ID_REQUIRED =
            "El ID del usuario es obligatorio.";

    // Validaciones de transacciones
    public static final String TRANSACTION_TYPE_REQUIRED =
            "El tipo de transacción es obligatorio (DEPOSIT, WITHDRAW o TRANSFER).";
    public static final String TRANSACTION_AMOUNT_REQUIRED =
            "El monto es obligatorio.";
    public static final String TRANSACTION_AMOUNT_MIN =
            "El monto debe ser un valor positivo.";
    public static final String TRANSACTION_ACCOUNT_ID_REQUIRED =
            "El ID de la cuenta es obligatorio.";
    public static final String TRANSACTION_TO_ACCOUNT_ID_REQUIRED =
            "El ID de la cuenta destino es obligatorio.";

    // Error inesperado
    public static final String UNEXPECTED_ERROR =
            "Ocurrió un error inesperado. Por favor, intenta más tarde.";
}
