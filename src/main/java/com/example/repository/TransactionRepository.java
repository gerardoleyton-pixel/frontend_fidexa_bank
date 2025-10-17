package com.example.repository;

import com.example.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 🔹 Buscar transacciones por ID de cuenta
    List<Transaction> findByBankAccountId(Long bankAccountId);

    // 🔹 Buscar transacciones por tipo (DEPOSIT, WITHDRAW, TRANSFER)
    List<Transaction> findByType(String type);

    // 🔹 Buscar transacciones por rango de fechas
    List<Transaction> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    // 🔹 Buscar transacciones por cuenta y tipo (útil para filtros combinados)
    List<Transaction> findByBankAccountIdAndType(Long bankAccountId, String type);
}
