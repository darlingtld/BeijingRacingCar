package com.lingda.gamble.repository;

import com.lingda.gamble.model.NinethTenthBet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NinethTenthBetRepository extends MongoRepository<NinethTenthBet, String> {

    NinethTenthBet findByRound(Integer round);
}
