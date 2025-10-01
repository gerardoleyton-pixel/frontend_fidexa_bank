package com.example.controller;

import com.example.dto.request.BankAccountCreateDTO;
import com.example.dto.response.BankAccountDTO;
import com.example.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping
    public ResponseEntity<BankAccountDTO> createAccount(@Valid @RequestBody BankAccountCreateDTO dto) {
        BankAccountDTO created = bankAccountService.createAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(bankAccountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(bankAccountService.getAccountById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        bankAccountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<BankAccountDTO>> getAccountsByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(bankAccountService.getAccountsByUserId(userId));
    }
}
