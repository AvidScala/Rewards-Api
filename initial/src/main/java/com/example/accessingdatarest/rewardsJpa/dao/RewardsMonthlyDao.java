package com.example.accessingdatarest.rewardsJpa.dao;

import com.example.accessingdatarest.rewardsJpa.entity.Customer;

import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxnMonthly;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface RewardsMonthlyDao extends CrudRepository<RewardsTxnMonthly, Long> {

  RewardsTxnMonthly findFirstByCustomerId(Integer customerId);
  Optional<List<RewardsTxnMonthly>> findByCustomerIdAndMonthyearBetween(Integer customerId, int fromDate, int toDate);

}