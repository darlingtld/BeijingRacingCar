package com.lingda.gamble.repository;

import com.lingda.gamble.model.ThirdFourthBet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThirdFourthBetRepository extends MongoRepository<ThirdFourthBet, String> {

    ThirdFourthBet findByRound(Integer round);
}
