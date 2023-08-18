package com.demo.ebankingbackend.web;

import com.demo.ebankingbackend.dtos.BankAccountDTO;
import com.demo.ebankingbackend.dtos.CustomerDTO;
import com.demo.ebankingbackend.entities.Customer;
import com.demo.ebankingbackend.exceptions.CustomerNotFoundException;
import com.demo.ebankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService;


    @GetMapping("/customers")
    public List<CustomerDTO> customers() {
        return bankAccountService.listCustomers();
    }

    @GetMapping("/customers/search")
    public List<CustomerDTO> customers(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        return bankAccountService.searchCustomers("%" + keyword + "%");
    }

    @GetMapping("customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/new-customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable(name = "customerId") Long customerId, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        bankAccountService.deleteCustomer(id);
    }

    @GetMapping("/customer-accounts/{id}")
    public List<BankAccountDTO> customers(@PathVariable(name = "id") Long id) {
        return bankAccountService.getCustomerAccounts(id);
    }
}
