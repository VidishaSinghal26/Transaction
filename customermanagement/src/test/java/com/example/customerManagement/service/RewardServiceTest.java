package com.example.customerManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.transactionManagement.entity.Transaction;
import com.example.transactionManagement.exceptions.CustomerNotFoundException;
import com.example.transactionManagement.exceptions.InvalidTransactionException;
import com.example.transactionManagement.exceptions.NoTransactionsFoundException;
import com.example.transactionManagement.repository.TransactionRepository;
import com.example.transactionManagement.service.RewardService;

public class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardService rewardService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this); // Initializes @Mock annotations
    }


    @Test
    public void testAddTransaction_Success() {
        Transaction transaction = new Transaction(1L, "Alice", 100.0, LocalDate.now());
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction savedTransaction = rewardService.addTransaction(transaction);
        assertNotNull(savedTransaction);
        assertEquals("Alice", savedTransaction.getCustomerName());
    }

   
    @Test
    public void testAddTransaction_InvalidAmount() {
        Transaction transaction = new Transaction(1L, "Alice", -50.0, LocalDate.now());

        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class, () -> {
            rewardService.addTransaction(transaction);
        });

        assertEquals("Transaction amount cannot be zero or negative.", exception.getMessage());
    }

    
    @Test
    public void testCalculateRewardPoints() {
        int points = rewardService.calculateRewardPoints(120.0);
        assertEquals(90, points);
    }

    
    @Test
    public void testCalculateRewardPoints_InvalidAmount() {
        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class, () -> {
            rewardService.calculateRewardPoints(-50.0);
        });

        assertEquals("Amount cannot be zero or negative.", exception.getMessage());
    }

   
    @Test
    public void testGetMonthlyCustomerRewards_Success() {
        List<Transaction> transactions = Arrays.asList(
            new Transaction(1L, "Alice", 120.0, LocalDate.now()),
            new Transaction(2L, "Alice", 75.0, LocalDate.now())
        );

        when(transactionRepository.findByTransactionDateBetweenMonth(anyInt(), anyInt())).thenReturn(transactions);

        Map<String, Map<String, Integer>> rewards = rewardService.getMonthlyCustomerRewards();
        assertNotNull(rewards);
        assertEquals(1, rewards.get("Alice").size());
        assertEquals(115, rewards.get("Alice").get(LocalDate.now().getMonth().toString()));
    }

    
    @Test
    public void testGetMonthlyCustomerRewards_NoTransactions() {
        when(transactionRepository.findByTransactionDateBetweenMonth(anyInt(), anyInt())).thenReturn(Collections.emptyList());

        NoTransactionsFoundException exception = assertThrows(NoTransactionsFoundException.class, () -> {
            rewardService.getMonthlyCustomerRewards();
        });

        assertEquals("No transactions found in the last 3 months.", exception.getMessage());
    }
    
    @Test
    public void testGetTotalRewardsForAllCustomers() {
        List<Transaction> transactions = Arrays.asList(
            new Transaction(1L, "Alice", 120.0, LocalDate.now()),
            new Transaction(2L, "Bob", 200.0, LocalDate.now())
        );

        when(transactionRepository.findAll()).thenReturn(transactions);

        Map<String, Integer> rewards = rewardService.getTotalRewardsForAllCustomers();
        assertEquals(90, rewards.get("Alice"));
        assertEquals(250, rewards.get("Bob"));
    }

    
    @Test
    public void testGetTotalRewardsForAllCustomers_NoTransactions() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        NoTransactionsFoundException exception = assertThrows(NoTransactionsFoundException.class, () -> {
            rewardService.getTotalRewardsForAllCustomers();
        });

        assertEquals("No transactions available.", exception.getMessage());
    }

    
   
}
