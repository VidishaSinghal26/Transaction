package com.example.transactionManagement.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transactionManagement.entity.Transaction;
import com.example.transactionManagement.service.RewardService;

@RestController
@RequestMapping("/rewards")
public class TransactionController {
	
    @Autowired
    private RewardService rewardService;

    @PostMapping("/add")
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return rewardService.addTransaction(transaction);
    }

    @GetMapping("/getAllRewards")
    public Map<String, Map<String, Integer>> getRewards() {
        return rewardService.getMonthlyCustomerRewards();
    }

    @GetMapping("/getRewardsByCustomer/{customerName}")
    public Map<String, Map<String, Integer>> getRewardByName(@PathVariable String customerName) {
        return rewardService.getRewardByName(customerName);
    }

 
    @GetMapping("/totalRewardsForAll")
    public Map<String, Integer> getTotalRewardsForAllCustomers() {
        return rewardService.getTotalRewardsForAllCustomers();
    }

    
    @GetMapping("/transactionsByMonth/{month}")
    public List<Transaction> getTransactionsByMonth(@PathVariable int month) {
        return rewardService.getTransactionsByMonth(month);
    }


}
