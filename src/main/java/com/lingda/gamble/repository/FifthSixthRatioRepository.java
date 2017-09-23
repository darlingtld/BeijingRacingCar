package com.lingda.gamble.repository;

import com.lingda.gamble.model.FifthSixthRatio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FifthSixthRatioRepository extends MongoRepository<FifthSixthRatio, String> {

    FifthSixthRatio findByRound(Integer round);
}
