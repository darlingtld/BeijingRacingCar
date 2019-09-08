package com.lingda.gamble.operation;

import com.lingda.gamble.model.FirstSecondBet;
import com.lingda.gamble.model.FirstSecondRatio;
import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleBet;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.FirstSecondBetRepository;
import com.lingda.gamble.repository.FirstSecondRatioRepository;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//幸运飞艇 冠亚军下注
@Component
public class BetForFirstSecondOperation {

    private static final Logger logger = LoggerFactory.getLogger(BetForFirstSecondOperation.class);

    private static final String PLAYGROUND = "冠亚军";

    @Value("${gamble.bet.money}")
    private double money;

    @Autowired
    private WinLostMailNotificationJob winLostMailNotificationJob;

    private LinkedHashMap<Integer, AtomicInteger> firstNumberCountMap = new LinkedHashMap<>();

    private LinkedHashMap<Integer, AtomicInteger> secondNumberCountMap = new LinkedHashMap<>();

    private final FirstSecondBetRepository firstSecondBetRepository;

    private final FirstSecondRatioRepository firstSecondRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    @Autowired
    public BetForFirstSecondOperation(FirstSecondBetRepository firstSecondBetRepository,
                                      FirstSecondRatioRepository firstSecondRatioRepository,
                                      LotteryResultRepository lotteryResultRepository) {
        this.firstSecondBetRepository = firstSecondBetRepository;
        this.firstSecondRatioRepository = firstSecondRatioRepository;
        this.lotteryResultRepository = lotteryResultRepository;
    }

