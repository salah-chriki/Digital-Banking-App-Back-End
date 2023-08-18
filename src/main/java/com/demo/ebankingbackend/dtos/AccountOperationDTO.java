package com.demo.ebankingbackend.dtos;

import com.demo.ebankingbackend.entities.BankAccount;
import com.demo.ebankingbackend.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType operationType;
    private String description;
}
