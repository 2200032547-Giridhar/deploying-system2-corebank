package com.bank.poc.system2_corebank.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String cardNumber;
    private String pin;
    private double amount;
    private String type; // "withdraw" or "topup"
}
