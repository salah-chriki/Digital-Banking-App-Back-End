package com.demo.ebankingbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@DiscriminatorValue("CC")
@Data @AllArgsConstructor @NoArgsConstructor
public class CurrentBankAccount extends BankAccount{
    private double overDraft;
}
