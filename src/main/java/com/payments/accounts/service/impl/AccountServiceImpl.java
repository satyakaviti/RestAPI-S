package com.payments.accounts.service.impl;

import com.payments.accounts.constants.AccountsConstants;
import com.payments.accounts.dto.AccountsDto;
import com.payments.accounts.dto.CustomerDto;
import com.payments.accounts.entity.AccountsEntity;
import com.payments.accounts.entity.CustomerEntity;
import com.payments.accounts.exception.CustomException;
import com.payments.accounts.mapper.AccountMapper;
import com.payments.accounts.mapper.CustomerMapper;
import com.payments.accounts.repository.AccountsRepository;
import com.payments.accounts.repository.CustomerRepository;
import com.payments.accounts.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private CustomerRepository customerRepository;
    private AccountsRepository accountsRepository;
    /**
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        CustomerEntity customer = CustomerMapper.mapToCustomer(customerDto, new CustomerEntity());
        Optional<CustomerEntity> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomException.CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        CustomerEntity savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer, customerDto));
    }
    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private AccountsEntity createNewAccount(CustomerEntity customer, CustomerDto customerDto) {
        AccountsEntity newAccount = new AccountsEntity();
        newAccount.setCustomerId(customer.getCustomerId());

        // Generate a random account number
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);

        // Get accountType and branchAddress from AccountsDto in customerDto
        AccountsDto accountsDto = customerDto.getAccountsDto();
        String accountType = accountsDto != null && accountsDto.getAccountType() != null
                ? accountsDto.getAccountType()
                : AccountsConstants.SAVINGS; // Default to SAVINGS if not provided
        String branchAddress = accountsDto != null && accountsDto.getBranchAddress() != null
                ? accountsDto.getBranchAddress()
                : AccountsConstants.ADDRESS; // Default to ADDRESS if not provided

        newAccount.setAccountType(accountType);
        newAccount.setBranchAddress(branchAddress);

        return newAccount;
    }


    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        CustomerEntity customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        AccountsEntity accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            AccountsEntity accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new CustomException.ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            CustomerEntity customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new CustomException.ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        CustomerEntity customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }
}
