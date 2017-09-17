package com.lingda.gamble.repository;

import com.lingda.gamble.model.WinLostMoney;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WinLostMoneyRepository extends MongoRepository<WinLostMoney, String> {

    WinLostMoney findByRound(Integer round);
    WinLostMoney findByRoundAndAccountName(Integer round, String accountName);
    WinLostMoney findFirstByAccountNameOrderByRoundDesc(String accountName);
}
