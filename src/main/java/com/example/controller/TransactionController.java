package com.example.controller;

import com.example.dto.response.TransactionDTO;
import com.example.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionDTO> deposit(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(transactionService.deposit(accountId, amount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDTO> withdraw(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(transactionService.withdraw(accountId, amount));
    }

    @GetMapping("/by-account")
    public ResponseEntity<List<TransactionDTO>> getByAccount(@RequestParam Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<TransactionDTO>> getByType(@RequestParam String type) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(type));
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<TransactionDTO>> getByDateRange(@RequestParam String start, @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return ResponseEntity.ok(transactionService.getTransactionsByDateRange(startDate, endDate));
    }
}
