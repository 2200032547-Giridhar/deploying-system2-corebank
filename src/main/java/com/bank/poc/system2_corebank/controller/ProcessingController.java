package com.bank.poc.system2_corebank.controller;


import com.bank.poc.system2_corebank.dto.TransactionRequest;
import com.bank.poc.system2_corebank.dto.TransactionResponse;
import com.bank.poc.system2_corebank.entity.Card;
import com.bank.poc.system2_corebank.entity.Transaction;
import com.bank.poc.system2_corebank.service.CoreBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/core")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ProcessingController {

    private final CoreBankService service;

    @PostMapping("/process")
    public TransactionResponse process(@RequestBody TransactionRequest req) {
        // System 2 assumes basic validation done in System 1, but still safe.
        return service.processTransaction(req);
    }

    // For Super Admin UI
    @GetMapping("/transactions")
    public List<Transaction> allTransactions() {
        return service.getAllTransactions();
    }

    // For Customer UI
    @GetMapping("/transactions/{cardNumber}")
    public List<Transaction> cardTransactions(@PathVariable String cardNumber) {
        return service.getTransactionsForCard(cardNumber);
    }

    @GetMapping("/cards/{cardNumber}")
    public Card getCard(@PathVariable String cardNumber) {
        return service.getCard(cardNumber);
    }
}
