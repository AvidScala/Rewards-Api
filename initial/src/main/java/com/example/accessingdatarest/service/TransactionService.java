package com.example.accessingdatarest.service;

import com.example.accessingdatarest.exception.Exception;
import com.example.accessingdatarest.exception.MethodArgumentNotValidException;
import com.example.accessingdatarest.exception.ResourceNotFoundException;
import com.example.accessingdatarest.model.RewardsResponse;
import com.example.accessingdatarest.rewardsJpa.dao.CustomerDao;
import com.example.accessingdatarest.rewardsJpa.dao.RewardsMonthlyDao;
import com.example.accessingdatarest.rewardsJpa.dao.RewardsTxnDao;
import com.example.accessingdatarest.rewardsJpa.entity.Customer;
import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxn;
import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxnMonthly;

import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Integer.parseInt;

@Service
@Slf4j
@Transactional
public class TransactionService {

	@Autowired
	RewardsTxnDao rewardsTxnDao;

	@Autowired
	CustomerDao customerDao;

	@Autowired
	RewardsMonthlyDao rewardsMonthlyDao;

	private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

	public RewardsTxn createAtransaction(Integer customerId, int txnAmount) throws
			ResourceNotFoundException,
			MethodArgumentNotValidException {

		Integer tot_txn_points = 0;
		Integer tot_points_to_date = 0;
		Integer tot_points_to_month = 0;

		LocalDate currentdate = LocalDate.now();
		int currentDay = currentdate.getDayOfMonth();
		YearMonth ymCurrent = YearMonth.now();
		Integer monthYear = Integer.valueOf(ymCurrent.format(DateTimeFormatter.ofPattern("MMyyyy")));

		//Checking if the customer exists 
		Customer cust = customerDao.findByCustomerId(customerId).orElseThrow(() -> new ResourceNotFoundException(
				"Customer is not existing " + customerId
		));
		RewardsTxn rewardsTxn = null;

		//Calculate the reward points
		if (txnAmount >= 100) {
			tot_txn_points = (txnAmount - 100) * 2 + (txnAmount / 50);
		}
		if (txnAmount < 100 && txnAmount >= 50) {
			tot_txn_points = (txnAmount / 50);
		}

		//Getting prev Reward Points of the customer to keep track of reward points

		Optional<Integer> rewardsTxn1 = rewardsTxnDao.findFirstByCustomerIdOrderByCreatedOnDesc(customerId).map(RewardsTxn::getPointsTodate);

		//Getting the prev monthly reward points for the customer
		RewardsTxnMonthly rewardsMonthly = rewardsMonthlyDao.findFirstByCustomerId(customerId);

		tot_points_to_date = rewardsTxn1.isPresent() ? (rewardsTxn1.get() + tot_txn_points) : tot_txn_points;

		RewardsTxnMonthly rewardsMonthly1 = new RewardsTxnMonthly();
		if (rewardsMonthly != null) {
			if (currentDay == 1) {
				tot_points_to_month = tot_txn_points;

				rewardsMonthly.setCustomerId(customerId);
				rewardsMonthly.setMonthyear(monthYear);
				rewardsMonthly.setCreatedOn(new Date());
				rewardsMonthly.setPointsPerMonth(tot_points_to_month);
				rewardsMonthlyDao.save(rewardsMonthly);
				logger.debug("The transaction saved in the monthly is {} ", rewardsMonthly.toString());

			} else {
				tot_points_to_month = rewardsMonthly.getPointsPerMonth() + tot_txn_points;
				rewardsMonthly.setPointsPerMonth(tot_points_to_month);
				rewardsMonthly.setCreatedOn(new Date());
				rewardsMonthlyDao.save(rewardsMonthly);
				logger.debug("The transaction saved in the monthly is  {}", rewardsMonthly.toString());
			}

		} else {
			tot_points_to_month = tot_txn_points;
			rewardsMonthly1.setCustomerId(customerId);
			rewardsMonthly1.setMonthyear(monthYear);
			rewardsMonthly1.setPointsPerMonth(tot_points_to_month);
			rewardsMonthly1.setCreatedOn(new Date());
			rewardsMonthlyDao.save(rewardsMonthly1);
			logger.debug("The transaction saved in the monthly is {} ", rewardsMonthly1.toString());
		}

		rewardsTxn = new RewardsTxn(txnAmount, tot_txn_points, tot_points_to_date);
		rewardsTxn.setCustomerId(customerId);
		try {
			 rewardsTxnDao.save(rewardsTxn);
			 logger.debug("The transaction saved is {} ", rewardsTxn.toString());
			 return rewardsTxn;
		} catch (MethodArgumentNotValidException e) {
			throw new MethodArgumentNotValidException("Either customer Id or Txnamount format is invalid :" + customerId + txnAmount);
		}catch(Exception e){
			throw new Exception("Insertion failed in exception " + e.getLocalizedMessage());
		}
	}

	public RewardsResponse getRewards(Integer customerId, Integer numOfMonths) throws
			ResourceNotFoundException {

		logger.debug("Getting rewards for a total of {}  for customerId {}",  numOfMonths, customerId);

		Customer c = new Customer();
		RewardsResponse rewardsResponse = null;

		YearMonth ymCurrent = YearMonth.now();
		YearMonth fromYm = ymCurrent.minusMonths(numOfMonths);

		int toDate = parseInt(ymCurrent.format(DateTimeFormatter.ofPattern("MMyyyy")));
		int fromDate = parseInt(fromYm.format(DateTimeFormatter.ofPattern("MMyyyy")));
		logger.debug("Getting rewards between {} and {} ", fromDate, toDate);

		//Getting the customer
		Customer customer = customerDao.findByCustomerId(customerId).orElseThrow(() -> new ResourceNotFoundException(
				"Customer not found " + customerId
		));
		logger.debug("The customer is {} ", customer.toString());

		Optional<List<RewardsTxnMonthly>> rewards = rewardsMonthlyDao.findByCustomerIdAndMonthyearBetween(customerId, fromDate, toDate);

		logger.debug("rewards are {}", rewards);

		if (!rewards.get().isEmpty()) {
				int totalPoints = rewards.get().stream().collect(Collectors.summingInt(RewardsTxnMonthly::getPointsPerMonth));
				rewardsResponse = new RewardsResponse(totalPoints, rewards.get());
				logger.debug("the number of points are {}, {}  ", rewards, totalPoints);
		}else {
			throw new ResourceNotFoundException("Customer transactions were not enough to earn points for the months specified" +
					"," + customerId);
		}
		return rewardsResponse;
	}
}
