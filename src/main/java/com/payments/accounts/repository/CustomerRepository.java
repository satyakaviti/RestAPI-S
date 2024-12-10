package com.payments.accounts.repository;

import com.payments.accounts.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {
   Optional<CustomerEntity> findByMobileNumber(String mobileNumber);
}
