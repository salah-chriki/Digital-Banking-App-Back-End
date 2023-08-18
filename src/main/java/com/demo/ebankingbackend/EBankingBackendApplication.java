package com.demo.ebankingbackend;

import com.demo.ebankingbackend.dtos.BankAccountDTO;
import com.demo.ebankingbackend.dtos.CurrentBankAccountDTO;
import com.demo.ebankingbackend.dtos.CustomerDTO;
import com.demo.ebankingbackend.dtos.SavingBankAccountDTO;
import com.demo.ebankingbackend.entities.*;
import com.demo.ebankingbackend.enums.AccountStatus;
import com.demo.ebankingbackend.enums.OperationType;
import com.demo.ebankingbackend.exceptions.BankAccountNotFoundException;
import com.demo.ebankingbackend.exceptions.BankBalanceNotSufficientException;
import com.demo.ebankingbackend.exceptions.CustomerNotFoundException;
import com.demo.ebankingbackend.mappers.BankAccountMapperImpl;
import com.demo.ebankingbackend.repositories.AccountOperationRepository;
import com.demo.ebankingbackend.repositories.BankAccountRepository;
import com.demo.ebankingbackend.repositories.CustomerRepository;
import com.demo.ebankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@AllArgsConstructor
public class EBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankingBackendApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService, BankAccountMapperImpl bankAccountMapper) {

        return args -> {
            Stream.of("Salah", "Othman", "Rim").forEach(name -> {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setName(name);
                customerDTO.setEmail(name + "@Gmail.com");
                bankAccountService.saveCustomer(customerDTO);
            });

            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentAccount(Math.random() * 5000, 500, customer.getId());
                    bankAccountService.saveSavingAccount(Math.random() * 7000, 3.5, customer.getId());

                } catch (CustomerNotFoundException
                        e) {
                    e.getStackTrace();
                }
            });
            List<BankAccountDTO> bankAccountList = bankAccountService.listBankAccount();
            for (BankAccountDTO ba : bankAccountList) {
                for (int i = 0; i < 10; i++) {
                    String accountId;
                    if (ba instanceof SavingBankAccountDTO) {
                        accountId = ((SavingBankAccountDTO) ba).getId();
                    } else {
                        accountId = ((CurrentBankAccountDTO) ba).getId();
                    }
                    bankAccountService.debit(accountId, 400 + Math.random() * 80, "DEBIT");
                    bankAccountService.credit(accountId, 500 + Math.random() * 300, "CREDIT");
                }
            }
        };
    }

//    //    @Bean
//    CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository) {
//        return args -> {
//            BankAccount bankAccount =
//                    bankAccountRepository.findById("45c20ee2-6ff7-49f4-9b12-168e3d35c429").orElse(null);
//
//            if (bankAccount != null) {
//                System.out.println(bankAccount.getId());
//                System.out.println(bankAccount.getBalance());
//                System.out.println(bankAccount.getAccountStatus());
//                System.out.println(bankAccount.getCreatedAt());
//                System.out.println(bankAccount.getCustomer().getName());
//                System.out.println(bankAccount.getClass().getSimpleName());
//                if (bankAccount instanceof CurrentAccount)
//                    System.out.println("OverDraft =>" + ((CurrentAccount) bankAccount).getOverDraft());
//                else if (bankAccount instanceof SavingAccount)
//                    System.out.println("InterestRate =>" + ((SavingAccount) bankAccount).getInterestRate());
//
//                bankAccount.getAccountOperations().forEach(accountOperation -> {
//                    System.out.println(accountOperation.getAmount() + "\t" + accountOperation.getOperationDate() + "\t" + accountOperation.getOperationType());
//                });
//            }
//        };
//    }

//@Bean
//    CommandLineRunner start(AccountOperationRepository accountOperationRepository,
//                            BankAccountRepository bankAccountRepository,
//                            CustomerRepository customerRepository
//    ) {
//        return args -> {
//            Stream.of("Salah", "Aboubakr", "Rim").forEach(name -> {
//                Customer customer = new Customer();
//                customer.setName(name);
//                customer.setEmail(name + "@Gmail.com");
//                customerRepository.save(customer);
//            });
//
//            customerRepository.findAll().forEach(customer -> {
//                CurrentBankAccount currentAccount = new CurrentBankAccount();
//                currentAccount.setId(UUID.randomUUID().toString());
//                currentAccount.setCustomer(customer);
//                currentAccount.setAccountStatus(AccountStatus.CREATED);
//                currentAccount.setBalance(Math.random() * 1000);
//                currentAccount.setCreatedAt(new Date());
//                currentAccount.setOverDraft(500);
//                bankAccountRepository.save(currentAccount);
//                SavingBankAccount savingAccount = new SavingBankAccount();
//                savingAccount.setId(UUID.randomUUID().toString());
//                savingAccount.setBalance(Math.random() * 1000);
//                savingAccount.setAccountStatus(AccountStatus.CREATED);
//                savingAccount.setCreatedAt(new Date());
//                savingAccount.setCreatedAt(new Date());
//                savingAccount.setInterestRate(2.5);
//                savingAccount.setCustomer(customer);
//                bankAccountRepository.save(savingAccount);
//            });
//            bankAccountRepository.findAll().forEach(bankAccount -> {
//                for (int i = 0; i < 4; i++) {
//                    AccountOperation accountOperation = new AccountOperation();
//                    accountOperation.setBankAccount(bankAccount);
//                    accountOperation.setOperationDate(new Date());
//                    accountOperation.setOperationType(Math.random() > 0.5 ? OperationType.CREDIT : OperationType.DEBIT);
//                    accountOperation.setAmount(Math.random() * 95000);
//                    accountOperationRepository.save(accountOperation);
//                }
//            });
//
//
//        };
//
//    }


};

