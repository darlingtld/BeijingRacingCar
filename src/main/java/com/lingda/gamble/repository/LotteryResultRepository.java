package com.lingda.gamble.repository;

import com.lingda.gamble.model.LotteryResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LotteryResultRepository extends MongoRepository<LotteryResult, String> {

    LotteryResult findByRound(Integer round);
}
