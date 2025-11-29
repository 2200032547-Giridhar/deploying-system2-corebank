package com.bank.poc.system2_corebank;

import com.bank.poc.system2_corebank.entity.Card;
import com.bank.poc.system2_corebank.repository.CardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class System2CorebankApplication {

    public static void main(String[] args) {
        SpringApplication.run(System2CorebankApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(CardRepository cardRepository) {
        return args -> {
            Card c = new Card();
            c.setCardNumber("4123456789012345");
            c.setPinHash(SHA256.hash("1234"));
            c.setBalance(1000.0);
            c.setCustomerName("John Doe");
            cardRepository.save(c);
        };
    }
}
