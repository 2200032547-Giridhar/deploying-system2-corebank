package com.bank.poc.system2_corebank.service;

import com.bank.poc.system2_corebank.dto.TransactionRequest;
import com.bank.poc.system2_corebank.dto.TransactionResponse;
import com.bank.poc.system2_corebank.entity.Card;
import com.bank.poc.system2_corebank.entity.Transaction;
import com.bank.poc.system2_corebank.repository.CardRepository;
import com.bank.poc.system2_corebank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreBankService {

    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    // =============================
    // Process Transaction (Step 6)
    // =============================
    public TransactionResponse processTransaction(TransactionRequest req) {

        TransactionResponse res = new TransactionResponse();

        // 1. Check if card exists
        Card card = cardRepository.findById(req.getCardNumber()).orElse(null);
        if (card == null) {
            return fail(req, res, "Invalid card");
        }

        // 2. Validate PIN using SHA-256 hashing
        String inputHash = sha256(req.getPin());
        if (!inputHash.equals(card.getPinHash())) {
            return fail(req, res, "Invalid PIN");
        }

        // 3. Validate amount > 0
        if (req.getAmount() <= 0) {
            return fail(req, res, "Amount must be > 0");
        }

        // 4. Validate transaction type and balance
        switch (req.getType()) {

            case "withdraw" -> {
                if (card.getBalance() < req.getAmount()) {
                    return fail(req, res, "Insufficient balance");
                }
                card.setBalance(card.getBalance() - req.getAmount());
            }

            case "topup" -> {
                card.setBalance(card.getBalance() + req.getAmount());
            }

            default -> {
                return fail(req, res, "Invalid transaction type");
            }
        }

        // 5. Save updated card balance
        cardRepository.save(card);

        // 6. Log success transaction
        log(req, "SUCCESS", "OK");

        // 7. Prepare response
        res.setStatus("SUCCESS");
        res.setMessage("Transaction successful");
        res.setBalance(card.getBalance());

        return res;
    }

    // =============================
    // Helper: Failure Response
    // =============================
    private TransactionResponse fail(TransactionRequest req, TransactionResponse res, String reason) {
        log(req, "FAILED", reason);
        res.setStatus("FAILED");
        res.setMessage(reason);
        return res;
    }

    // =============================
    // Helper: Transaction Logging
    // =============================
    private void log(TransactionRequest req, String status, String reason) {
        Transaction t = new Transaction();
        t.setCardNumber(req.getCardNumber());
        t.setType(req.getType());
        t.setAmount(req.getAmount());
        t.setTimestamp(LocalDateTime.now());
        t.setStatus(status);
        t.setReason(reason);
        transactionRepository.save(t);
    }

    // =============================
    // Helper: SHA-256 Hash Method
    // =============================
    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 hashing failed", e);
        }
    }

    // =============================
    // UI Helpers For Admin/Customer
    // =============================
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsForCard(String cardNumber) {
        return transactionRepository.findByCardNumber(cardNumber);
    }

    public Card getCard(String cardNumber) {
        return cardRepository.findById(cardNumber).orElse(null);
    }
}
