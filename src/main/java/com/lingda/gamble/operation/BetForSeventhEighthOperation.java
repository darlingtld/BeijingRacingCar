package com.lingda.gamble.operation;

import com.lingda.gamble.model.SeventhEighthBet;
import com.lingda.gamble.model.SeventhEighthRatio;
import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleBet;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.SeventhEighthBetRepository;
import com.lingda.gamble.repository.SeventhEighthRatioRepository;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.service.WinLostMailNotificationJob;
import com.lingda.gamble.util.DriverUtils;
import com.lingda.gamble.util.Store;
import com.lingda.gamble.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//幸运飞艇 七八名下注
@Component
public class BetForSeventhEighthOperation {

    private static final Logger logger = LoggerFactory.getLogger(BetForSeventhEighthOperation.class);

    private static final String PLAYGROUND = "七八名";

    @Value("${gamble.bet.money}")
    private double money;

    @Autowired
    private WinLostMailNotificationJob winLostMailNotificationJob;

    private LinkedHashMap<Integer, AtomicInteger> seventhNumberCountMap = new LinkedHashMap<>();

    private LinkedHashMap<Integer, AtomicInteger> eighthNumberCountMap = new LinkedHashMap<>();

    private final SeventhEighthBetRepository seventhEighthBetRepository;

    private final SeventhEighthRatioRepository seventhEighthRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    @Autowired
    private OperationUtils operationUtils;

    private Integer lastBetSeventhNumber;

    private Integer lastBetEighthNumber;

    @Autowired
    public BetForSeventhEighthOperation(SeventhEighthBetRepository seventhEighthBetRepository,
                                        SeventhEighthRatioRepository seventhEighthRatioRepository,
                                        LotteryResultRepository lotteryResultRepository) {
        this.seventhEighthBetRepository = seventhEighthBetRepository;
        this.seventhEighthRatioRepository = seventhEighthRatioRepository;
        this.lotteryResultRepository = lotteryResultRepository;
    }

