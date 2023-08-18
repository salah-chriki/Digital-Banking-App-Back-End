package com.demo.ebankingbackend.dtos;

import com.demo.ebankingbackend.entities.AccountOperation;
import com.demo.ebankingbackend.entities.Customer;
import com.demo.ebankingbackend.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
public class CurrentBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus accountStatus;
    private CustomerDTO customerDTO;
    private double overDraft;
}
