package com.example.transactionManagement.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.transactionManagement.entity.Transaction;
import com.example.transactionManagement.exceptions.CustomerNotFoundException;
import com.example.transactionManagement.exceptions.InvalidTransactionException;
import com.example.transactionManagement.exceptions.NoTransactionsFoundException;
import com.example.transactionManagement.repository.TransactionRepository;

@Service
public class RewardService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction addTransaction(Transaction transaction) {
        if (transaction.getAmount() <= 0) {
            throw new InvalidTransactionException("Transaction amount cannot be zero or negative.");
        }
        return transactionRepository.save(transaction);
    }


    public Map<String, Map<String, Integer>> getMonthlyCustomerRewards() {
        int startMonth = LocalDate.now().getMonthValue() - 2;
        int endMonth = LocalDate.now().getMonthValue();

        List<Transaction> transactions = transactionRepository.findByTransactionDateBetweenMonth(startMonth, endMonth);

        if (transactions.isEmpty()) {
            throw new NoTransactionsFoundException("No transactions found in the last 3 months.");
        }

        Map<String, Map<String, Integer>> rewardsByCustomer = new HashMap<>();

        for (Transaction txn : transactions) {
            String customerName = txn.getCustomerName();
            String month = txn.getTransactionDate().getMonth().toString();
            int points = calculateRewardPoints(txn.getAmount());

            rewardsByCustomer.putIfAbsent(customerName, new HashMap<>());
            rewardsByCustomer.get(customerName).merge(month, points, Integer::sum);
        }

        return rewardsByCustomer;
    }


    public List<Transaction> getRewardByName(String customerName) {
        List<Transaction> transactions = transactionRepository.findByCustomerName(customerName);
        if (transactions.isEmpty()) {
            throw new CustomerNotFoundException("No transactions found for customer: " + customerName);
        }
        return transactions;
    }

    public int calculateRewardPoints(double amount) {
        if (amount < 0) {
            throw new InvalidTransactionException("Amount cannot be zero or negative.");
        }

        int points = 0;
        if (amount > 100) {
            points += (amount - 100) * 2;
            amount = 100;
        }
        if (amount > 50) {
            points += (amount - 50);
        }
        return points;
    }

    public int getTotalRewardPointsByCustomer(String customerName) {
        List<Transaction> transactions = transactionRepository.findByCustomerName(customerName);
        if (transactions.isEmpty()) {
            throw new CustomerNotFoundException("No transactions found for customer: " + customerName);
        }

        return transactions.stream()
                .mapToInt(txn -> calculateRewardPoints(txn.getAmount()))
                .sum();
    }

    public Map<String, Integer> getTotalRewardsForAllCustomers() {
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            throw new NoTransactionsFoundException("No transactions available.");
        }

        Map<String, Integer> totalRewards = new HashMap<>();
        for (Transaction txn : transactions) {
            String customerName = txn.getCustomerName();
            int points = calculateRewardPoints(txn.getAmount());
            totalRewards.merge(customerName, points, Integer::sum);
        }

        return totalRewards;
    }

    public List<Transaction> getTransactionsByMonth(int month) {
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetweenMonth(month, month);
        if (transactions.isEmpty()) {
            throw new NoTransactionsFoundException("No transactions found for month: " + month);
        }
        return transactions;
    }

    public void deleteTransaction(Long transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new InvalidTransactionException("Transaction not found with ID: " + transactionId);
        }
        transactionRepository.deleteById(transactionId);
    }

    public Transaction updateTransaction(Long transactionId, double newAmount, LocalDate newDate) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new InvalidTransactionException("Transaction not found with ID: " + transactionId));

        if (newAmount <= 0) {
            throw new InvalidTransactionException("Transaction amount cannot be zero or negative.");
        }

        transaction.setAmount(newAmount);
        transaction.setTransactionDate(newDate);

        return transactionRepository.save(transaction);
    }
}

