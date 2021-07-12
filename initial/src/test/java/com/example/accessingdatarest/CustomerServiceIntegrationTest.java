package com.example.accessingdatarest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.accessingdatarest.rewardsJpa.dao.CustomerDao;
import com.example.accessingdatarest.rewardsJpa.entity.Customer;
import com.example.accessingdatarest.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
public class CustomerServiceIntegrationTest {


    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerDao customerDao;

    @Before
    public void setUp() {
        Customer customer1 = new Customer(9999999999L,"First","Last","city");
        Customer customer2 = new Customer(9999999998L,"Second","SLast","scity");
        Customer customer3 = new Customer(9999999997L,"Third","TLast","tcity");
        customer1.setCustomerId(1);
        customer2.setCustomerId(1);
        customer3.setCustomerId(1);



        List<Customer> allCustomers = Arrays.asList(customer1, customer2, customer3);

        Mockito.when(customerDao.findByCustomerId(1)).thenReturn(Optional.of(customer1));
        Mockito.when(customerDao.findByPhoneNumber(9999999999L)).thenReturn(Optional.of(customer2));
        Mockito.when(customerDao.findByCustomerId(10)).thenReturn(null);
        Mockito.when(customerDao.findByPhoneNumber(899999999L)).thenReturn(null);
        Mockito.when(customerDao.findAll()).thenReturn(allCustomers);

    }

    @Test
    public void whenValidId_thenCustomerShouldBeFound() {
        Optional<Customer> found = customerService.getCustomerById(1);

        assertThat(found.get().getFirstName()).isEqualTo("First");
        verifyFindByIdIsCalledOnce(1);
    }

    @Test
    public void whenPhone_thenCustomerBeFound() {
        Optional<Customer> validPhone = customerService.getCustomerByPhoneNumber(9999999999L);

        assertThat(validPhone).isNull();


    }

    @Test
    public void whenInvalidPhone_thenNoCustomer() {
        Optional<Customer> noCust = customerService.getCustomerByPhoneNumber(7999999999L);
        assertThat(noCust).isNull();

        verifyFindByPhoneNumberIsCalledOnce(7999999999L);
    }

    @Test
    public void whenNonExistingId_thenCustomerShouldNotExist() {
        Optional<Customer> cust = customerService.getCustomerById(100);
        assertThat(cust).isNull();
        verifyFindByIdIsCalledOnce(100);

    }



    @Test
    public void whenInValidId_thenCustomerShouldNotBeFound() {
        Optional<Customer> fromDbNoCust = customerService.getCustomerById(-99);
        verifyFindByIdIsCalledOnce(-99);
        assertThat(fromDbNoCust).isNull();
    }

    @Test
    public void given3Customers_whengetAll_thenReturn3Records() {

        List<Customer> allCustomers = customerService.getCustomers();
        verifyGetCustomersIsCalledOnce();
        assertThat(allCustomers).hasSize(3);
                //.extracting(Customer::getFirstName).contains(customer1.getFirstName(), customer2.getFirstName(), customer3.getFirstName);
    }

    private void verifyFindByPhoneNumberIsCalledOnce(long phNum) {
        Mockito.verify(customerDao, VerificationModeFactory.times(1)).findByPhoneNumber(phNum);
        Mockito.reset(customerDao);
    }

    private void verifyFindByIdIsCalledOnce(Integer custId) {
        Mockito.verify(customerDao, VerificationModeFactory.times(1)).findByCustomerId(custId);
        Mockito.reset(customerDao);
    }

    private void verifyGetCustomersIsCalledOnce() {
        Mockito.verify(customerDao, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(customerDao);
    }
}