    public boolean doBet(WebDriver driver, Integer round, boolean isPlayTime) throws InterruptedException {
        Integer chip = Config.getSeventhEighthLevelAccList().get(0);
        logger.info("[Operation - Bet] Base chip is {}", chip);
        logger.info("[Operation - Bet] Play Time is {}", isPlayTime);
        if (round == null) {
            logger.info("[Operation - Bet] 当前无法下注");
            return false;
        }
        seventhNumberCountMap.clear();
        eighthNumberCountMap.clear();
        logger.info("[Operation - Bet] Third fourth numbers to exclude is {}", Config.getSeventhEighthExcludeNumbers());

        logger.info("[Operation - Bet] Bet for 幸运飞艇 - {}", PLAYGROUND);

        logger.info("[Operation - Bet] Get fetched ratio for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round);
        SeventhEighthRatio ratio = seventhEighthRatioRepository.findByRound(round);
        if (ratio == null) {
            logger.info("[Operation - Bet] No ratio information for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round);
            return false;
        }

        logger.info("[Operation - Bet] Get last lottery result for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round - 1);
        LotteryResult lastLotteryResult = lotteryResultRepository.findByRound(round - 1);
        if (lastLotteryResult == null) {
            logger.info("[Operation - Bet] No last lottery result for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round - 1);
            return false;
        }
//        take the last 6 lottery result into consideration
        markNumber(lastLotteryResult);

        LotteryResult lotteryResult2 = lotteryResultRepository.findByRound(round - 2);
        if (lotteryResult2 != null) {
            markNumber(lotteryResult2);
        }
        LotteryResult lotteryResult3 = lotteryResultRepository.findByRound(round - 3);
        if (lotteryResult3 != null) {
            markNumber(lotteryResult3);
        }
        LotteryResult lotteryResult4 = lotteryResultRepository.findByRound(round - 4);
        if (lotteryResult4 != null) {
            markNumber(lotteryResult4);
        }
        LotteryResult lotteryResult5 = lotteryResultRepository.findByRound(round - 5);
        if (lotteryResult5 != null) {
            markNumber(lotteryResult5);
        }
        LotteryResult lotteryResult6 = lotteryResultRepository.findByRound(round - 6);
        if (lotteryResult6 != null) {
            markNumber(lotteryResult6);
        }
        LotteryResult lotteryResult7 = lotteryResultRepository.findByRound(round - 7);
        if (lotteryResult7 != null) {
            markNumber(lotteryResult7);
        }

        logger.info("[Operation - Bet] Last 7 lottery result for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round - 1);
        for (Map.Entry<Integer, AtomicInteger> entry : seventhNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 7 lottery result 第七名 {}:{}次 for 幸运飞艇 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }
        for (Map.Entry<Integer, AtomicInteger> entry : eighthNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 7 lottery result 第八名 {}:{}次 for 幸运飞艇 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }

//      check if the bet is already done
        if (seventhEighthBetRepository.findByRound(round) != null) {
            logger.info("[Operation - Bet] Already bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round);
            return false;
        }

        logger.info("[Operation - Bet] Get last bet information for 幸运飞艇 - {}", PLAYGROUND);
        SeventhEighthBet lastBet = seventhEighthBetRepository.findByRound(round - 1);
        //            结算上次中奖情况
        logger.info("=============== 金额 (for test) ===============");
        money = calculateMoney(money, calculateLastLotteryResult(lastBet, lastLotteryResult));
        logger.info("我的余额:{}", money);
        logger.info("====================================");

        SeventhEighthBet bet = new SeventhEighthBet();
        bet.setRound(round);
        switch (Config.getSeventhEighthStrategyMode()) {
            case SMART:
                logger.info("[Operation - Bet] Bet in smart mode");
                List<Integer> stepIntegerList1 = Arrays.stream(Config.getSeventhEighthSmartSwitch().get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
                List<Integer> stepIntegerList2 = Arrays.stream(Config.getSeventhEighthSmartSwitch().get(1).split(",")).map(Integer::parseInt).collect(Collectors.toList());
                List<Integer> allNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
                if (lotteryResult2 == null) {
                    logger.info("[Operation - Bet] Cannot find lottery result for 2 consecutive round");
                    return false;
                }
                if (Config.getSeventhEighthSmartDetectRoundNumber() == 3) {
                    if (lotteryResult2 == null || lotteryResult3 == null) {
                        logger.info("[Operation - Bet] Cannot find lottery result for 3 consecutive round");
                        return false;
                    }
                }
                if (Config.getSeventhEighthSmartDetectRoundNumber() == 4) {
                    if (lotteryResult2 == null || lotteryResult3 == null || lotteryResult4 == null) {
                        logger.info("[Operation - Bet] Cannot find lottery result for 4 consecutive round");
                        return false;
                    }
                }
                if (!isPlayTime) {
                    logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round);
                    return false;
                }
//            no last bet or last time is a win
                if (lastBet == null || Utils.isLastBetWin(lastLotteryResult.getSeventh(), lastBet.getBetSeventh())) {
//            First
                    if (Utils.detectStepIntegerList(Config.getSeventhEighthSmartDetectRoundNumber(), stepIntegerList1, stepIntegerList2, lastLotteryResult.getSeventh(), lotteryResult2.getSeventh(), lotteryResult3.getSeventh(), lotteryResult4.getSeventh())) {
                        logger.info("[Operation - Bet] Bingo! Bet for Seventh exclude {}", stepIntegerList2);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList2);
                        logger.info("[Operation - Bet] Bet Seventh for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForSeventh(bet, chip, numberBetList, driver);
                        money = calculateMoney(money, -7 * chip);
                        if (bet.getBetEighth() == null) {
                            betForEighth(bet, chip, Collections.emptyList(), driver);
                        }
                    } else if (Utils.detectStepIntegerList(Config.getSeventhEighthSmartDetectRoundNumber(), stepIntegerList2, stepIntegerList1, lastLotteryResult.getSeventh(), lotteryResult2.getSeventh(), lotteryResult3.getSeventh(), lotteryResult4.getSeventh())) {
                        logger.info("[Operation - Bet] Bingo! Bet for Seventh exclude {}", stepIntegerList1);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList1);
                        logger.info("[Operation - Bet] Bet Seventh for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForSeventh(bet, chip, numberBetList, driver);
                        money = calculateMoney(money, -7 * chip);
                        if (bet.getBetEighth() == null) {
                            betForEighth(bet, chip, Collections.emptyList(), driver);
                        }
                    }
                } else {
//                last bet is a loser
//                exclude 6,8,10
                    if ((stepIntegerList1.contains(1) && lastBet.getBetSeventh().getFirst() > 0)
                            || (stepIntegerList1.contains(2) && lastBet.getBetSeventh().getSecond() > 0)
                            || (stepIntegerList1.contains(3) && lastBet.getBetSeventh().getThird() > 0)
                            || (stepIntegerList1.contains(4) && lastBet.getBetSeventh().getFourth() > 0)
                            || (stepIntegerList1.contains(5) && lastBet.getBetSeventh().getFifth() > 0)
                            || (stepIntegerList1.contains(6) && lastBet.getBetSeventh().getSixth() > 0)
                            || (stepIntegerList1.contains(7) && lastBet.getBetSeventh().getSeventh() > 0)
                            || (stepIntegerList1.contains(8) && lastBet.getBetSeventh().getEighth() > 0)
                            || (stepIntegerList1.contains(9) && lastBet.getBetSeventh().getNineth() > 0)
                            || (stepIntegerList1.contains(10) && lastBet.getBetSeventh().getTenth() > 0)) {
                        logger.info("[Operation - Bet] Continue! Bet for Seventh exclude {}", stepIntegerList1);
                        Integer betChip = decideBetChip(lastLotteryResult.getSeventh(), lastBet.getBetSeventh(), isPlayTime);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList1);
                        logger.info("[Operation - Bet] Bet Seventh for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForSeventh(bet, betChip, numberBetList, driver);
                        money = calculateMoney(money, -7 * betChip);
                        if (bet.getBetEighth() == null) {
                            betForEighth(bet, chip, Collections.emptyList(), driver);
                        }
                    }
                    //                exclude 1,3,5
                    else if ((stepIntegerList2.contains(1) && lastBet.getBetSeventh().getFirst() > 0)
                            || (stepIntegerList2.contains(2) && lastBet.getBetSeventh().getSecond() > 0)
                            || (stepIntegerList2.contains(3) && lastBet.getBetSeventh().getThird() > 0)
                            || (stepIntegerList2.contains(4) && lastBet.getBetSeventh().getFourth() > 0)
                            || (stepIntegerList2.contains(5) && lastBet.getBetSeventh().getFifth() > 0)
                            || (stepIntegerList2.contains(6) && lastBet.getBetSeventh().getSixth() > 0)
                            || (stepIntegerList2.contains(7) && lastBet.getBetSeventh().getSeventh() > 0)
                            || (stepIntegerList2.contains(8) && lastBet.getBetSeventh().getEighth() > 0)
                            || (stepIntegerList2.contains(9) && lastBet.getBetSeventh().getNineth() > 0)
                            || (stepIntegerList2.contains(10) && lastBet.getBetSeventh().getTenth() > 0)) {
                        logger.info("[Operation - Bet] Continue! Bet for Seventh exclude {}", stepIntegerList2);
                        Integer betChip = decideBetChip(lastLotteryResult.getSeventh(), lastBet.getBetSeventh(), isPlayTime);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList2);
                        logger.info("[Operation - Bet] Bet Seventh for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForSeventh(bet, betChip, numberBetList, driver);
                        money = calculateMoney(money, -7 * betChip);
                        if (bet.getBetEighth() == null) {
                            betForEighth(bet, chip, Collections.emptyList(), driver);
                        }
                    }
                }
                //            no last bet or last time is a win
                if (lastBet == null || Utils.isLastBetWin(lastLotteryResult.getEighth(), lastBet.getBetEighth())) {
//            Eighth
                    if (Utils.detectStepIntegerList(Config.getSeventhEighthSmartDetectRoundNumber(), stepIntegerList1, stepIntegerList2, lastLotteryResult.getEighth(), lotteryResult2.getEighth(), lotteryResult3.getEighth(), lotteryResult4.getEighth())) {
                        logger.info("[Operation - Bet] Bingo! Bet for Eighth exclude {}", stepIntegerList2);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList2);
                        logger.info("[Operation - Bet] Bet Eighth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForEighth(bet, chip, numberBetList, driver);
                        money = calculateMoney(money, -7 * chip);
                        if (bet.getBetSeventh() == null) {
                            betForSeventh(bet, chip, Collections.emptyList(), driver);
                        }
                    } else if (Utils.detectStepIntegerList(Config.getSeventhEighthSmartDetectRoundNumber(), stepIntegerList2, stepIntegerList1, lastLotteryResult.getEighth(), lotteryResult2.getEighth(), lotteryResult3.getEighth(), lotteryResult4.getEighth())) {
                        logger.info("[Operation - Bet] Bingo! Bet for Eighth exclude {}", stepIntegerList1);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList1);
                        logger.info("[Operation - Bet] Bet Eighth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForEighth(bet, chip, numberBetList, driver);
                        money = calculateMoney(money, -7 * chip);
                        if (bet.getBetSeventh() == null) {
                            betForSeventh(bet, chip, Collections.emptyList(), driver);
                        }
                    }
                } else {
//                last bet is a loser
//                exclude 6,8,10
                    if ((stepIntegerList1.contains(1) && lastBet.getBetEighth().getFirst() > 0)
                            || (stepIntegerList1.contains(2) && lastBet.getBetEighth().getSecond() > 0)
                            || (stepIntegerList1.contains(3) && lastBet.getBetEighth().getThird() > 0)
                            || (stepIntegerList1.contains(4) && lastBet.getBetEighth().getFourth() > 0)
                            || (stepIntegerList1.contains(5) && lastBet.getBetEighth().getFifth() > 0)
                            || (stepIntegerList1.contains(6) && lastBet.getBetEighth().getSixth() > 0)
                            || (stepIntegerList1.contains(7) && lastBet.getBetEighth().getSeventh() > 0)
                            || (stepIntegerList1.contains(8) && lastBet.getBetEighth().getEighth() > 0)
                            || (stepIntegerList1.contains(9) && lastBet.getBetEighth().getNineth() > 0)
                            || (stepIntegerList1.contains(10) && lastBet.getBetEighth().getTenth() > 0)) {
                        logger.info("[Operation - Bet] Continue! Bet for Eighth exclude {}", stepIntegerList1);
                        Integer betChip = decideBetChip(lastLotteryResult.getEighth(), lastBet.getBetEighth(), isPlayTime);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList1);
                        logger.info("[Operation - Bet] Bet Eighth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForEighth(bet, betChip, numberBetList, driver);
                        money = calculateMoney(money, -7 * betChip);
                        if (bet.getBetSeventh() == null) {
                            betForSeventh(bet, chip, Collections.emptyList(), driver);
                        }
                    } else if ((stepIntegerList2.contains(1) && lastBet.getBetEighth().getFirst() > 0)
                            || (stepIntegerList2.contains(2) && lastBet.getBetEighth().getSecond() > 0)
                            || (stepIntegerList2.contains(3) && lastBet.getBetEighth().getThird() > 0)
                            || (stepIntegerList2.contains(4) && lastBet.getBetEighth().getFourth() > 0)
                            || (stepIntegerList2.contains(5) && lastBet.getBetEighth().getFifth() > 0)
                            || (stepIntegerList2.contains(6) && lastBet.getBetEighth().getSixth() > 0)
                            || (stepIntegerList2.contains(7) && lastBet.getBetEighth().getSeventh() > 0)
                            || (stepIntegerList2.contains(8) && lastBet.getBetEighth().getEighth() > 0)
                            || (stepIntegerList2.contains(9) && lastBet.getBetEighth().getNineth() > 0)
                            || (stepIntegerList2.contains(10) && lastBet.getBetEighth().getTenth() > 0)) {
                        logger.info("[Operation - Bet] Continue! Bet for Eighth exclude {}", stepIntegerList2);
                        Integer betChip = decideBetChip(lastLotteryResult.getEighth(), lastBet.getBetEighth(), isPlayTime);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList2);
                        logger.info("[Operation - Bet] Bet Eighth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForEighth(bet, betChip, numberBetList, driver);
                        money = calculateMoney(money, -7 * betChip);
                        if (bet.getBetSeventh() == null) {
                            betForSeventh(bet, chip, Collections.emptyList(), driver);
                        }
                    }
                }

                if (bet.getBetSeventh() == null || bet.getBetEighth() == null) {
                    return false;
                }

                break;
            case BASIC:
                List<Integer> numberBetList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
                numberBetList.removeAll(Config.getSeventhEighthExcludeNumbers());
//        ============== 策略逻辑 ==============
                if (lastBet == null) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round - 1);
                    } else {
                        logger.info("[Operation - Bet] No last bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round - 1);
                        int seventhCountOfNumbersToRemove = 3 - Config.getSeventhEighthExcludeNumbers().size();
                        int eighthCountOfNumbersToRemove = 3 - Config.getSeventhEighthExcludeNumbers().size();
                        Map<Integer, Integer> seventhNumberStatsMap = Utils.sortByValue(Utils.convertMap(seventhNumberCountMap), true);
                        Map<Integer, Integer> eighthNumberStatsMap = Utils.sortByValue(Utils.convertMap(eighthNumberCountMap), true);

                        List<Integer> seventhNumberToBetList = new ArrayList<>(numberBetList);
                        List<Integer> eighthNumberToBetList = new ArrayList<>(numberBetList);
                        List<Integer> seventhNumberToRemoveList = new ArrayList<>();
                        List<Integer> eighthNumberToRemoveList = new ArrayList<>();

                        seventhNumberStatsMap.forEach((k, v) -> {
                            if (v >= 2 && seventhCountOfNumbersToRemove > seventhNumberToRemoveList.size()) {
                                seventhNumberToRemoveList.add(k);
                            }
                        });

                        eighthNumberStatsMap.forEach((k, v) -> {
                            if (v >= 2 && eighthCountOfNumbersToRemove > eighthNumberToRemoveList.size()) {
                                eighthNumberToRemoveList.add(k);
                            }
                        });

                        seventhNumberToBetList.removeAll(seventhNumberToRemoveList);
                        eighthNumberToBetList.removeAll(eighthNumberToRemoveList);

                        Collections.shuffle(seventhNumberToBetList);
                        Collections.shuffle(eighthNumberToBetList);

                        betForSeventh(bet, chip, seventhNumberToBetList.subList(0, Math.min(seventhNumberToBetList.size(), Config.getSeventhEighthMaxBetCount())), driver);
                        money = calculateMoney(money, -Math.min(seventhNumberToBetList.size(), Config.getSeventhEighthMaxBetCount()) * chip);
                        betForEighth(bet, chip, eighthNumberToBetList.subList(0, Math.min(eighthNumberToBetList.size(), Config.getSeventhEighthMaxBetCount())), driver);
                        money = calculateMoney(money, -Math.min(eighthNumberToBetList.size(), Config.getSeventhEighthMaxBetCount()) * chip);

                    }
                } else {
                    int seventhCountOfNumbersToRemove = 3 - Config.getSeventhEighthExcludeNumbers().size();
                    int eighthCountOfNumbersToRemove = 3 - Config.getSeventhEighthExcludeNumbers().size();
                    Map<Integer, Integer> seventhNumberStatsMap = Utils.sortByValue(Utils.convertMap(seventhNumberCountMap), true);
                    Map<Integer, Integer> eighthNumberStatsMap = Utils.sortByValue(Utils.convertMap(eighthNumberCountMap), true);

                    List<Integer> seventhNumberToBetList = new ArrayList<>(numberBetList);
                    List<Integer> eighthNumberToBetList = new ArrayList<>(numberBetList);
                    List<Integer> seventhNumberToRemoveList = new ArrayList<>();
                    List<Integer> eighthNumberToRemoveList = new ArrayList<>();

                    seventhNumberStatsMap.forEach((k, v) -> {
                        if (v >= 2 && seventhCountOfNumbersToRemove > seventhNumberToRemoveList.size()) {
                            seventhNumberToRemoveList.add(k);
                        }
                    });

                    eighthNumberStatsMap.forEach((k, v) -> {
                        if (v >= 2 && eighthCountOfNumbersToRemove > eighthNumberToRemoveList.size()) {
                            eighthNumberToRemoveList.add(k);
                        }
                    });

                    seventhNumberToBetList.removeAll(seventhNumberToRemoveList);
                    eighthNumberToBetList.removeAll(eighthNumberToRemoveList);

                    Collections.shuffle(seventhNumberToBetList);
                    Collections.shuffle(eighthNumberToBetList);

                    Integer seventhMoneyBet = decideBetChip(lastLotteryResult.getSeventh(), lastBet.getBetSeventh(), isPlayTime);
                    betForSeventh(bet, seventhMoneyBet, seventhNumberToBetList.subList(0, Math.min(seventhNumberToBetList.size(), Config.getSeventhEighthMaxBetCount())), driver);
                    money = calculateMoney(money, -Math.min(seventhNumberToBetList.size(), Config.getSeventhEighthMaxBetCount()) * seventhMoneyBet);
                    Integer eighthMoneyBet = decideBetChip(lastLotteryResult.getEighth(), lastBet.getBetEighth(), isPlayTime);
                    betForEighth(bet, eighthMoneyBet, eighthNumberToBetList.subList(0, Math.min(eighthNumberToBetList.size(), Config.getSeventhEighthMaxBetCount())), driver);
                    money = calculateMoney(money, -Math.min(eighthNumberToBetList.size(), Config.getSeventhEighthMaxBetCount()) * eighthMoneyBet);

                }
                break;
            case PAIR:
                logger.info("[Operation - Bet] Bet in PAIR mode");
                if (lastBet == null) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round);
                    } else {
                        logger.info("[Operation - Bet] No last bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round - 1);
                        Map<String, Integer> numbersToBet = operationUtils.getConsecutivePairOccursWithinANumberOfRounds(round, Config.getSeventhEighthPairModeDetectRoundNumber(), Config.getSeventhEighthGapRoundsForConsecutiveNumbers());
                        logger.info("[Operation - Bet] bet {}", numbersToBet);
                        if (numbersToBet.containsValue(lastLotteryResult.getSeventh())) {
                            betForSeventh(bet, chip, Collections.singletonList(lastLotteryResult.getSeventh()), driver);
                            lastBetSeventhNumber = lastLotteryResult.getSeventh();
                            money = calculateMoney(money, -Math.min(1, Config.getSeventhEighthMaxBetCount()) * chip);
                        }
                        if (numbersToBet.containsValue(lastLotteryResult.getEighth())) {
                            betForEighth(bet, chip, Collections.singletonList(lastLotteryResult.getEighth()), driver);
                            lastBetEighthNumber = lastLotteryResult.getEighth();
                            money = calculateMoney(money, -Math.min(1, Config.getSeventhEighthMaxBetCount()) * chip);
                        }
                    }
                } else {
//                    check if last bet is a win
                    if (lastLotteryResult.getSeventh().equals(lastBetSeventhNumber)) {
//                        bingo. It's a win
                        logger.info("Last bet for seventh is a win. Last bet seventh is {}", lastBetSeventhNumber);
                        lastBetSeventhNumber = null;
                    } else {
                        logger.info("No luck. Continue to bet {} for seventh", lastBetSeventhNumber);
                        Integer seventhMoneyBetChip = decideBetChip(lastLotteryResult.getSeventh(), lastBet.getBetSeventh(), isPlayTime);
                        betForSeventh(bet, seventhMoneyBetChip, Collections.singletonList(lastBetSeventhNumber), driver);
                        money = calculateMoney(money, -Math.min(1, Config.getSeventhEighthMaxBetCount()) * seventhMoneyBetChip);
                    }
                    if (lastLotteryResult.getEighth().equals(lastBetEighthNumber)) {
                        logger.info("Last bet for eighth is a win. Last bet eighth is {}", lastBetEighthNumber);
                        lastBetEighthNumber = null;
                    } else {
                        logger.info("No luck. Continue to bet {} for eighth", lastBetEighthNumber);
                        Integer eighthMoneyBetChip = decideBetChip(lastLotteryResult.getEighth(), lastBet.getBetEighth(), isPlayTime);
                        betForEighth(bet, eighthMoneyBetChip, Collections.singletonList(lastBetEighthNumber), driver);
                        money = calculateMoney(money, -Math.min(1, Config.getSeventhEighthMaxBetCount()) * eighthMoneyBetChip);
                    }
                }
                if (bet.getBetSeventh() == null || bet.getBetEighth() == null) {
                    return false;
                }
                break;
        }
        logger.info("=============== 金额 (for test) ===============");
        logger.info("我的余额:{}", money);
        logger.info("====================================");
        seventhEighthBetRepository.save(bet);
        return true;

    }

    private Integer decideBetChip(Integer winningNumber, RankSingleBet lastRankSingleBet, boolean isPlayTime) {
        Integer chip = Config.getSeventhEighthLevelAccList().get(0);
        double betChip = Stream.of(
                lastRankSingleBet.getFirst(),
                lastRankSingleBet.getSecond(),
                lastRankSingleBet.getThird(),
                lastRankSingleBet.getFourth(),
                lastRankSingleBet.getFifth(),
                lastRankSingleBet.getSixth(),
                lastRankSingleBet.getSeventh(),
                lastRankSingleBet.getEighth(),
                lastRankSingleBet.getNineth(),
                lastRankSingleBet.getTenth()).max(Double::compare).get();

        if (lastRankSingleBet.getFirst() > 0 && winningNumber == 1) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getSecond() > 0 && winningNumber == 2) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getThird() > 0 && winningNumber == 3) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getFourth() > 0 && winningNumber == 4) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getFifth() > 0 && winningNumber == 5) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getSixth() > 0 && winningNumber == 6) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getSeventh() > 0 && winningNumber == 7) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getEighth() > 0 && winningNumber == 8) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getNineth() > 0 && winningNumber == 9) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getTenth() > 0 && winningNumber == 10) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else {
            for (int i = 0; i < Config.getSeventhEighthLevelAccList().size(); i++) {
                if (Config.getSeventhEighthLevelAccList().get(i) > betChip) {
                    if (i >= 4) {
                        String message = String.format("%s - %s 第%s关 当前每一注为 %s", Store.getAccountName(), PLAYGROUND, i, Config.getSeventhEighthLevelAccList().get(i));
                        winLostMailNotificationJob.sendDangerousNotificationJobs(message);
                    }
                    return Config.getSeventhEighthLevelAccList().get(i);
                }
            }
            return chip;
        }
    }

    private double calculateLastLotteryResult(SeventhEighthBet lastBet, LotteryResult lotteryResult) {
        if (lastBet == null) {
            return 0;
        }
        double winningMoney = 0;
        SeventhEighthRatio lastRatio = seventhEighthRatioRepository.findByRound(lastBet.getRound());
        if (lotteryResult.getFifth() > 5) {
            winningMoney += lastBet.getBetSeventh().getDa() * lastRatio.getRatioSeventh().getDa();
        } else {
            winningMoney += lastBet.getBetSeventh().getXiao() * lastRatio.getRatioSeventh().getXiao();
        }

        if (lotteryResult.getSixth() > 5) {
            winningMoney += lastBet.getBetEighth().getDa() * lastRatio.getRatioEighth().getDa();
        } else {
            winningMoney += lastBet.getBetEighth().getXiao() * lastRatio.getRatioEighth().getXiao();
        }

        if (lotteryResult.getFifth() == 1) {
            winningMoney += lastBet.getBetSeventh().getFirst() * lastRatio.getRatioSeventh().getFirst();
        } else if (lotteryResult.getThird() == 2) {
            winningMoney += lastBet.getBetSeventh().getSecond() * lastRatio.getRatioSeventh().getSecond();
        } else if (lotteryResult.getThird() == 3) {
            winningMoney += lastBet.getBetSeventh().getThird() * lastRatio.getRatioSeventh().getThird();
        } else if (lotteryResult.getThird() == 4) {
            winningMoney += lastBet.getBetSeventh().getFourth() * lastRatio.getRatioSeventh().getFourth();
        } else if (lotteryResult.getThird() == 5) {
            winningMoney += lastBet.getBetSeventh().getFifth() * lastRatio.getRatioSeventh().getFifth();
        } else if (lotteryResult.getThird() == 6) {
            winningMoney += lastBet.getBetSeventh().getSixth() * lastRatio.getRatioSeventh().getSixth();
        } else if (lotteryResult.getThird() == 7) {
            winningMoney += lastBet.getBetSeventh().getSeventh() * lastRatio.getRatioSeventh().getSeventh();
        } else if (lotteryResult.getThird() == 8) {
            winningMoney += lastBet.getBetSeventh().getEighth() * lastRatio.getRatioSeventh().getEighth();
        } else if (lotteryResult.getThird() == 9) {
            winningMoney += lastBet.getBetSeventh().getNineth() * lastRatio.getRatioSeventh().getNineth();
        } else if (lotteryResult.getThird() == 10) {
            winningMoney += lastBet.getBetSeventh().getTenth() * lastRatio.getRatioSeventh().getTenth();
        }

        if (lotteryResult.getSixth() == 1) {
            winningMoney += lastBet.getBetEighth().getFirst() * lastRatio.getRatioEighth().getFirst();
        } else if (lotteryResult.getFourth() == 2) {
            winningMoney += lastBet.getBetEighth().getSecond() * lastRatio.getRatioEighth().getSecond();
        } else if (lotteryResult.getFourth() == 3) {
            winningMoney += lastBet.getBetEighth().getThird() * lastRatio.getRatioEighth().getThird();
        } else if (lotteryResult.getFourth() == 4) {
            winningMoney += lastBet.getBetEighth().getFourth() * lastRatio.getRatioEighth().getFourth();
        } else if (lotteryResult.getFourth() == 5) {
            winningMoney += lastBet.getBetEighth().getFifth() * lastRatio.getRatioEighth().getFifth();
        } else if (lotteryResult.getFourth() == 6) {
            winningMoney += lastBet.getBetEighth().getSixth() * lastRatio.getRatioEighth().getSixth();
        } else if (lotteryResult.getFourth() == 7) {
            winningMoney += lastBet.getBetEighth().getSeventh() * lastRatio.getRatioEighth().getSeventh();
        } else if (lotteryResult.getFourth() == 8) {
            winningMoney += lastBet.getBetEighth().getEighth() * lastRatio.getRatioEighth().getEighth();
        } else if (lotteryResult.getFourth() == 9) {
            winningMoney += lastBet.getBetEighth().getNineth() * lastRatio.getRatioEighth().getNineth();
        } else if (lotteryResult.getFourth() == 10) {
            winningMoney += lastBet.getBetEighth().getTenth() * lastRatio.getRatioEighth().getTenth();
        }

        logger.info("[Operation - Summary] Winning money for round {} is {}", lastBet.getRound(), winningMoney);

        return winningMoney;
    }

    private void markNumber(LotteryResult lotteryResult) {
        if (seventhNumberCountMap.containsKey(lotteryResult.getSeventh())) {
            seventhNumberCountMap.get(lotteryResult.getSeventh()).getAndIncrement();
        } else {
            seventhNumberCountMap.put(lotteryResult.getSeventh(), new AtomicInteger(1));
        }
        if (eighthNumberCountMap.containsKey(lotteryResult.getEighth())) {
            eighthNumberCountMap.get(lotteryResult.getEighth()).getAndIncrement();
        } else {
            eighthNumberCountMap.put(lotteryResult.getEighth(), new AtomicInteger(1));
        }
    }

    private double calculateMoney(double money, double value) {
        return money + value;
    }

    private void betForSeventh(SeventhEighthBet bet, Integer chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 幸运飞艇 - {} - 期数 {} - 金额 - {}", "第七名", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetSeventh(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetSeventh();
        if (singleBet.getFirst() > 0) {
            String dataId = "B7_1";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String dataId = "B7_2";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String dataId = "B7_3";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String dataId = "B7_4";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String dataId = "B7_5";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String dataId = "B7_6";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String dataId = "B7_7";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String dataId = "B7_8";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String dataId = "B7_9";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String dataId = "B7_10";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
    }

    private void betForEighth(SeventhEighthBet bet, Integer chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 幸运飞艇 - {} - 期数 {} - 金额 - {}", "第八名", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetEighth(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetEighth();
        if (singleBet.getFirst() > 0) {
            String dataId = "B8_1";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String dataId = "B8_2";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String dataId = "B8_3";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String dataId = "B8_4";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String dataId = "B8_5";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String dataId = "B8_6";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String dataId = "B8_7";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String dataId = "B8_8";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String dataId = "B8_9";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String dataId = "B8_10";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
    }

    private RankSingleBet generateSingleBet(List<Integer> numberList, Integer chip) {
        RankSingleBet rankSingleBet = new RankSingleBet();
        for (Integer number : numberList) {
            switch (number) {
                case 1:
                    rankSingleBet.setFirst(chip);
                    break;
                case 2:
                    rankSingleBet.setSecond(chip);
                    break;
                case 3:
                    rankSingleBet.setThird(chip);
                    break;
                case 4:
                    rankSingleBet.setFourth(chip);
                    break;
                case 5:
                    rankSingleBet.setFifth(chip);
                    break;
                case 6:
                    rankSingleBet.setSixth(chip);
                    break;
                case 7:
                    rankSingleBet.setSeventh(chip);
                    break;
                case 8:
                    rankSingleBet.setEighth(chip);
                    break;
                case 9:
                    rankSingleBet.setNineth(chip);
                    break;
                case 10:
                    rankSingleBet.setTenth(chip);
                    break;
            }
        }
        return rankSingleBet;
    }

    private void sendKeys(WebDriver driver, String dataId, String chip) {
        DriverUtils.returnOnFindingElementEqualsDataId(driver, By.tagName("input"), dataId).sendKeys(chip);
    }
}
