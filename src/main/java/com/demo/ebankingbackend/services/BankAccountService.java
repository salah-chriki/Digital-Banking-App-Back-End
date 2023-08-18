package com.demo.ebankingbackend.services;

import com.demo.ebankingbackend.dtos.*;
import com.demo.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.demo.ebankingbackend.exceptions.BankBalanceNotSufficientException;
import com.demo.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentBankAccountDTO saveCurrentAccount(double initialBalance, double overDraft, Long CustomerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingAccount(double initialBalance, double interestRate, Long CustomerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundException;
    void debit(String bankAccountId,double amount,String description) throws BankAccountNotFoundException, BankBalanceNotSufficientException;
    void credit(String bankAccountId,double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BankBalanceNotSufficientException;

    List<BankAccountDTO> listBankAccount();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;


    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> getAccountOperations(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws CustomerNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);

    List<BankAccountDTO> getCustomerAccounts(Long customerId);
}
