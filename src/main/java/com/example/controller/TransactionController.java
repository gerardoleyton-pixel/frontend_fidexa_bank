package com.example.controller;

import com.example.dto.request.DepositRequestDTO;
import com.example.dto.request.TransferRequestDTO;
import com.example.dto.request.WithdrawRequestDTO;
import com.example.dto.response.TransactionDTO;
import com.example.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controlador REST para operaciones relacionadas con transacciones bancarias.
 * Permite realizar depósitos, retiros, transferencias y consultas por filtros.
 */
@RestController
@RequestMapping("/transactions")
@Tag(name = "Transacciones", description = "Operaciones bancarias como depósitos, retiros, transferencias y consultas")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Obtener todas las transacciones", description = "Devuelve la lista completa de transacciones registradas")
    @ApiResponse(responseCode = "200", description = "Transacciones obtenidas exitosamente")
    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @Operation(summary = "Obtener transacción por ID", description = "Devuelve los datos de una transacción específica")
    @ApiResponse(responseCode = "200", description = "Transacción encontrada")
    @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(
            @Parameter(description = "ID de la transacción") @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @Operation(summary = "Realizar depósito", description = "Deposita un monto en una cuenta bancaria")
    @ApiResponse(responseCode = "200", description = "Depósito realizado exitosamente")
    @ApiResponse(responseCode = "400", description = "Monto inválido")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    @PostMapping("/deposit")
    public ResponseEntity<TransactionDTO> deposit(@Valid @RequestBody DepositRequestDTO dto) {
        return ResponseEntity.ok(transactionService.deposit(dto.getAccountId(), dto.getAmount()));
    }

    @Operation(summary = "Realizar retiro", description = "Retira un monto desde una cuenta bancaria")
    @ApiResponse(responseCode = "200", description = "Retiro realizado exitosamente")
    @ApiResponse(responseCode = "400", description = "Monto inválido o fondos insuficientes")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDTO> withdraw(@Valid @RequestBody WithdrawRequestDTO dto) {
        return ResponseEntity.ok(transactionService.withdraw(dto.getAccountId(), dto.getAmount()));
    }

    @Operation(summary = "Realizar transferencia", description = "Transfiere un monto entre dos cuentas bancarias")
    @ApiResponse(responseCode = "200", description = "Transferencia realizada exitosamente")
    @ApiResponse(responseCode = "400", description = "Monto inválido o fondos insuficientes")
    @ApiResponse(responseCode = "404", description = "Cuenta origen o destino no encontrada")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> transfer(@Valid @RequestBody TransferRequestDTO dto) {
        return ResponseEntity.ok(transactionService.transfer(dto.getFromAccountId(), dto.getToAccountId(), dto.getAmount()));
    }

    @Operation(summary = "Obtener transacciones por cuenta", description = "Devuelve todas las transacciones asociadas a una cuenta")
    @ApiResponse(responseCode = "200", description = "Transacciones encontradas")
    @GetMapping("/by-account")
    public ResponseEntity<List<TransactionDTO>> getByAccount(
            @Parameter(description = "ID de la cuenta") @RequestParam Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }

    @Operation(summary = "Obtener transacciones por tipo", description = "Filtra transacciones por tipo: DEPOSIT, WITHDRAW, TRANSFER")
    @ApiResponse(responseCode = "200", description = "Transacciones filtradas por tipo")
    @GetMapping("/by-type")
    public ResponseEntity<List<TransactionDTO>> getByType(
            @Parameter(description = "Tipo de transacción") @RequestParam String type) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(type));
    }

    @Operation(summary = "Obtener transacciones por rango de fechas", description = "Filtra transacciones entre dos fechas (formato yyyy-MM-dd)")
    @ApiResponse(responseCode = "200", description = "Transacciones encontradas en el rango")
    @ApiResponse(responseCode = "400", description = "Formato de fecha inválido")
    @GetMapping("/by-date-range")
    public ResponseEntity<List<TransactionDTO>> getByDateRange(
            @Parameter(description = "Fecha de inicio (yyyy-MM-dd)") @RequestParam String start,
            @Parameter(description = "Fecha de fin (yyyy-MM-dd)") @RequestParam String end
    ) {
        try {
            LocalDateTime startDate = LocalDate.parse(start).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(end).atTime(23, 59, 59);
            return ResponseEntity.ok(transactionService.getTransactionsByDateRange(startDate, endDate));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
