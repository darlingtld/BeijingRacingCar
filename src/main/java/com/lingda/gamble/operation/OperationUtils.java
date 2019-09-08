package com.lingda.gamble.operation;

import com.lingda.gamble.model.Constant;
import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.repository.LotteryResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OperationUtils {

    private static final Logger logger = LoggerFactory.getLogger(OperationUtils.class);

    @Autowired
    private LotteryResultRepository lotteryResultRepository;

    public Map<String, Integer> getConsecutivePairOccursWithinANumberOfRounds(int currentRound, int detectRounds, int gapRoundsForConsecutiveNumbers) {
        List<LotteryResult> lotteryResultList = new ArrayList<>(detectRounds);
        for (int i = 1; i <= detectRounds; i++) {
            LotteryResult lotteryResult = lotteryResultRepository.findByRound(currentRound - i);
            logger.info("之前的开奖结果:{}", lotteryResult);
            if (lotteryResult == null) {
                logger.warn("没有足够的指定{}轮开奖信息", detectRounds);
            }
            lotteryResultList.add(lotteryResult);
        }
        Map<String, Integer> consecutiveRoundsForSameNumber = new HashMap<>();

        for (int i = 0; i < detectRounds - 1; i++) {
            if (lotteryResultList.get(i).getFirst().equals(lotteryResultList.get(i + 1).getFirst()) && i >= gapRoundsForConsecutiveNumbers) {
                if (consecutiveRoundsForSameNumber.containsKey(Constant.FIRST)) {
                    consecutiveRoundsForSameNumber.remove(Constant.FIRST);
                } else {
                    consecutiveRoundsForSameNumber.put(Constant.FIRST, lotteryResultList.get(i).getFirst());
                }
            }
            if (lotteryResultList.get(i).getSecond().equals(lotteryResultList.get(i + 1).getSecond()) && i >= gapRoundsForConsecutiveNumbers) {
                if (consecutiveRoundsForSameNumber.containsKey(Constant.SECOND)) {
                    consecutiveRoundsForSameNumber.remove(Constant.SECOND);
                } else {
                    consecutiveRoundsForSameNumber.put(Constant.SECOND, lotteryResultList.get(i).getSecond());
                }
            }
            if (lotteryResultList.get(i).getThird().equals(lotteryResultList.get(i + 1).getThird()) && i >= gapRoundsForConsecutiveNumbers) {
                if (consecutiveRoundsForSameNumber.containsKey(Constant.THIRD)) {
                    consecutiveRoundsForSameNumber.remove(Constant.THIRD);
                } else {
                    consecutiveRoundsForSameNumber.put(Constant.THIRD, lotteryResultList.get(i).getFirst());
                }
            }
            if (lotteryResultList.get(i).getFourth().equals(lotteryResultList.get(i + 1).getFourth()) && i >= gapRoundsForConsecutiveNumbers) {
                if (consecutiveRoundsForSameNumber.containsKey(Constant.FOURTH)) {
                    consecutiveRoundsForSameNumber.remove(Constant.FOURTH);
                } else {
                    consecutiveRoundsForSameNumber.put(Constant.FOURTH, lotteryResultList.get(i).getFourth());
                }
            }
            if (lotteryResultList.get(i).getFifth().equals(lotteryResultList.get(i + 1).getFifth()) && i >= gapRoundsForConsecutiveNumbers) {
                if (consecutiveRoundsForSameNumber.containsKey(Constant.FIFTH)) {
                    consecutiveRoundsForSameNumber.remove(Constant.FIFTH);
                } else {
                    consecutiveRoundsForSameNumber.put(Constant.FIFTH, lotteryResultList.get(i).getFifth());
                }
            }
            if (lotteryResultList.get(i).getSixth().equals(lotteryResultList.get(i + 1).getSixth()) && i >= gapRoundsForConsecutiveNumbers) {
                if (consecutiveRoundsForSameNumber.containsKey(Constant.SIXTH)) {
                    consecutiveRoundsForSameNumber.remove(Constant.SIXTH);
                } else {
                    consecutiveRoundsForSameNumber.put(Constant.SIXTH, lotteryResultList.get(i).getSixth());
                }
            }
            if (lotteryResultList.get(i).getSeventh().equals(lotteryResultList.get(i + 1).getSeventh()) && i >= gapRoundsForConsecutiveNumbers) {
                if (consecutiveRoundsForSameNumber.containsKey(Constant.SEVENTH)) {
                    consecutiveRoundsForSameNumber.remove(Constant.SEVENTH);
                } else {
                    consecutiveRoundsForSameNumber.put(Constant.SEVENTH, lotteryResultList.get(i).getSeventh());
                }
            }
            if (lotteryResultList.get(i).getEighth().equals(lotteryResultList.get(i + 1).getEighth()) && i >= gapRoundsForConsecutiveNumbers) {
                if (consecutiveRoundsForSameNumber.containsKey(Constant.EIGHTH)) {
                    consecutiveRoundsForSameNumber.remove(Constant.EIGHTH);
                } else {
                    consecutiveRoundsForSameNumber.put(Constant.EIGHTH, lotteryResultList.get(i).getEighth());
                }
            }
            if (lotteryResultList.get(i).getNineth().equals(lotteryResultList.get(i + 1).getNineth()) && i >= gapRoundsForConsecutiveNumbers) {
                if (consecutiveRoundsForSameNumber.containsKey(Constant.NINETH)) {
                    consecutiveRoundsForSameNumber.remove(Constant.NINETH);
                } else {
                    consecutiveRoundsForSameNumber.put(Constant.NINETH, lotteryResultList.get(i).getNineth());
                }
            }
            if (lotteryResultList.get(i).getTenth().equals(lotteryResultList.get(i + 1).getTenth()) && i >= gapRoundsForConsecutiveNumbers) {
                if (consecutiveRoundsForSameNumber.containsKey(Constant.TENTH)) {
                    consecutiveRoundsForSameNumber.remove(Constant.TENTH);
                } else {
                    consecutiveRoundsForSameNumber.put(Constant.TENTH, lotteryResultList.get(i).getTenth());
                }
            }
        }

        return consecutiveRoundsForSameNumber;
    }
}
