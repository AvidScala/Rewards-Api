package com.example.accessingdatarest.controllers;

import com.example.accessingdatarest.exception.Exception;
import com.example.accessingdatarest.exception.ResourceNotFoundException;
import com.example.accessingdatarest.model.RewardsResponse;
import com.example.accessingdatarest.rewardsJpa.entity.Customer;
import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxn;
import com.example.accessingdatarest.service.CustomerService;
import com.example.accessingdatarest.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.MultiValueMap;

import javax.validation.constraints.NotBlank;
import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;


@RestController
@Validated
@Slf4j
public class RewardsController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransactionService transactionService;

    private static final Logger logger = LoggerFactory.getLogger(RewardsController.class);

    /**
     *
     * @param customer
     * @return
     */

    @PostMapping(path = "/customer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer)
     {
            logger.debug("The new customer is {} ", customer.toString());
            customerService.createCustomer(customer);
            return new ResponseEntity<>(customer, HttpStatus.OK);

    }

    /**
     *
     * @param rewardsTxn
     * @return
     */
    // For inserting into the Transaction DB
    @PostMapping(path = "/transaction" , consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody RewardsTxn rewardsTxn){

            try {
                logger.debug("RewardsTxn is ", rewardsTxn);
                logger.debug("The CustomerId & txnamount is  {}, {}", rewardsTxn.getCustomerId(), rewardsTxn.getTransactionAmount());
                RewardsTxn rewardsTxn1 = transactionService.createAtransaction(rewardsTxn.getCustomerId(), rewardsTxn.getTransactionAmount());
                return new ResponseEntity<>(rewardsTxn1, HttpStatus.OK);
            }catch (Exception e){
                throw new Exception("Valid input format for txnAmount is only Integer, no decimals " + e.getMessage());
            }
    }

    /**
     *
     * @return
     * @throws ResourceNotFoundException
     */
    //Getting all the customers in the DB
    @GetMapping("/customers")
    public ResponseEntity<?> allCustomers() throws ResourceNotFoundException {
        List<Customer> customers = customerService.getCustomers();
        if (customers != null) {
            return new ResponseEntity<>(customerService.getCustomers(), HttpStatus.OK);
        }
        return new ResponseEntity<>("No customers in the DB ", HttpStatus.NOT_FOUND);
    }

    /**
     *
     * @param phoneNumber
     * @return
     */
    //Getting the customers using the phone number
    @GetMapping(path = "/customers/phoneNumber/{phoneNumber}")
    public ResponseEntity<?> findCustomer(@PathVariable Long phoneNumber)
    {
        logger.debug("The phone number is {}" , phoneNumber  );
        Optional<Customer> cust;
        cust  = customerService.getCustomerByPhoneNumber(phoneNumber);
        return new ResponseEntity<> (cust, HttpStatus.OK);
    }

    /**
     *
     * @param customerId
     * @return
     */
    @GetMapping(path = "/customers/Id/{customerId}")
    public ResponseEntity<?> findCustomerById(@PathVariable Integer customerId)
    {
        logger.debug("The customer Id is {} " , customerId  );
        Optional<Customer> cust =  customerService.getCustomerById(customerId);
        return new ResponseEntity<> (cust, HttpStatus.OK);
    }

    /**
     *
     * @param customerId
     * @param numOfMonths
     * @return
     */
    //Getting the reward Points
    @GetMapping(path = "/rewardPoints/{customerId}/")
    public ResponseEntity<?> getRewards(@PathVariable Integer customerId,
                                        @RequestParam(required = false, defaultValue = "3") Integer numOfMonths)
    {

        logger.debug("Getting rewards for customer id  {}, during {} months " , customerId, numOfMonths  );
        if(numOfMonths >= 0 && numOfMonths <= 12) {
            RewardsResponse rewardsResponse = transactionService.getRewards(customerId, numOfMonths);
            return new ResponseEntity<> (rewardsResponse, HttpStatus.OK);
        }
        return new ResponseEntity<> ("NumofMonths should be in between 1 - 12", HttpStatus.NOT_ACCEPTABLE);


    }
}
