package com.example.accessingdatarest;

import com.example.accessingdatarest.rewardsJpa.dao.RewardsMonthlyDao;
import com.example.accessingdatarest.rewardsJpa.dao.RewardsTxnDao;
import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxn;
import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxnMonthly;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RewardsMonthlyDoaTest {

    @Autowired
    private RewardsMonthlyDao rewardsMonthlyDao;

    @Test
    public void testSaveRewardsMonthly() {
        Integer monthlyPoints = 100;
        Integer month = 72021;
        Integer custid = 1 ;

        RewardsTxnMonthly rewardsMonthly = new RewardsTxnMonthly(72021, 100 );
        rewardsMonthly.setCustomerId(1);
        rewardsMonthlyDao.save(rewardsMonthly);


        RewardsTxnMonthly rewardsMonthlyTxn_actual = rewardsMonthlyDao.findFirstByCustomerId(1);
        assertNotNull(rewardsMonthlyTxn_actual);
        assertEquals(rewardsMonthlyTxn_actual.getPointsPerMonth(), monthlyPoints);
        assertEquals(rewardsMonthlyTxn_actual.getCustomerId(), custid);
    }

    


}