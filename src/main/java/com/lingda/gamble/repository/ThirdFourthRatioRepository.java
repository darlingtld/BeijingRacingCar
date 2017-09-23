package com.lingda.gamble.repository;

import com.lingda.gamble.model.ThirdFourthRatio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ThirdFourthRatioRepository extends MongoRepository<ThirdFourthRatio, String> {

    ThirdFourthRatio findByRound(Integer round);
}
