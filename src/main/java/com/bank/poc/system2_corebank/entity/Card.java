package com.bank.poc.system2_corebank.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Card {

    @Id
    private String cardNumber;

    private String pinHash;      // SHA-256 hash of PIN
    private double balance;
    private String customerName;
}
