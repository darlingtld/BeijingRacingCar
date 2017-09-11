package com.lingda.gamble.repository;

import com.lingda.gamble.model.FirstSecondRatio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FirstSecondRatioRepository extends MongoRepository<FirstSecondRatio, String> {

    FirstSecondRatio findByRound(Integer round);
}
