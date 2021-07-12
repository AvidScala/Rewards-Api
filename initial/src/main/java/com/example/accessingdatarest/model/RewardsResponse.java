package com.example.accessingdatarest.model;

import com.example.accessingdatarest.rewardsJpa.entity.RewardsTxnMonthly;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;


import java.util.List;

@Data
public class RewardsResponse {
    private List<RewardsTxnMonthly> rewardsTxnMonthlyList;
    private Integer totalPoints;

    public RewardsResponse(int totalPoints, List<RewardsTxnMonthly> rewards) {
        this.rewardsTxnMonthlyList = rewards;
        this.totalPoints = totalPoints;

    }
}
