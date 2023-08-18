package com.demo.ebankingbackend.dtos;

import com.demo.ebankingbackend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class SavingBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus accountStatus;
    private CustomerDTO customerDTO;
    private double interestRate;
}