    public boolean doBet(WebDriver driver, Integer round, boolean isPlayTime) throws InterruptedException {
        Integer chip = Config.getFirstSecondLevelAccList().get(0);
        logger.info("[Operation - Bet] Base chip is {}", chip);
        logger.info("[Operation - Bet] Play Time is {}", isPlayTime);
        if (round == null) {
            logger.info("[Operation - Bet] 当前无法下注");
            return false;
        }
        firstNumberCountMap.clear();
        secondNumberCountMap.clear();
        logger.info("[Operation - Bet] First second numbers to exclude is {}", Config.getFirstSecondExcludeNumbers());

        logger.info("[Operation - Bet] Bet for 幸运飞艇 - {}", PLAYGROUND);

        logger.info("[Operation - Bet] Get fetched ratio for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round);
        FirstSecondRatio ratio = firstSecondRatioRepository.findByRound(round);
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
//        take the last 7 lottery result into consideration
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
        for (Map.Entry<Integer, AtomicInteger> entry : firstNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 7 lottery result 冠军 {}:{}次 for 幸运飞艇 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }
        for (Map.Entry<Integer, AtomicInteger> entry : secondNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 7 lottery result 亚军 {}:{}次 for 幸运飞艇 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }

//      check if the bet is already done
        if (firstSecondBetRepository.findByRound(round) != null) {
            logger.info("[Operation - Bet] Already bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round);
            return false;
        }

        logger.info("[Operation - Bet] Get last bet information for 幸运飞艇 - {}", PLAYGROUND);
        FirstSecondBet lastBet = firstSecondBetRepository.findByRound(round - 1);
        //            结算上次中奖情况
        logger.info("=============== 金额 (for test) ===============");
        money = calculateMoney(money, calculateLastLotteryResult(lastBet, lastLotteryResult));
        logger.info("我的余额:{}", money);
        logger.info("====================================");

        FirstSecondBet bet = new FirstSecondBet();
        bet.setRound(round);
        switch (Config.getFirstSecondStrategyMode()) {
            case SMART:
                logger.info("[Operation - Bet] Bet in smart mode");
                List<Integer> stepIntegerList1 = Arrays.stream(Config.getFirstSecondSmartSwitch().get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
                List<Integer> stepIntegerList2 = Arrays.stream(Config.getFirstSecondSmartSwitch().get(1).split(",")).map(Integer::parseInt).collect(Collectors.toList());
                List<Integer> allNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
                if (lotteryResult2 == null) {
                    logger.info("[Operation - Bet] Cannot find lottery result for 2 consecutive round");
                    return false;
                }
                if (Config.getFirstSecondSmartDetectRoundNumber() == 3) {
                    if (lotteryResult2 == null || lotteryResult3 == null) {
                        logger.info("[Operation - Bet] Cannot find lottery result for 3 consecutive round");
                        return false;
                    }
                }
                if (Config.getFirstSecondSmartDetectRoundNumber() == 4) {
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
                if (lastBet == null || Utils.isLastBetWin(lastLotteryResult.getFirst(), lastBet.getBetFirst())) {
//            First
                    if (Utils.detectStepIntegerList(Config.getFirstSecondSmartDetectRoundNumber(), stepIntegerList1, stepIntegerList2, lastLotteryResult.getFirst(), lotteryResult2.getFirst(), lotteryResult3.getFirst(), lotteryResult4.getFirst())) {
                        logger.info("[Operation - Bet] Bingo! Bet for First exclude {}", stepIntegerList2);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList2);
                        logger.info("[Operation - Bet] Bet First for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForFirst(bet, chip, numberBetList, driver);
                        money = calculateMoney(money, -7 * chip);
                        if (bet.getBetSecond() == null) {
                            betForSecond(bet, chip, Collections.emptyList(), driver);
                        }
                    } else if (Utils.detectStepIntegerList(Config.getFirstSecondSmartDetectRoundNumber(), stepIntegerList2, stepIntegerList1, lastLotteryResult.getFirst(), lotteryResult2.getFirst(), lotteryResult3.getFirst(), lotteryResult4.getFirst())) {
                        logger.info("[Operation - Bet] Bingo! Bet for First exclude {}", stepIntegerList1);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList1);
                        logger.info("[Operation - Bet] Bet First for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForFirst(bet, chip, numberBetList, driver);
                        money = calculateMoney(money, -7 * chip);
                        if (bet.getBetSecond() == null) {
                            betForSecond(bet, chip, Collections.emptyList(), driver);
                        }
                    }
                } else {
//                last bet is a loser
//                exclude 6,8,10
                    if ((stepIntegerList1.contains(1) && lastBet.getBetFirst().getFirst() > 0)
                            || (stepIntegerList1.contains(2) && lastBet.getBetFirst().getSecond() > 0)
                            || (stepIntegerList1.contains(3) && lastBet.getBetFirst().getThird() > 0)
                            || (stepIntegerList1.contains(4) && lastBet.getBetFirst().getFourth() > 0)
                            || (stepIntegerList1.contains(5) && lastBet.getBetFirst().getFifth() > 0)
                            || (stepIntegerList1.contains(6) && lastBet.getBetFirst().getSixth() > 0)
                            || (stepIntegerList1.contains(7) && lastBet.getBetFirst().getSeventh() > 0)
                            || (stepIntegerList1.contains(8) && lastBet.getBetFirst().getEighth() > 0)
                            || (stepIntegerList1.contains(9) && lastBet.getBetFirst().getNineth() > 0)
                            || (stepIntegerList1.contains(10) && lastBet.getBetFirst().getTenth() > 0)) {
                        logger.info("[Operation - Bet] Continue! Bet for First exclude {}", stepIntegerList1);
                        Integer betChip = decideBetChip(lastLotteryResult.getFirst(), lastBet.getBetFirst(), isPlayTime);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList1);
                        logger.info("[Operation - Bet] Bet First for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForFirst(bet, betChip, numberBetList, driver);
                        money = calculateMoney(money, -7 * betChip);
                        if (bet.getBetSecond() == null) {
                            betForSecond(bet, chip, Collections.emptyList(), driver);
                        }
                    }
                    //                exclude 1,3,5
                    else if ((stepIntegerList2.contains(1) && lastBet.getBetFirst().getFirst() > 0)
                            || (stepIntegerList2.contains(2) && lastBet.getBetFirst().getSecond() > 0)
                            || (stepIntegerList2.contains(3) && lastBet.getBetFirst().getThird() > 0)
                            || (stepIntegerList2.contains(4) && lastBet.getBetFirst().getFourth() > 0)
                            || (stepIntegerList2.contains(5) && lastBet.getBetFirst().getFifth() > 0)
                            || (stepIntegerList2.contains(6) && lastBet.getBetFirst().getSixth() > 0)
                            || (stepIntegerList2.contains(7) && lastBet.getBetFirst().getSeventh() > 0)
                            || (stepIntegerList2.contains(8) && lastBet.getBetFirst().getEighth() > 0)
                            || (stepIntegerList2.contains(9) && lastBet.getBetFirst().getNineth() > 0)
                            || (stepIntegerList2.contains(10) && lastBet.getBetFirst().getTenth() > 0)) {
                        logger.info("[Operation - Bet] Continue! Bet for First exclude {}", stepIntegerList2);
                        Integer betChip = decideBetChip(lastLotteryResult.getFirst(), lastBet.getBetFirst(), isPlayTime);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList2);
                        logger.info("[Operation - Bet] Bet First for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForFirst(bet, betChip, numberBetList, driver);
                        money = calculateMoney(money, -7 * betChip);
                        if (bet.getBetSecond() == null) {
                            betForSecond(bet, chip, Collections.emptyList(), driver);
                        }
                    }
                }
                //            no last bet or last time is a win
                if (lastBet == null || Utils.isLastBetWin(lastLotteryResult.getSecond(), lastBet.getBetSecond())) {
//            Second
                    if (Utils.detectStepIntegerList(Config.getFirstSecondSmartDetectRoundNumber(), stepIntegerList1, stepIntegerList2, lastLotteryResult.getSecond(), lotteryResult2.getSecond(), lotteryResult3.getSecond(), lotteryResult4.getSecond())) {
                        logger.info("[Operation - Bet] Bingo! Bet for Second exclude {}", stepIntegerList2);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList2);
                        logger.info("[Operation - Bet] Bet Second for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForSecond(bet, chip, numberBetList, driver);
                        money = calculateMoney(money, -7 * chip);
                        if (bet.getBetFirst() == null) {
                            betForFirst(bet, chip, Collections.emptyList(), driver);
                        }
                    } else if (Utils.detectStepIntegerList(Config.getFirstSecondSmartDetectRoundNumber(), stepIntegerList2, stepIntegerList1, lastLotteryResult.getSecond(), lotteryResult2.getSecond(), lotteryResult3.getSecond(), lotteryResult4.getSecond())) {
                        logger.info("[Operation - Bet] Bingo! Bet for Second exclude {}", stepIntegerList1);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList1);
                        logger.info("[Operation - Bet] Bet Second for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForSecond(bet, chip, numberBetList, driver);
                        money = calculateMoney(money, -7 * chip);
                        if (bet.getBetFirst() == null) {
                            betForFirst(bet, chip, Collections.emptyList(), driver);
                        }
                    }
                } else {
//                last bet is a loser
//                exclude 6,8,10
                    if ((stepIntegerList1.contains(1) && lastBet.getBetSecond().getFirst() > 0)
                            || (stepIntegerList1.contains(2) && lastBet.getBetSecond().getSecond() > 0)
                            || (stepIntegerList1.contains(3) && lastBet.getBetSecond().getThird() > 0)
                            || (stepIntegerList1.contains(4) && lastBet.getBetSecond().getFourth() > 0)
                            || (stepIntegerList1.contains(5) && lastBet.getBetSecond().getFifth() > 0)
                            || (stepIntegerList1.contains(6) && lastBet.getBetSecond().getSixth() > 0)
                            || (stepIntegerList1.contains(7) && lastBet.getBetSecond().getSeventh() > 0)
                            || (stepIntegerList1.contains(8) && lastBet.getBetSecond().getEighth() > 0)
                            || (stepIntegerList1.contains(9) && lastBet.getBetSecond().getNineth() > 0)
                            || (stepIntegerList1.contains(10) && lastBet.getBetSecond().getTenth() > 0)) {
                        logger.info("[Operation - Bet] Continue! Bet for Second exclude {}", stepIntegerList1);
                        Integer betChip = decideBetChip(lastLotteryResult.getSecond(), lastBet.getBetSecond(), isPlayTime);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList1);
                        logger.info("[Operation - Bet] Bet Second for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForSecond(bet, betChip, numberBetList, driver);
                        money = calculateMoney(money, -7 * betChip);
                        if (bet.getBetFirst() == null) {
                            betForFirst(bet, chip, Collections.emptyList(), driver);
                        }
                    } else if ((stepIntegerList2.contains(1) && lastBet.getBetSecond().getFirst() > 0)
                            || (stepIntegerList2.contains(2) && lastBet.getBetSecond().getSecond() > 0)
                            || (stepIntegerList2.contains(3) && lastBet.getBetSecond().getThird() > 0)
                            || (stepIntegerList2.contains(4) && lastBet.getBetSecond().getFourth() > 0)
                            || (stepIntegerList2.contains(5) && lastBet.getBetSecond().getFifth() > 0)
                            || (stepIntegerList2.contains(6) && lastBet.getBetSecond().getSixth() > 0)
                            || (stepIntegerList2.contains(7) && lastBet.getBetSecond().getSeventh() > 0)
                            || (stepIntegerList2.contains(8) && lastBet.getBetSecond().getEighth() > 0)
                            || (stepIntegerList2.contains(9) && lastBet.getBetSecond().getNineth() > 0)
                            || (stepIntegerList2.contains(10) && lastBet.getBetSecond().getTenth() > 0)) {
                        logger.info("[Operation - Bet] Continue! Bet for Second exclude {}", stepIntegerList2);
                        Integer betChip = decideBetChip(lastLotteryResult.getSecond(), lastBet.getBetSecond(), isPlayTime);
                        List<Integer> numberBetList = new ArrayList<>(allNumbers);
                        numberBetList.removeAll(stepIntegerList2);
                        logger.info("[Operation - Bet] Bet Second for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                        betForSecond(bet, betChip, numberBetList, driver);
                        money = calculateMoney(money, -7 * betChip);
                        if (bet.getBetFirst() == null) {
                            betForFirst(bet, chip, Collections.emptyList(), driver);
                        }
                    }
                }

                if (bet.getBetFirst() == null || bet.getBetSecond() == null) {
                    return false;
                }
                break;
            case BASIC:
                logger.info("[Operation - Bet] Bet in basic mode");
                List<Integer> numberBetList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
                numberBetList.removeAll(Config.getFirstSecondExcludeNumbers());

                if (lastBet == null) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round);
                    } else {
                        logger.info("[Operation - Bet] No last bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round - 1);
                        int firstCountOfNumbersToRemove = 3 - Config.getFirstSecondExcludeNumbers().size();
                        int secondCountOfNumbersToRemove = 3 - Config.getFirstSecondExcludeNumbers().size();
                        Map<Integer, Integer> firstNumberStatsMap = Utils.sortByValue(Utils.convertMap(firstNumberCountMap), true);
                        Map<Integer, Integer> secondNumberStatsMap = Utils.sortByValue(Utils.convertMap(secondNumberCountMap), true);

                        List<Integer> firstNumberToBetList = new ArrayList<>(numberBetList);
                        List<Integer> secondNumberToBetList = new ArrayList<>(numberBetList);
                        List<Integer> firstNumberToRemoveList = new ArrayList<>();
                        List<Integer> secondNumberToRemoveList = new ArrayList<>();

                        firstNumberStatsMap.forEach((k, v) -> {
                            if (v >= 2 && firstCountOfNumbersToRemove > firstNumberToRemoveList.size()) {
                                firstNumberToRemoveList.add(k);
                            }
                        });

                        secondNumberStatsMap.forEach((k, v) -> {
                            if (v >= 2 && secondCountOfNumbersToRemove > secondNumberToRemoveList.size()) {
                                secondNumberToRemoveList.add(k);
                            }
                        });

                        firstNumberToBetList.removeAll(firstNumberToRemoveList);
                        secondNumberToBetList.removeAll(secondNumberToRemoveList);

                        Collections.shuffle(firstNumberToBetList);
                        Collections.shuffle(secondNumberToBetList);

                        betForFirst(bet, chip, firstNumberToBetList.subList(0, Math.min(firstNumberToBetList.size(), Config.getFirstSecondMaxBetCount())), driver);
                        money = calculateMoney(money, -Math.min(firstNumberToBetList.size(), Config.getFirstSecondMaxBetCount()) * chip);
                        betForSecond(bet, chip, secondNumberToBetList.subList(0, Math.min(secondNumberToBetList.size(), Config.getFirstSecondMaxBetCount())), driver);
                        money = calculateMoney(money, -Math.min(secondNumberToBetList.size(), Config.getFirstSecondMaxBetCount()) * chip);
                    }
                } else {
                    int firstCountOfNumbersToRemove = 3 - Config.getFirstSecondExcludeNumbers().size();
                    int secondCountOfNumbersToRemove = 3 - Config.getFirstSecondExcludeNumbers().size();
                    Map<Integer, Integer> firstNumberStatsMap = Utils.sortByValue(Utils.convertMap(firstNumberCountMap), true);
                    Map<Integer, Integer> secondNumberStatsMap = Utils.sortByValue(Utils.convertMap(secondNumberCountMap), true);

                    List<Integer> firstNumberToBetList = new ArrayList<>(numberBetList);
                    List<Integer> secondNumberToBetList = new ArrayList<>(numberBetList);
                    List<Integer> firstNumberToRemoveList = new ArrayList<>();
                    List<Integer> secondNumberToRemoveList = new ArrayList<>();

                    firstNumberStatsMap.forEach((k, v) -> {
                        if (v >= 2 && firstCountOfNumbersToRemove > firstNumberToRemoveList.size()) {
                            firstNumberToRemoveList.add(k);
                        }
                    });

                    secondNumberStatsMap.forEach((k, v) -> {
                        if (v >= 2 && secondCountOfNumbersToRemove > secondNumberToRemoveList.size()) {
                            secondNumberToRemoveList.add(k);
                        }
                    });
                    firstNumberToBetList.removeAll(firstNumberToRemoveList);
                    secondNumberToBetList.removeAll(secondNumberToRemoveList);

                    Collections.shuffle(firstNumberToBetList);
                    Collections.shuffle(secondNumberToBetList);

                    Integer firstMoneyBet = decideBetChip(lastLotteryResult.getFirst(), lastBet.getBetFirst(), isPlayTime);
                    betForFirst(bet, firstMoneyBet, firstNumberToBetList.subList(0, Math.min(firstNumberToBetList.size(), Config.getFirstSecondMaxBetCount())), driver);
                    money = calculateMoney(money, -Math.min(firstNumberToBetList.size(), Config.getFirstSecondMaxBetCount()) * firstMoneyBet);
                    Integer secondMoneyBet = decideBetChip(lastLotteryResult.getSecond(), lastBet.getBetSecond(), isPlayTime);
                    betForSecond(bet, secondMoneyBet, secondNumberToBetList.subList(0, Math.min(secondNumberToBetList.size(), Config.getFirstSecondMaxBetCount())), driver);
                    money = calculateMoney(money, -Math.min(secondNumberToBetList.size(), Config.getFirstSecondMaxBetCount()) * secondMoneyBet);

                    break;
                }
            case PAIR:
                logger.info("[Operation - Bet] Bet in PAIR mode");
                if (lastBet == null) {
                    if (!isPlayTime) {
                        logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round);
                    } else {
                        logger.info("[Operation - Bet] No last bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round - 1);
                        Set<Integer> numbersToBet = getConsecutivePairOccursWithinANumberOfRounds(round, 20);
                        System.out.println(numbersToBet);
                        if (numbersToBet.contains(lastLotteryResult.getFirst())) {
                            betForFirst(bet, chip, Collections.singletonList(lastLotteryResult.getFirst()), driver);
                            money = calculateMoney(money, -Math.min(1, Config.getFirstSecondMaxBetCount()) * chip);
                        }
                        if (numbersToBet.contains(lastLotteryResult.getSecond())) {
                            betForSecond(bet, chip, Collections.singletonList(lastLotteryResult.getSecond()), driver);
                            money = calculateMoney(money, -Math.min(1, Config.getFirstSecondMaxBetCount()) * chip);
                        }
                    }
                } else {
                    Integer firstMoneyBetChip = decideBetChip(lastLotteryResult.getFirst(), lastBet.getBetFirst(), isPlayTime);
                    betForFirst(bet, firstMoneyBetChip, Collections.singletonList(1), driver);
                    money = calculateMoney(money, -Math.min(1, Config.getFirstSecondMaxBetCount()) * firstMoneyBetChip);

                    Integer secondMoneyBetChip = decideBetChip(lastLotteryResult.getSecond(), lastBet.getBetSecond(), isPlayTime);
                    betForSecond(bet, secondMoneyBetChip, Collections.singletonList(lastLotteryResult.getSecond()), driver);
                    money = calculateMoney(money, -Math.min(1, Config.getFirstSecondMaxBetCount()) * secondMoneyBetChip);

                    break;
                }
        }

        logger.info("=============== 金额 (for test) ===============");
        logger.info("我的余额:{}", money);
        logger.info("====================================");
        firstSecondBetRepository.save(bet);
        return true;

    }

    private Set<Integer> getConsecutivePairOccursWithinANumberOfRounds(int currentRound, int numberOfRounds) {
        List<LotteryResult> lotteryResultList = new ArrayList<>(numberOfRounds);
        for (int i = 0; i < numberOfRounds; i++) {
            lotteryResultList.add(lotteryResultRepository.findByRound(currentRound - i));
        }
        Set<Integer> consecutiveRoundsForSameNumber = new HashSet<>();
        for (int i = 0; i < numberOfRounds - 1; i++) {
            if (lotteryResultList.get(i).getFirst().equals(lotteryResultList.get(i + 1).getFirst())) {
                consecutiveRoundsForSameNumber.add(lotteryResultList.get(i).getFirst());
            }
            if (lotteryResultList.get(i).getSecond().equals(lotteryResultList.get(i + 1).getSecond())) {
                consecutiveRoundsForSameNumber.add(lotteryResultList.get(i).getSecond());
            }
            if (lotteryResultList.get(i).getThird().equals(lotteryResultList.get(i + 1).getThird())) {
                consecutiveRoundsForSameNumber.add(lotteryResultList.get(i).getFirst());
            }
            if (lotteryResultList.get(i).getFourth().equals(lotteryResultList.get(i + 1).getFourth())) {
                consecutiveRoundsForSameNumber.add(lotteryResultList.get(i).getFourth());
            }
            if (lotteryResultList.get(i).getFifth().equals(lotteryResultList.get(i + 1).getFifth())) {
                consecutiveRoundsForSameNumber.add(lotteryResultList.get(i).getFifth());
            }
            if (lotteryResultList.get(i).getSixth().equals(lotteryResultList.get(i + 1).getSixth())) {
                consecutiveRoundsForSameNumber.add(lotteryResultList.get(i).getSixth());
            }
            if (lotteryResultList.get(i).getSeventh().equals(lotteryResultList.get(i + 1).getSeventh())) {
                consecutiveRoundsForSameNumber.add(lotteryResultList.get(i).getSeventh());
            }
            if (lotteryResultList.get(i).getEighth().equals(lotteryResultList.get(i + 1).getEighth())) {
                consecutiveRoundsForSameNumber.add(lotteryResultList.get(i).getEighth());
            }
            if (lotteryResultList.get(i).getNineth().equals(lotteryResultList.get(i + 1).getNineth())) {
                consecutiveRoundsForSameNumber.add(lotteryResultList.get(i).getNineth());
            }
            if (lotteryResultList.get(i).getTenth().equals(lotteryResultList.get(i + 1).getTenth())) {
                consecutiveRoundsForSameNumber.add(lotteryResultList.get(i).getTenth());
            }
        }

        return consecutiveRoundsForSameNumber;
    }

    private Integer decideBetChip(Integer winningNumber, RankSingleBet lastRankSingleBet, boolean isPlayTime) {
        Integer chip = Config.getFirstSecondLevelAccList().get(0);
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
            for (int i = 0; i < Config.getFirstSecondLevelAccList().size(); i++) {
                if (Config.getFirstSecondLevelAccList().get(i) > betChip) {
                    if (i >= 4) {
                        String message = String.format("%s - %s 第%s关 当前每一注为 %s", Store.getAccountName(), PLAYGROUND, i, Config.getFirstSecondLevelAccList().get(i));
                        winLostMailNotificationJob.sendDangerousNotificationJobs(message);
                    }
                    return Config.getFirstSecondLevelAccList().get(i);
                }
            }
            return chip;
        }
    }

    private double calculateLastLotteryResult(FirstSecondBet lastBet, LotteryResult lotteryResult) {
        if (lastBet == null) {
            return 0;
        }
        double winningMoney = 0;
        FirstSecondRatio lastRatio = firstSecondRatioRepository.findByRound(lastBet.getRound());
        if (lotteryResult.getFirst() > 5) {
            winningMoney += lastBet.getBetFirst().getDa() * lastRatio.getRatioFirst().getDa();
        } else {
            winningMoney += lastBet.getBetFirst().getXiao() * lastRatio.getRatioFirst().getXiao();
        }

        if (lotteryResult.getSecond() > 5) {
            winningMoney += lastBet.getBetSecond().getDa() * lastRatio.getRatioSecond().getDa();
        } else {
            winningMoney += lastBet.getBetSecond().getXiao() * lastRatio.getRatioSecond().getXiao();
        }

        if (lotteryResult.getFirst() == 1) {
            winningMoney += lastBet.getBetFirst().getFirst() * lastRatio.getRatioFirst().getFirst();
        } else if (lotteryResult.getFirst() == 2) {
            winningMoney += lastBet.getBetFirst().getSecond() * lastRatio.getRatioFirst().getSecond();
        } else if (lotteryResult.getFirst() == 3) {
            winningMoney += lastBet.getBetFirst().getThird() * lastRatio.getRatioFirst().getThird();
        } else if (lotteryResult.getFirst() == 4) {
            winningMoney += lastBet.getBetFirst().getFourth() * lastRatio.getRatioFirst().getFourth();
        } else if (lotteryResult.getFirst() == 5) {
            winningMoney += lastBet.getBetFirst().getFifth() * lastRatio.getRatioFirst().getFifth();
        } else if (lotteryResult.getFirst() == 6) {
            winningMoney += lastBet.getBetFirst().getSixth() * lastRatio.getRatioFirst().getSixth();
        } else if (lotteryResult.getFirst() == 7) {
            winningMoney += lastBet.getBetFirst().getSeventh() * lastRatio.getRatioFirst().getSeventh();
        } else if (lotteryResult.getFirst() == 8) {
            winningMoney += lastBet.getBetFirst().getEighth() * lastRatio.getRatioFirst().getEighth();
        } else if (lotteryResult.getFirst() == 9) {
            winningMoney += lastBet.getBetFirst().getNineth() * lastRatio.getRatioFirst().getNineth();
        } else if (lotteryResult.getFirst() == 10) {
            winningMoney += lastBet.getBetFirst().getTenth() * lastRatio.getRatioFirst().getTenth();
        }

        if (lotteryResult.getSecond() == 1) {
            winningMoney += lastBet.getBetSecond().getFirst() * lastRatio.getRatioSecond().getFirst();
        } else if (lotteryResult.getSecond() == 2) {
            winningMoney += lastBet.getBetSecond().getSecond() * lastRatio.getRatioSecond().getSecond();
        } else if (lotteryResult.getSecond() == 3) {
            winningMoney += lastBet.getBetSecond().getThird() * lastRatio.getRatioSecond().getThird();
        } else if (lotteryResult.getSecond() == 4) {
            winningMoney += lastBet.getBetSecond().getFourth() * lastRatio.getRatioSecond().getFourth();
        } else if (lotteryResult.getSecond() == 5) {
            winningMoney += lastBet.getBetSecond().getFifth() * lastRatio.getRatioSecond().getFifth();
        } else if (lotteryResult.getSecond() == 6) {
            winningMoney += lastBet.getBetSecond().getSixth() * lastRatio.getRatioSecond().getSixth();
        } else if (lotteryResult.getSecond() == 7) {
            winningMoney += lastBet.getBetSecond().getSeventh() * lastRatio.getRatioSecond().getSeventh();
        } else if (lotteryResult.getSecond() == 8) {
            winningMoney += lastBet.getBetSecond().getEighth() * lastRatio.getRatioSecond().getEighth();
        } else if (lotteryResult.getSecond() == 9) {
            winningMoney += lastBet.getBetSecond().getNineth() * lastRatio.getRatioSecond().getNineth();
        } else if (lotteryResult.getSecond() == 10) {
            winningMoney += lastBet.getBetSecond().getTenth() * lastRatio.getRatioSecond().getTenth();
        }

        logger.info("[Operation - Summary] Winning money for round {} is {}", lastBet.getRound(), winningMoney);

        return winningMoney;
    }

    private void markNumber(LotteryResult lotteryResult) {
        if (firstNumberCountMap.containsKey(lotteryResult.getFirst())) {
            firstNumberCountMap.get(lotteryResult.getFirst()).getAndIncrement();
        } else {
            firstNumberCountMap.put(lotteryResult.getFirst(), new AtomicInteger(1));
        }
        if (secondNumberCountMap.containsKey(lotteryResult.getSecond())) {
            secondNumberCountMap.get(lotteryResult.getSecond()).getAndIncrement();
        } else {
            secondNumberCountMap.put(lotteryResult.getSecond(), new AtomicInteger(1));
        }
    }

    private double calculateMoney(double money, double value) {
        return money + value;
    }

    private void betForFirst(FirstSecondBet bet, Integer chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 幸运飞艇 - {} - 期数 {} - 金额 - {}", "冠军", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetFirst(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetFirst();
        if (singleBet.getFirst() > 0) {
            String dataId = "B1_1";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String dataId = "B1_2";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String dataId = "B1_3";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String dataId = "B1_4";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String dataId = "B1_5";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String dataId = "B1_6";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String dataId = "B1_7";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String dataId = "B1_8";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String dataId = "B1_9";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String dataId = "B1_10";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
    }

    private void betForSecond(FirstSecondBet bet, Integer chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 幸运飞艇 - {} - 期数 {} - 金额 - {}", "亚军", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetSecond(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetSecond();
        if (singleBet.getFirst() > 0) {
            String dataId = "B2_1";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String dataId = "B2_2";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String dataId = "B2_3";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String dataId = "B2_4";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String dataId = "B2_5";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String dataId = "B2_6";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String dataId = "B2_7";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String dataId = "B2_8";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String dataId = "B2_9";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String dataId = "B2_10";
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
