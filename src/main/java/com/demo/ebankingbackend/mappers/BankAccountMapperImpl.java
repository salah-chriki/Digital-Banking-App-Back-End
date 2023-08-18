package com.demo.ebankingbackend.mappers;

import com.demo.ebankingbackend.dtos.AccountOperationDTO;
import com.demo.ebankingbackend.dtos.CurrentBankAccountDTO;
import com.demo.ebankingbackend.dtos.CustomerDTO;
import com.demo.ebankingbackend.dtos.SavingBankAccountDTO;
import com.demo.ebankingbackend.entities.AccountOperation;
import com.demo.ebankingbackend.entities.CurrentBankAccount;
import com.demo.ebankingbackend.entities.Customer;
import com.demo.ebankingbackend.entities.SavingBankAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO=new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer=new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentBankAccount currentBankAccount){
        CurrentBankAccountDTO currentBankAccountDTO=new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentBankAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentBankAccount.getCustomer()));
        currentBankAccountDTO.setType(currentBankAccount.getClass().getSimpleName());
        return currentBankAccountDTO;
    }

    public CurrentBankAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO){
        CurrentBankAccount currentBankAccount=new CurrentBankAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentBankAccount);
        currentBankAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentBankAccount;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingBankAccount savingBankAccount){
        SavingBankAccountDTO savingBankAccountDTO=new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingBankAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingBankAccount.getCustomer()));
        savingBankAccountDTO.setType(savingBankAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }

    public SavingBankAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO){
        SavingBankAccount savingBankAccount=new SavingBankAccount();
        BeanUtils.copyProperties(savingBankAccount,savingBankAccountDTO);
        savingBankAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return savingBankAccount;
    }
    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;
    }

    public AccountOperation fromAccountOperationDTO(AccountOperationDTO accountOperationDTO){
        AccountOperation accountOperation=new AccountOperation();
        BeanUtils.copyProperties(accountOperationDTO,accountOperation);
        return accountOperation;
    }
}
