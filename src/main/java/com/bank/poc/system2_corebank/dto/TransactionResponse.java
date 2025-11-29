package com.bank.poc.system2_corebank.dto;

import lombok.Data;

@Data
public class TransactionResponse {
    private String status;   // SUCCESS / FAILED
    private String message;  // reason
    private Double balance;  // current balance (if available)
}
