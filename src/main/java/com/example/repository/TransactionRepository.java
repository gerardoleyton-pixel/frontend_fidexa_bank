package com.example.repository;

import com.example.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBankAccountId(Long bankAccountId);
    List<Transaction> findByType(String type);
    List<Transaction> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<Transaction> findByBankAccountIdAndType(Long bankAccountId, String type);
}
