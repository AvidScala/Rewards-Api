package com.example.accessingdatarest.rewardsJpa.dao;

import com.example.accessingdatarest.rewardsJpa.entity.Customer;
import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxn;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RewardsTxnDao extends CrudRepository<RewardsTxn, Long> {

  Optional<RewardsTxn> findFirstByCustomerIdOrderByCreatedOnDesc(int CustomerId);

}