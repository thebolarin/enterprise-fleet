package com.enterprise.fleet.user.repository;

import com.enterprise.fleet.user.entity.Customer;
import com.enterprise.fleet.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
