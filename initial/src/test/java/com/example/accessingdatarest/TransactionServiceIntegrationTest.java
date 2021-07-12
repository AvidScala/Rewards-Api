package com.example.accessingdatarest;

import com.example.accessingdatarest.exception.ResourceNotFoundException;
import com.example.accessingdatarest.model.RewardsResponse;
import com.example.accessingdatarest.rewardsJpa.dao.CustomerDao;
import com.example.accessingdatarest.rewardsJpa.dao.RewardsMonthlyDao;
import com.example.accessingdatarest.rewardsJpa.dao.RewardsTxnDao;
import com.example.accessingdatarest.rewardsJpa.entity.Customer;
import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxn;
import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxnMonthly;
import com.example.accessingdatarest.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
public class TransactionServiceIntegrationTest {


    @Autowired
    private TransactionService transactionService;

    @MockBean
    private RewardsTxnDao rewardsTxnDao;

    @MockBean
    private CustomerDao customerDao;

    @MockBean
    RewardsMonthlyDao rewardsMonthlyDao;

    @Before
    public void setUp() {

        Customer customer1 = new Customer(9999999999L,"First","Last","city");
        customer1.setCustomerId(1);


        RewardsTxn rewardsTxn = new RewardsTxn(100,10,10);
        rewardsTxn.setTxn_id(1);
        rewardsTxn.setCustomerId(1);
        rewardsTxn.setRedeemedPoints(null);
        rewardsTxn.setCreatedOn(new Date());

        RewardsTxn rewardsTxn1 = new RewardsTxn(120,10,20);
        rewardsTxn.setTxn_id(2);
        rewardsTxn.setCustomerId(1);
        rewardsTxn.setRedeemedPoints(null);
        rewardsTxn.setCreatedOn(new Date());

        RewardsTxnMonthly rewardsTxnMonthly = new RewardsTxnMonthly(72021, 20);
        rewardsTxnMonthly.setMonthlyTxnId(1);
        rewardsTxnMonthly.setCustomerId(1);
        rewardsTxnMonthly.setCreatedOn(new Date());

        RewardsTxnMonthly rewardsTxnMonthly1 = new RewardsTxnMonthly(32021, 20);
        rewardsTxnMonthly.setMonthlyTxnId(1);
        rewardsTxnMonthly.setCustomerId(1);
        rewardsTxnMonthly.setCreatedOn(new Date());

        RewardsTxnMonthly rewardsTxnMonthly2 = new RewardsTxnMonthly(42021, 20);
        rewardsTxnMonthly.setMonthlyTxnId(1);
        rewardsTxnMonthly.setCustomerId(1);
        rewardsTxnMonthly.setCreatedOn(new Date());

        RewardsResponse rewardsResponse = new RewardsResponse(60, Arrays.asList(
                rewardsTxnMonthly,
                rewardsTxnMonthly1,
                rewardsTxnMonthly2
        )

        );

        Mockito.when(rewardsTxnDao.findFirstByCustomerIdOrderByCreatedOnDesc(1)).thenReturn(Optional.of(rewardsTxn1));
        Mockito.when(rewardsMonthlyDao.findFirstByCustomerId(1)).thenReturn(rewardsTxnMonthly);


        Mockito.when(rewardsMonthlyDao.findByCustomerIdAndMonthyearBetween(1,32021,72021)).thenReturn(Optional.of(
                Arrays.asList(rewardsTxnMonthly, rewardsTxnMonthly1, rewardsTxnMonthly2

                )));
        Mockito.when(customerDao.findByCustomerId(1)).thenReturn(Optional.of(customer1));

    }

    @Test
    public void whenValid_thenCreateTxn() {
            RewardsTxn  rewardsTxn = transactionService.createAtransaction(1, 100);
            //RewardsTxnMonthly rewardsTxnMonthly
            assertThat(rewardsTxn.getPointsTodate()).isEqualTo(22);
            assertThat(rewardsTxn.getCustomerId()).isEqualTo(1);
            assertThat(rewardsTxn.getRewardPoints()).isEqualTo(2);
            verifyFindByCustomerIdForTxnPointsIsCalledOnce(1);
            verifyCreateTxnIsCalledOnce(rewardsTxn);
            verifyCreateMonthlyTxnIsCalledOnce();
            verifyGetCustomersIsCalledOnce(1);
    }

    @Test
    public void whenCust_not_Found_thenException() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.createAtransaction(2, 100);
        });
        String expectedMessage = "Customer is not existing ";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whencustomerFound_GetRewards() {

        RewardsResponse rewardsResponse_actual = transactionService.getRewards(1, 3);
        assertThat(rewardsResponse_actual).isNotNull();
        assertThat(rewardsResponse_actual.getTotalPoints()).isEqualTo(60);
        verifyGetCustomersIsCalledOnce(1);
    }

    @Test
    public void whencustomerNoFound_GetRewards_Exception() {

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getRewards(2, 3);
        });

        String expectedMessage1 = "Customer is not existing ";
        String actualMessage1 = exception.getMessage();

        assertTrue(actualMessage1.contains(expectedMessage1));
    }

    private void verifyFindByCustomerIdForTxnPointsIsCalledOnce(Integer custId) {
        Mockito.verify(rewardsTxnDao, VerificationModeFactory.times(1)).findFirstByCustomerIdOrderByCreatedOnDesc(custId);
        Mockito.reset(rewardsTxnDao);
    }

    private void verifyCreateTxnIsCalledOnce(RewardsTxn rewardsTxn) {
        Mockito.verify(rewardsTxnDao, VerificationModeFactory.times(1)).save(rewardsTxn);
        Mockito.reset(rewardsTxnDao);
    }

    private void verifyCreateMonthlyTxnIsCalledOnce() {
        Mockito.verify(rewardsMonthlyDao, VerificationModeFactory.times(1)).save(Mockito.any());
        Mockito.reset(rewardsMonthlyDao);
    }
    private void verifyGetCustomersIsCalledOnce(Integer custId) {
        Mockito.verify(customerDao, VerificationModeFactory.times(1)).findByCustomerId(custId);
        Mockito.reset(customerDao);
    }
}