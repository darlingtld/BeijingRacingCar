package com.lingda.gamble.repository;

import com.lingda.gamble.model.SeventhEighthRatio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeventhEighthRatioRepository extends MongoRepository<SeventhEighthRatio, String> {

    SeventhEighthRatio findByRound(Integer round);
}
