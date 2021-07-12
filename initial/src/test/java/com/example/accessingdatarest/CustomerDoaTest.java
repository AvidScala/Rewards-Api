package com.example.accessingdatarest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.accessingdatarest.rewardsJpa.dao.CustomerDao;
import com.example.accessingdatarest.rewardsJpa.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerDoaTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void testSaveCustomer() {

        Customer customer = new Customer(9999999999L, "TestFirst", "TestLast", "TestCity");
        customerDao.save(customer);
        Optional<Customer> CustomerAct = customerDao.findByPhoneNumber(9999999999L);
        assertNotNull(CustomerAct.get());
        assertEquals(CustomerAct.get().getFirstName(), customer.getFirstName());
        assertEquals(CustomerAct.get().getLastName(), customer.getLastName());
    }

    @Test
    public void testGetCustomerByPhoneNumber() {

        Customer customer = new Customer(9999999999L, "TestFirst", "TestLast", "TestCity");
        customerDao.save(customer);
        List<Customer> CustomerAct = customerDao.findAll();
        assertNotNull(CustomerAct.get(0));
        assertEquals(CustomerAct.get(0).getFirstName(), customer.getFirstName());
        assertEquals(CustomerAct.get(0).getLastName(), customer.getLastName());
    }


}