package com.example.BankingApplication.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    List<Transaction> findByAccountNumber(Long accountNumber);

}
