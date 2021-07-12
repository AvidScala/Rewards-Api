package com.example.accessingdatarest.rewardsJpa.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Setter
@NoArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer customerId;

  @NotNull
  @Digits(integer = 10, fraction = 0)
  @Column(name = "Phone_Num")
  private Long phoneNumber;

  @NotBlank(message = "FirstName  is mandatory")
  private String firstName;

  private String lastName;
  private String city;

  public Customer(Long phoneNumber, String firstName, String lastName, String city) {
    this.phoneNumber = phoneNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.city = city;
  }

}