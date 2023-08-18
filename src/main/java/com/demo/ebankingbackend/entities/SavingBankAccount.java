package com.demo.ebankingbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@DiscriminatorValue("SA")
@Data @AllArgsConstructor @NoArgsConstructor
public class SavingBankAccount extends BankAccount{
    private double interestRate;
}
