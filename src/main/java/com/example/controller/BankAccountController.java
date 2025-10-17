package com.example.controller;

import com.example.dto.request.BankAccountCreateDTO;
import com.example.dto.request.BankAccountUpdateDTO;
import com.example.dto.response.BankAccountDTO;
import com.example.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones relacionadas con cuentas bancarias.
 * Permite crear, consultar, actualizar y eliminar cuentas asociadas a usuarios.
 */
@RestController
@RequestMapping("/accounts")
@Tag(name = "Cuentas Bancarias", description = "Operaciones relacionadas con cuentas bancarias")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Operation(summary = "Crear una nueva cuenta bancaria", description = "Registra una cuenta asociada a un usuario existente")
    @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente")
    @PostMapping
    public ResponseEntity<BankAccountDTO> createAccount(@Valid @RequestBody BankAccountCreateDTO dto) {
        BankAccountDTO created = bankAccountService.createAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Obtener todas las cuentas bancarias", description = "Devuelve la lista completa de cuentas registradas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(bankAccountService.getAllAccounts());
    }

    @Operation(summary = "Obtener cuenta por ID", description = "Devuelve los datos de una cuenta específica")
    @ApiResponse(responseCode = "200", description = "Cuenta encontrada")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> getAccountById(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        return ResponseEntity.ok(bankAccountService.getAccountById(id));
    }

    @Operation(summary = "Actualizar cuenta por ID", description = "Modifica los datos de una cuenta existente")
    @ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<BankAccountDTO> updateAccount(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id,
            @Valid @RequestBody BankAccountUpdateDTO dto) {
        BankAccountDTO updated = bankAccountService.updateAccount(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar cuenta por ID", description = "Elimina una cuenta bancaria existente")
    @ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "ID de la cuenta") @PathVariable Long id) {
        bankAccountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener cuentas por ID de usuario", description = "Devuelve todas las cuentas asociadas a un usuario")
    @ApiResponse(responseCode = "200", description = "Cuentas encontradas")
    @GetMapping("/by-user")
    public ResponseEntity<List<BankAccountDTO>> getAccountsByUserId(
            @Parameter(description = "ID del usuario") @RequestParam Long userId) {
        return ResponseEntity.ok(bankAccountService.getAccountsByUserId(userId));
    }
}
