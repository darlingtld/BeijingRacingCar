package com.lingda.gamble.repository;

import com.lingda.gamble.model.NinethTenthRatio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NinethTenthRatioRepository extends MongoRepository<NinethTenthRatio, String> {

    NinethTenthRatio findByRound(Integer round);
}
