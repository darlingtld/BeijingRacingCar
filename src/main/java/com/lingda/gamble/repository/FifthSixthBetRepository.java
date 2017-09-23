package com.lingda.gamble.repository;

import com.lingda.gamble.model.FifthSixthBet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FifthSixthBetRepository extends MongoRepository<FifthSixthBet, String> {

    FifthSixthBet findByRound(Integer round);
}
