package com.lingda.gamble.repository;

import com.lingda.gamble.model.FirstSecondBet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FirstSecondBetRepository extends MongoRepository<FirstSecondBet, String> {

    FirstSecondBet findByRound(Integer round);
}
