package com.lingda.gamble.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "win_lost_money")
public class WinLostMoney {
    @Id
    private String id;
    private Integer round;
    private String accountName;
    private Double winLostMoney;

    @Override
    public String toString() {
        return "WinLostMoney{" +
                "id='" + id + '\'' +
                ", round=" + round +
                ", accountName='" + accountName + '\'' +
                ", winLostMoney=" + winLostMoney +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Double getWinLostMoney() {
        return winLostMoney;
    }

    public void setWinLostMoney(Double winLostMoney) {
        this.winLostMoney = winLostMoney;
    }
}
