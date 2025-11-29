package com.bank.poc.system2_corebank.repository;

import com.bank.poc.system2_corebank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardNumber(String cardNumber);
}
