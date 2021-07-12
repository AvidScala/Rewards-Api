package com.example.accessingdatarest.service;

import com.example.accessingdatarest.exception.DuplicateResourceException;
import com.example.accessingdatarest.exception.Exception;
import com.example.accessingdatarest.exception.MethodArgumentNotValidException;
import com.example.accessingdatarest.exception.ResourceNotFoundException;
import com.example.accessingdatarest.rewardsJpa.dao.CustomerDao;
import com.example.accessingdatarest.rewardsJpa.entity.Customer;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Optional;

@Service

public class CustomerService {

	@Autowired
	CustomerDao customerDao;

	public List<Customer> getCustomers(){
		List<Customer> customers = customerDao.findAll();
		return customers;
	}

	public Optional<Customer> getCustomerByPhoneNumber(Long phoneNumber) throws ResourceNotFoundException{

		Optional<Customer> customer = Optional.ofNullable(customerDao.findByPhoneNumber(phoneNumber).orElseThrow(
				() -> new ResourceNotFoundException("Customer not found for the phoneNumber :: " + phoneNumber)
		));
		return customer;
	}

	public Optional<Customer> getCustomerById(Integer customerId){

		Optional<Customer> customer = Optional.ofNullable(customerDao.findByCustomerId(customerId).orElseThrow(
				() -> new ResourceNotFoundException("Customer not found for this id :: " + customerId)
		));
		return customer;
	}



	public Customer createCustomer(Customer c)
			throws MethodArgumentNotValidException, DuplicateResourceException {
		Optional<Customer> cust;
		cust = customerDao.findByPhoneNumber(c.getPhoneNumber());

		try {
			if (cust.isPresent()) {
				throw new DuplicateResourceException("Customer already exists " + c.getPhoneNumber());
			} else {
				customerDao.save(c);
			}
		} catch (MethodArgumentNotValidException ex) {
		throw new DuplicateResourceException("Customer Phone number is in the system :" + c.getPhoneNumber());
		} catch (Exception ex) {
		throw new Exception("exception while adding the customer : " + ex.getLocalizedMessage());
		}
		return c;
	}
}
