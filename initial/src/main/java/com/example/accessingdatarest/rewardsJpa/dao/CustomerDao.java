package com.example.accessingdatarest.rewardsJpa.dao;

import java.util.List;
import java.util.Optional;
import com.example.accessingdatarest.rewardsJpa.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CustomerDao extends CrudRepository<Customer, Long> {
  Optional<Customer> findByCustomerId(Integer id);
  Optional<Customer> findByPhoneNumber(Long phoneNumber);
  List<Customer> findAll();
}