package com.example.accessingdatarest;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import com.example.accessingdatarest.controllers.RewardsController;
import com.example.accessingdatarest.model.RewardsResponse;
import com.example.accessingdatarest.rewardsJpa.entity.Customer;
import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxn;
import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxnMonthly;
import com.example.accessingdatarest.service.CustomerService;
import com.example.accessingdatarest.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RewardsController.class)
class RewardsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private CustomerService customerService;

    @Test
    void whenPostCustomer_thenReturns200() throws Exception {

        Customer customer =  new Customer(9999999999L,"Firs", "last","city");

        MvcResult result =  mockMvc.perform(post("/customer/")
                .content(objectMapper.writeValueAsString(customer))
                .contentType("application/json"))
                .andExpect(status().isOk()).andReturn();
        assertNotNull(result);
        String actualResponseBody = result.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(customer));
    }

    @Test
    void testGetCustomers() throws Exception {
        when(customerService.getCustomers()).thenReturn(Arrays.asList(
         new Customer(99999999999L, "first","last","city")
        ));


        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk());
    }


    @Test
    void testGetCustomerById() throws Exception {
        Optional<Customer> customer = Optional.of(new Customer(99999999999L, "first", "last", "city"));
        customer.get().setCustomerId(1);
        when(customerService.getCustomerById(1)).thenReturn(
                customer
        );
      mockMvc.perform(get("/customers/Id/1"))
                .andExpect(status().isOk()).andExpect(content().contentType("application/json"));

    }

    @Test
    void testPostATransaction() throws Exception {
        Integer tot_points = 100;
        Integer txn_amount = 200;
        Integer points_to_date = 10;
        RewardsTxn rewardsTxn = new RewardsTxn(txn_amount,tot_points,points_to_date);
        rewardsTxn.setCustomerId(1);
        rewardsTxn.setCreatedOn(new Date());
        rewardsTxn.setRewardPoints(0);
        rewardsTxn.setPointsTodate(20);
        rewardsTxn.setRedeemedPoints(null);
        when(transactionService.createAtransaction(1,10)).thenReturn(
        rewardsTxn);

         mockMvc.perform(post("/transaction/")
                 .content(objectMapper.writeValueAsString(rewardsTxn))
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetRewards() throws Exception {
        Integer customerId = 1;
        Integer monthYear = 72021;
        Integer totalPoints = 10;
        Integer numOfMonths = 3;
        RewardsTxnMonthly rewardsTxnMonthly = new RewardsTxnMonthly(monthYear, totalPoints);
        rewardsTxnMonthly.setCustomerId(customerId);
        rewardsTxnMonthly.setCreatedOn(new Date());

        RewardsResponse rewardsResponse = new RewardsResponse(
           100, Arrays.asList(rewardsTxnMonthly));

        System.out.println(transactionService.toString());
        when(transactionService.getRewards(1, 3)).thenReturn(null);
              //  rewardsResponse);


         mockMvc.perform(get("/rewardPoints/1")
                 .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRewardsValid() throws Exception {
        Integer customerId = 1;
        Integer monthYear = 72021;
        Integer totalPoints = 10;
        Integer numOfMonths = 3;
        RewardsTxnMonthly rewardsTxnMonthly = new RewardsTxnMonthly(monthYear, totalPoints);
        rewardsTxnMonthly.setCustomerId(customerId);
        rewardsTxnMonthly.setCreatedOn(new Date());

        RewardsResponse rewardsResponse = new RewardsResponse(
                100, Arrays.asList(rewardsTxnMonthly));

        when(transactionService.getRewards(1, 3)).thenReturn(rewardsResponse);


        mockMvc.perform(get("/rewardPoints/1/?numOfMonths=3")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetRewardsInValidNumofMonths() throws Exception {
        Integer customerId = 1;
        Integer monthYear = 72021;
        Integer totalPoints = 10;
        Integer numOfMonths = 3;
        RewardsTxnMonthly rewardsTxnMonthly = new RewardsTxnMonthly(monthYear, totalPoints);
        rewardsTxnMonthly.setCustomerId(customerId);
        rewardsTxnMonthly.setCreatedOn(new Date());

        RewardsResponse rewardsResponse = new RewardsResponse(
                100, Arrays.asList(rewardsTxnMonthly));

        when(transactionService.getRewards(1, 3)).thenReturn(rewardsResponse);


        mockMvc.perform(get("/rewardPoints/1/?numOfMonths=24")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotAcceptable());
    }
}