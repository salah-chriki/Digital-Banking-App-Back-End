package com.demo.ebankingbackend.web;

import com.demo.ebankingbackend.dtos.*;
import com.demo.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.demo.ebankingbackend.exceptions.BankBalanceNotSufficientException;
import com.demo.ebankingbackend.exceptions.CustomerNotFoundException;
import com.demo.ebankingbackend.services.BankAccountServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestAPI {
    BankAccountServiceImpl bankAccountService;

    @GetMapping("/accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(id);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> getBankAccounts() {
        return bankAccountService.listBankAccount();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
        return bankAccountService.getAccountOperations(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAcountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "4") int size
    ) throws CustomerNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    @GetMapping("/accounts/{accountId}/credit")
    public void credit(@PathVariable String accountId,
                       @RequestParam double amount,
                       @RequestParam String description) throws BankAccountNotFoundException {
        bankAccountService.credit(accountId,amount,description);
    }

    @GetMapping("/accounts/{accountId}/debit")
    public void debit(@PathVariable String accountId,
                       @RequestParam double amount,
                       @RequestParam String description) throws BankAccountNotFoundException, BankBalanceNotSufficientException {
        bankAccountService.debit(accountId,amount,description);
    }

    @GetMapping("/accounts/{accountIdSource}/transfer")
    public void transfer(@PathVariable String accountIdSource,
                         @RequestParam String accountIdDestination,
                         @RequestParam double amount) throws BankAccountNotFoundException, BankBalanceNotSufficientException {
        bankAccountService.transfer(accountIdSource,accountIdDestination,amount);
    }
    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
         bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
         return creditDTO;
    }
    @PostMapping("/accounts/debit")
    public DebitDTO credit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BankBalanceNotSufficientException {
         bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
         return debitDTO;
    }
    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BankBalanceNotSufficientException {
        bankAccountService.transfer(transferRequestDTO.getAccountIdSource(),transferRequestDTO.getAccountIdDestination(), transferRequestDTO.getAmount());
    }
}

