package com.rga.customermodule.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.rga.customermodule.config.DatabaseConfig;
import com.rga.customermodule.controller.CustomerController;
import com.rga.customermodule.model.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DatabaseConfig.class, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("sampleData.xml")
@WebAppConfiguration
@Transactional
public class CustomerControllerTest {

	@Autowired
	private CustomerController customerController;

	@Test
	public void test_list_customer() throws Exception {
		List<Customer> customerList = customerController.listCustomer();
		assertEquals(2, customerList.size());
	}

	@Test
	public void when_customer_name_is_empty_should_not_save() throws Exception {
		Customer customer = new Customer();
		customer.setEmail("a@email.com");
		customer.setPhNo("00");

		customerController.registerPerson(customer);

		List<Customer> personList = customerController.listCustomer();
		assertEquals(2, personList.size());
	}

	@Test
	public void when_customer_email_is_empty_should_not_save() throws Exception {
		Customer customer = new Customer();
		customer.setName("a");
		customer.setPhNo("00");

		customerController.registerPerson(customer);

		List<Customer> personList = customerController.listCustomer();
		assertEquals(2, personList.size());

	}

	@Test
	public void when_customer_phone_is_empty_should_not_save() throws Exception {
		Customer customer = new Customer();
		customer.setName("a");
		customer.setEmail("a@email.com");

		ResponseEntity<String> response = customerController
				.registerPerson(customer);

		List<Customer> personList = customerController.listCustomer();
		assertEquals(2, personList.size());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void test_register_customer() throws Exception {

		Customer customer = new Customer();
		customer.setEmail("test@email.com");
		customer.setName("test");
		customer.setPhNo("000");
		customerController.registerPerson(customer);

		List<Customer> personList = customerController.listCustomer();
		assertEquals(3, personList.size());
	}

	@Test
	public void test_get_a_customer() throws Exception {
		ResponseEntity<Object> response = customerController.getACustomer("1");
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void test_get_a_custer_id_non_int_should_throw_exception() {
		ResponseEntity<Object> response = customerController.getACustomer("a");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("number expected", response.getBody());
	}
	
	@Test
	public void test_get_a_customer_not_in_the_list_should_return_null(){
		ResponseEntity<Object> response = customerController.getACustomer("3");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	public void test_update_a_customer() throws Exception {

		Customer customer = new Customer();
		customer.setId(1);
		customer.setEmail("c@email.com");
		customer.setName("c");
		customer.setPhNo("999");

		ResponseEntity<String> response = customerController
				.updateCustomer(customer);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void test_update_customer_without_id_should_throw_exception() {
		Customer customer = new Customer();
		customer.setEmail("c@email.com");
		customer.setName("c");
		customer.setPhNo("999");

		ResponseEntity<String> response = customerController
				.updateCustomer(customer);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("could not find", response.getBody());
	}

	@Test
	public void test_delete_a_customer() throws Exception {
		customerController.deleteCustomer("1");

		List<Customer> personList = customerController.listCustomer();
		assertEquals(1, personList.size());
	}

	@Test
	public void test_delete_a_customer_not_in_the_list_should_say_404() {
		ResponseEntity<String> response = customerController
				.deleteCustomer("3");

		List<Customer> personList = customerController.listCustomer();
		assertEquals(2, personList.size());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

}
