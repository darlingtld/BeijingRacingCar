package com.lingda.gamble.repository;

import com.lingda.gamble.model.SeventhEighthBet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeventhEighthBetRepository extends MongoRepository<SeventhEighthBet, String> {

    SeventhEighthBet findByRound(Integer round);
}
