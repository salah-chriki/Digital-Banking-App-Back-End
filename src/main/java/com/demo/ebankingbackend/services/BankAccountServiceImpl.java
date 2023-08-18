package com.demo.ebankingbackend.services;

import com.demo.ebankingbackend.dtos.*;
import com.demo.ebankingbackend.entities.*;
import com.demo.ebankingbackend.enums.OperationType;
import com.demo.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.demo.ebankingbackend.exceptions.BankBalanceNotSufficientException;
import com.demo.ebankingbackend.exceptions.CustomerNotFoundException;
import com.demo.ebankingbackend.mappers.BankAccountMapperImpl;
import com.demo.ebankingbackend.repositories.AccountOperationRepository;
import com.demo.ebankingbackend.repositories.BankAccountRepository;
import com.demo.ebankingbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl bankAccountMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("save new customer");
        Customer customer = customerRepository.save(bankAccountMapper.fromCustomerDTO(customerDTO));
        return bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Customer not found");

        CurrentBankAccount currentAccount = new CurrentBankAccount();
        currentAccount.setBalance(initialBalance);
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        currentAccount.setCreatedAt(new Date());
        bankAccountRepository.save(currentAccount);
        return bankAccountMapper.fromCurrentBankAccount(currentAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException("Customer not found");
        SavingBankAccount savingAccount = new SavingBankAccount();
        savingAccount.setBalance(initialBalance);
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        savingAccount.setCreatedAt(new Date());
        bankAccountRepository.save(savingAccount);
        return bankAccountMapper.fromSavingBankAccount(savingAccount);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customerList = customerRepository.findAll();
        return customerList.stream().map(customer -> bankAccountMapper.fromCustomer(customer)).collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        if (bankAccount instanceof SavingBankAccount) {
            return bankAccountMapper.fromSavingBankAccount((SavingBankAccount) bankAccount);
        } else {
            return bankAccountMapper.fromCurrentBankAccount((CurrentBankAccount) bankAccount);
        }

    }

    @Override
    public void debit(String bankAccountId, double amount, String description) throws BankAccountNotFoundException, BankBalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        if (amount > bankAccount.getBalance())
            throw new BankBalanceNotSufficientException("Bank account balance not sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setOperationDate(new Date());
        accountOperation.setAmount(amount);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setDescription(description);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        accountOperationRepository.save(accountOperation);
    }

    @Override
    public void credit(String bankAccountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.CREDIT);
        accountOperation.setOperationDate(new Date());
        accountOperation.setAmount(amount);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setDescription(description);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        accountOperationRepository.save(accountOperation);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BankBalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> listBankAccount() {
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        return bankAccountList.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingBankAccount) {
                return bankAccountMapper.fromSavingBankAccount((SavingBankAccount) bankAccount);
            } else {
                return bankAccountMapper.fromCurrentBankAccount((CurrentBankAccount) bankAccount);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = customerRepository.save(bankAccountMapper.fromCustomerDTO(customerDTO));
        return bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> getAccountOperations(String accountId) {
        List<AccountOperation> listAccountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return listAccountOperations.stream().map(accountOperation -> bankAccountMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws CustomerNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw new CustomerNotFoundException("Customer not found");
        Page<AccountOperation> operationsPage = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOList = operationsPage.getContent().stream().map(accountOperation -> bankAccountMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOList);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(operationsPage.getTotalPages());
        accountHistoryDTO.setCurrentPage(page);
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customerList=customerRepository.searchCustomers(keyword);
        return customerList.stream().map(customer -> bankAccountMapper.fromCustomer(customer)).collect(Collectors.toList());
    }

    @Override
    public List<BankAccountDTO> getCustomerAccounts(Long customerId){
        List<BankAccount> bankAccountList = bankAccountRepository.findByCustomerId(customerId);
        return bankAccountList.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingBankAccount) {
                return bankAccountMapper.fromSavingBankAccount((SavingBankAccount) bankAccount);
            } else {
                return bankAccountMapper.fromCurrentBankAccount((CurrentBankAccount) bankAccount);
            }
        }).collect(Collectors.toList());

    }
}
