package com.example.accessingdatarest.rewardsJpa.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.util.Date;

@Entity
@Data
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "rewards")
public class RewardsTxn {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer txn_id;

  @Basic(optional = false)
  @Column(name = "createdOn", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdOn;

  @Digits(integer=5,fraction=0)
  @Column(name = "txn_amount")
  private Integer transactionAmount;

  @Column(name = "rewardPoints")
  private Integer rewardPoints;

  @Column(name = "Points_to_date")
  private Integer pointsTodate;

  @Column(name = "redeem_points")
  private Integer redeemedPoints;

  @Column(name = "customerId")
  private Integer customerId;


    public RewardsTxn(Integer transactionAmount, Integer rewardPoints, Integer pointsTodate) {
    this.transactionAmount = transactionAmount;
    this.rewardPoints = rewardPoints;
    this.pointsTodate = pointsTodate;
    this.createdOn = new Date();
  }

}