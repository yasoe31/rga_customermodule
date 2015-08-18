package com.rga.customermodule.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rga.customermodule.model.Customer;
import com.rga.customermodule.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	CustomerRepository customerRepository;

	public List<Customer> listCustomer() {

		return (List<Customer>) customerRepository.findAll();

	}

	public String saveCustomer(Customer customer) {

		String temp = null;

		if (customer.getName() == null || customer.getName().isEmpty()) {
			temp = "Name should not be empty";
		}

		if (customer.getPhNo() == null || customer.getPhNo().isEmpty()) {
			temp = "Phno should not be empty";
		}

		if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
			temp = "Email should not be empty";
		}

		if (temp == null) {
			try {
				customerRepository.save(customer);
			} catch (Exception e) {
				return "could not save customer";
			}
		}
		return temp;

	}

	public Customer getACustomer(int id) {
		return customerRepository.findOne(id);
	}
	
	public void deleteCustomer(int id) {
		customerRepository.delete(id);
	}


}
