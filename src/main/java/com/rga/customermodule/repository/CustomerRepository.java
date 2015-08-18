package com.rga.customermodule.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rga.customermodule.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}
