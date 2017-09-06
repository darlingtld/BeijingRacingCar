package com.lingda.gamble.repository;

import com.lingda.gamble.model.SMPBet;
import com.lingda.gamble.model.SMPRatio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SMPBetRepository extends MongoRepository<SMPBet, String> {

    SMPBet findByRound(Integer round);
}
