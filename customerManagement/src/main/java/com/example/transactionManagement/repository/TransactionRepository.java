package com.example.transactionManagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.transactionManagement.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	@Query("SELECT t FROM Transaction t WHERE MONTH(t.transactionDate) BETWEEN :startMonth AND :endMonth")
	List<Transaction> findByTransactionDateBetweenMonth(int startMonth, int endMonth);

	List<Transaction> findByCustomerName(String customerName);

}
