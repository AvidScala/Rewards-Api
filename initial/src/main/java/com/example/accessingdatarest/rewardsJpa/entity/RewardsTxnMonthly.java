package com.example.accessingdatarest.rewardsJpa.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Table;;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
@Setter
@Table(name = "MonthlyRewards")
public class RewardsTxnMonthly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer monthlyTxnId;


    @Column(name = "MonthYear")
    private Integer monthyear;

    @Column(name = "TotalPoints")
    private Integer pointsPerMonth;

    @Column(name = "customerId")
    private Integer customerId;

    @Column(name = "createdOn", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date createdOn;

    public RewardsTxnMonthly(Integer monthyear, Integer totalPoints) {
        this.monthyear = monthyear;
        this.pointsPerMonth = totalPoints;
        this.createdOn = new Date();
    }

}
