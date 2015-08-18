package com.rga.customermodule.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.classic.Logger;

import com.rga.customermodule.model.Customer;
import com.rga.customermodule.service.CustomerService;

@Controller
public class CustomerController {

	@Autowired
	CustomerService customerService;

	private static final Logger LOGGER = (Logger) LoggerFactory
			.getLogger(CustomerController.class);

	@RequestMapping(value = "/listCustomer", method = RequestMethod.GET)
	@ResponseBody
	public List<Customer> listCustomer() {

		LOGGER.debug("Listing All Customer");
		return customerService.listCustomer();
	}

	@RequestMapping(value = "/registerCustomer", method = RequestMethod.POST)
	public ResponseEntity<String> registerPerson(@RequestBody Customer customer) {

		LOGGER.debug("Registering A Customer");
		try {
			if (customerService.saveCustomer(customer) == null) {

				return new ResponseEntity<String>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(
						customerService.saveCustomer(customer),
						HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/getACustomer/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getACustomer(@PathVariable String id) {

		try {
			LOGGER.debug("Getting a Customer");
			return new ResponseEntity<Object>(
					customerService.getACustomer(Integer.parseInt(id)),
					HttpStatus.OK);
		} catch (NumberFormatException e) {
			return new ResponseEntity<Object>("number expected",
					HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/updateCustomer", method = { RequestMethod.POST,
			RequestMethod.PUT })
	@ResponseBody
	public ResponseEntity<String> updateCustomer(@RequestBody Customer customer) {

		try {
			LOGGER.debug("Updating A Customer");
			Customer c = customerService.getACustomer(customer.getId());
			c.setEmail(customer.getEmail());
			c.setName(customer.getName());
			c.setPhNo(customer.getPhNo());
			customerService.saveCustomer(c);

			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (InvalidDataAccessApiUsageException e) {
			return new ResponseEntity<String>("customer id expected",
					HttpStatus.NOT_FOUND);
		} catch (NullPointerException e) {
			return new ResponseEntity<String>("could not find",
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/deleteCustomer/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteCustomer(@PathVariable String id) {

		try {

			Integer.parseInt(id);
			LOGGER.debug("Deleting A Customer");
			customerService.deleteCustomer(Integer.parseInt(id));
			return new ResponseEntity<String>(HttpStatus.OK);

		} catch (NumberFormatException e) {

			return new ResponseEntity<String>("number expected",
					HttpStatus.BAD_REQUEST);

		} catch (EmptyResultDataAccessException erdae) {

			return new ResponseEntity<String>("could not find the customer",
					HttpStatus.NOT_FOUND);
		}
	}

}
