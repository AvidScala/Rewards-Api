package com.example.accessingdatarest;

import com.example.accessingdatarest.rewardsJpa.dao.CustomerDao;
import com.example.accessingdatarest.rewardsJpa.dao.RewardsTxnDao;
import com.example.accessingdatarest.rewardsJpa.entity.Customer;
import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxn;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RewardsTxnDaoTest {

    @Autowired
    private RewardsTxnDao rewardsTxnDao;

    @Test
    public void testSaveRewardsTxn() {
        Integer txnamt = 150;
        Integer totPoints = 6;

        RewardsTxn rewardsTxn = new RewardsTxn(100, 0, 5);
        rewardsTxn.setCustomerId(1);
        rewardsTxnDao.save(rewardsTxn);

        RewardsTxn rewardsTxn1 = new RewardsTxn(150, 1, 6);
        rewardsTxn1.setCustomerId(1);
        rewardsTxnDao.save(rewardsTxn1);
        Optional<RewardsTxn> rewardsTxn_actual = rewardsTxnDao.findFirstByCustomerIdOrderByCreatedOnDesc(1);
        assertNotNull(rewardsTxn_actual.get());
        assertEquals(rewardsTxn_actual.get().getPointsTodate(), totPoints);
        assertEquals(rewardsTxn_actual.get().getTransactionAmount(), txnamt);
    }




}