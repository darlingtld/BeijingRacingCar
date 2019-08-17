package com.lingda.gamble.operation;

import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleBet;
import com.lingda.gamble.model.NinethTenthBet;
import com.lingda.gamble.model.NinethTenthRatio;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.repository.NinethTenthBetRepository;
import com.lingda.gamble.repository.NinethTenthRatioRepository;
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

//幸运飞艇 九十名下注
@Component
public class BetForNinethTenthOperation {

    private static final Logger logger = LoggerFactory.getLogger(BetForNinethTenthOperation.class);

    private static final String PLAYGROUND = "九十名";

    @Value("${gamble.bet.money}")
    private double money;

    @Autowired
    private WinLostMailNotificationJob winLostMailNotificationJob;

    private LinkedHashMap<Integer, AtomicInteger> ninethNumberCountMap = new LinkedHashMap<>();

    private LinkedHashMap<Integer, AtomicInteger> tenthNumberCountMap = new LinkedHashMap<>();

    private final NinethTenthBetRepository ninethTenthBetRepository;

    private final NinethTenthRatioRepository ninethTenthRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    @Autowired
    public BetForNinethTenthOperation(NinethTenthBetRepository ninethTenthBetRepository,
                                      NinethTenthRatioRepository ninethTenthRatioRepository,
                                      LotteryResultRepository lotteryResultRepository) {
        this.ninethTenthBetRepository = ninethTenthBetRepository;
        this.ninethTenthRatioRepository = ninethTenthRatioRepository;
        this.lotteryResultRepository = lotteryResultRepository;
    }

    public boolean doBet(WebDriver driver, Integer round, boolean isPlayTime) throws InterruptedException {
        Integer chip = Config.getNinethTenthLevelAccList().get(0);
        logger.info("[Operation - Bet] Base chip is {}", chip);
        logger.info("[Operation - Bet] Play Time is {}", isPlayTime);
        if (round == null) {
            logger.info("[Operation - Bet] 当前无法下注");
            return false;
        }
        ninethNumberCountMap.clear();
        tenthNumberCountMap.clear();
        logger.info("[Operation - Bet] Third fourth numbers to exclude is {}", Config.getNinethTenthExcludeNumbers());

        logger.info("[Operation - Bet] Bet for 幸运飞艇 - {}", PLAYGROUND);

        logger.info("[Operation - Bet] Get fetched ratio for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round);
        NinethTenthRatio ratio = ninethTenthRatioRepository.findByRound(round);
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
        for (Map.Entry<Integer, AtomicInteger> entry : ninethNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 7 lottery result 第九名 {}:{}次 for 幸运飞艇 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }
        for (Map.Entry<Integer, AtomicInteger> entry : tenthNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 7 lottery result 第十名 {}:{}次 for 幸运飞艇 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }

//      check if the bet is already done
        if (ninethTenthBetRepository.findByRound(round) != null) {
            logger.info("[Operation - Bet] Already bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round);
            return false;
        }

        logger.info("[Operation - Bet] Get last bet information for 幸运飞艇 - {}", PLAYGROUND);
        NinethTenthBet lastBet = ninethTenthBetRepository.findByRound(round - 1);
        //            结算上次中奖情况
        logger.info("=============== 金额 (for test) ===============");
        money = calculateMoney(money, calculateLastLotteryResult(lastBet, lastLotteryResult));
        logger.info("我的余额:{}", money);
        logger.info("====================================");


        NinethTenthBet bet = new NinethTenthBet();
        bet.setRound(round);
        if (Config.getNinethTenthSmartMode()) {
            logger.info("[Operation - Bet] Bet in smart mode");
            List<Integer> stepIntegerList1 = Arrays.stream(Config.getNinethTenthSmartSwitch().get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> stepIntegerList2 = Arrays.stream(Config.getNinethTenthSmartSwitch().get(1).split(",")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> allNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

            if (Config.getNinethTenthSmartDetectRoundNumber() == 2) {
                if (lotteryResult2 == null) {
                    logger.info("[Operation - Bet] Cannot find lottery result for 2 consecutive round");
                    return false;
                }
            }
            if (Config.getNinethTenthSmartDetectRoundNumber() == 3) {
                if (lotteryResult2 == null || lotteryResult3 == null) {
                    logger.info("[Operation - Bet] Cannot find lottery result for 3 consecutive round");
                    return false;
                }
            }
            if (Config.getNinethTenthSmartDetectRoundNumber() == 4) {
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
            if (lastBet == null || Utils.isLastBetWin(lastLotteryResult.getNineth(), lastBet.getBetNineth())) {
//            First
                if (Utils.detectStepIntegerList(Config.getNinethTenthSmartDetectRoundNumber(), stepIntegerList1, stepIntegerList2, lastLotteryResult.getNineth(), lotteryResult2.getNineth(), lotteryResult3.getNineth(), lotteryResult4.getNineth())) {
                    logger.info("[Operation - Bet] Bingo! Bet for Nineth exclude {}", stepIntegerList2);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList2);
                    logger.info("[Operation - Bet] Bet Nineth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForNineth(bet, chip, numberBetList, driver);
                    money = calculateMoney(money, -7 * chip);
                    if (bet.getBetTenth() == null) {
                        betForTenth(bet, chip, Collections.emptyList(), driver);
                    }
                } else if (Utils.detectStepIntegerList(Config.getNinethTenthSmartDetectRoundNumber(), stepIntegerList2, stepIntegerList1, lastLotteryResult.getNineth(), lotteryResult2.getNineth(), lotteryResult3.getNineth(), lotteryResult4.getNineth())) {
                    logger.info("[Operation - Bet] Bingo! Bet for Nineth exclude {}", stepIntegerList1);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList1);
                    logger.info("[Operation - Bet] Bet Nineth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForNineth(bet, chip, numberBetList, driver);
                    money = calculateMoney(money, -7 * chip);
                    if (bet.getBetTenth() == null) {
                        betForTenth(bet, chip, Collections.emptyList(), driver);
                    }
                }
            } else {
//                last bet is a loser
//                exclude 6,8,10
                if ((stepIntegerList1.contains(1) && lastBet.getBetNineth().getFirst() > 0)
                        || (stepIntegerList1.contains(2) && lastBet.getBetNineth().getSecond() > 0)
                        || (stepIntegerList1.contains(3) && lastBet.getBetNineth().getThird() > 0)
                        || (stepIntegerList1.contains(4) && lastBet.getBetNineth().getFourth() > 0)
                        || (stepIntegerList1.contains(5) && lastBet.getBetNineth().getFifth() > 0)
                        || (stepIntegerList1.contains(6) && lastBet.getBetNineth().getSixth() > 0)
                        || (stepIntegerList1.contains(7) && lastBet.getBetNineth().getSeventh() > 0)
                        || (stepIntegerList1.contains(8) && lastBet.getBetNineth().getEighth() > 0)
                        || (stepIntegerList1.contains(9) && lastBet.getBetNineth().getNineth() > 0)
                        || (stepIntegerList1.contains(10) && lastBet.getBetNineth().getTenth() > 0)) {
                    logger.info("[Operation - Bet] Continue! Bet for Nineth exclude {}", stepIntegerList1);
                    Integer betChip = decideBetChip(lastLotteryResult.getNineth(), lastBet.getBetNineth(), isPlayTime);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList1);
                    logger.info("[Operation - Bet] Bet Nineth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForNineth(bet, betChip, numberBetList, driver);
                    money = calculateMoney(money, -7 * betChip);
                    if (bet.getBetTenth() == null) {
                        betForTenth(bet, chip, Collections.emptyList(), driver);
                    }
                }
                //                exclude 1,3,5
                else if ((stepIntegerList2.contains(1) && lastBet.getBetNineth().getFirst() > 0)
                        || (stepIntegerList2.contains(2) && lastBet.getBetNineth().getSecond() > 0)
                        || (stepIntegerList2.contains(3) && lastBet.getBetNineth().getThird() > 0)
                        || (stepIntegerList2.contains(4) && lastBet.getBetNineth().getFourth() > 0)
                        || (stepIntegerList2.contains(5) && lastBet.getBetNineth().getFifth() > 0)
                        || (stepIntegerList2.contains(6) && lastBet.getBetNineth().getSixth() > 0)
                        || (stepIntegerList2.contains(7) && lastBet.getBetNineth().getSeventh() > 0)
                        || (stepIntegerList2.contains(8) && lastBet.getBetNineth().getEighth() > 0)
                        || (stepIntegerList2.contains(9) && lastBet.getBetNineth().getNineth() > 0)
                        || (stepIntegerList2.contains(10) && lastBet.getBetNineth().getTenth() > 0)) {
                    logger.info("[Operation - Bet] Continue! Bet for Nineth exclude {}", stepIntegerList2);
                    Integer betChip = decideBetChip(lastLotteryResult.getNineth(), lastBet.getBetNineth(), isPlayTime);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList2);
                    logger.info("[Operation - Bet] Bet Nineth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForNineth(bet, betChip, numberBetList, driver);
                    money = calculateMoney(money, -7 * betChip);
                    if (bet.getBetTenth() == null) {
                        betForTenth(bet, chip, Collections.emptyList(), driver);
                    }
                }
            }
            //            no last bet or last time is a win
            if (lastBet == null || Utils.isLastBetWin(lastLotteryResult.getTenth(), lastBet.getBetTenth())) {
//            Tenth
                if (Utils.detectStepIntegerList(Config.getNinethTenthSmartDetectRoundNumber(), stepIntegerList1, stepIntegerList2, lastLotteryResult.getTenth(), lotteryResult2.getTenth(), lotteryResult3.getTenth(), lotteryResult4.getTenth())) {
                    logger.info("[Operation - Bet] Bingo! Bet for Tenth exclude {}", stepIntegerList2);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList2);
                    logger.info("[Operation - Bet] Bet Tenth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForTenth(bet, chip, numberBetList, driver);
                    money = calculateMoney(money, -7 * chip);
                    if (bet.getBetNineth() == null) {
                        betForNineth(bet, chip, Collections.emptyList(), driver);
                    }
                } else if (Utils.detectStepIntegerList(Config.getNinethTenthSmartDetectRoundNumber(), stepIntegerList2, stepIntegerList1, lastLotteryResult.getTenth(), lotteryResult2.getTenth(), lotteryResult3.getTenth(), lotteryResult4.getTenth())) {
                    logger.info("[Operation - Bet] Bingo! Bet for Tenth exclude {}", stepIntegerList1);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList1);
                    logger.info("[Operation - Bet] Bet Tenth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForTenth(bet, chip, numberBetList, driver);
                    money = calculateMoney(money, -7 * chip);
                    if (bet.getBetNineth() == null) {
                        betForNineth(bet, chip, Collections.emptyList(), driver);
                    }
                }
            } else {
//                last bet is a loser
//                exclude 6,8,10
                if ((stepIntegerList1.contains(1) && lastBet.getBetTenth().getFirst() > 0)
                        || (stepIntegerList1.contains(2) && lastBet.getBetTenth().getSecond() > 0)
                        || (stepIntegerList1.contains(3) && lastBet.getBetTenth().getThird() > 0)
                        || (stepIntegerList1.contains(4) && lastBet.getBetTenth().getFourth() > 0)
                        || (stepIntegerList1.contains(5) && lastBet.getBetTenth().getFifth() > 0)
                        || (stepIntegerList1.contains(6) && lastBet.getBetTenth().getSixth() > 0)
                        || (stepIntegerList1.contains(7) && lastBet.getBetTenth().getSeventh() > 0)
                        || (stepIntegerList1.contains(8) && lastBet.getBetTenth().getEighth() > 0)
                        || (stepIntegerList1.contains(9) && lastBet.getBetTenth().getNineth() > 0)
                        || (stepIntegerList1.contains(10) && lastBet.getBetTenth().getTenth() > 0)) {
                    logger.info("[Operation - Bet] Continue! Bet for Tenth exclude {}", stepIntegerList1);
                    Integer betChip = decideBetChip(lastLotteryResult.getTenth(), lastBet.getBetTenth(), isPlayTime);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList1);
                    logger.info("[Operation - Bet] Bet Tenth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForTenth(bet, betChip, numberBetList, driver);
                    money = calculateMoney(money, -7 * betChip);
                    if (bet.getBetNineth() == null) {
                        betForNineth(bet, chip, Collections.emptyList(), driver);
                    }
                } else if ((stepIntegerList2.contains(1) && lastBet.getBetTenth().getFirst() > 0)
                        || (stepIntegerList2.contains(2) && lastBet.getBetTenth().getSecond() > 0)
                        || (stepIntegerList2.contains(3) && lastBet.getBetTenth().getThird() > 0)
                        || (stepIntegerList2.contains(4) && lastBet.getBetTenth().getFourth() > 0)
                        || (stepIntegerList2.contains(5) && lastBet.getBetTenth().getFifth() > 0)
                        || (stepIntegerList2.contains(6) && lastBet.getBetTenth().getSixth() > 0)
                        || (stepIntegerList2.contains(7) && lastBet.getBetTenth().getSeventh() > 0)
                        || (stepIntegerList2.contains(8) && lastBet.getBetTenth().getEighth() > 0)
                        || (stepIntegerList2.contains(9) && lastBet.getBetTenth().getNineth() > 0)
                        || (stepIntegerList2.contains(10) && lastBet.getBetTenth().getTenth() > 0)) {
                    logger.info("[Operation - Bet] Continue! Bet for Tenth exclude {}", stepIntegerList2);
                    Integer betChip = decideBetChip(lastLotteryResult.getTenth(), lastBet.getBetTenth(), isPlayTime);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList2);
                    logger.info("[Operation - Bet] Bet Tenth for 幸运飞艇 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForTenth(bet, betChip, numberBetList, driver);
                    money = calculateMoney(money, -7 * betChip);
                    if (bet.getBetNineth() == null) {
                        betForNineth(bet, chip, Collections.emptyList(), driver);
                    }
                }
            }
            if (bet.getBetNineth() == null || bet.getBetTenth() == null) {
                return false;
            }
        } else {
            List<Integer> numberBetList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            numberBetList.removeAll(Config.getNinethTenthExcludeNumbers());
//        ============== 策略逻辑 ==============
            if (lastBet == null) {
                if (!isPlayTime) {
                    logger.info("[Operation - Bet] Not in play time.  Do not bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round - 1);
                } else {
                    logger.info("[Operation - Bet] No last bet for 幸运飞艇 - {} - 期数 {}", PLAYGROUND, round - 1);

                    int ninethCountOfNumbersToRemove = 3 - Config.getNinethTenthExcludeNumbers().size();
                    int tenthCountOfNumbersToRemove = 3 - Config.getNinethTenthExcludeNumbers().size();
                    Map<Integer, Integer> ninethNumberStatsMap = Utils.sortByValue(Utils.convertMap(ninethNumberCountMap), true);
                    Map<Integer, Integer> tenthNumberStatsMap = Utils.sortByValue(Utils.convertMap(tenthNumberCountMap), true);

                    List<Integer> ninethNumberToBetList = new ArrayList<>(numberBetList);
                    List<Integer> tenthNumberToBetList = new ArrayList<>(numberBetList);
                    List<Integer> ninethNumberToRemoveList = new ArrayList<>();
                    List<Integer> tenthNumberToRemoveList = new ArrayList<>();

                    ninethNumberStatsMap.forEach((k, v) -> {
                        if (v >= 2 && ninethCountOfNumbersToRemove > ninethNumberToRemoveList.size()) {
                            ninethNumberToRemoveList.add(k);
                        }
                    });

                    tenthNumberStatsMap.forEach((k, v) -> {
                        if (v >= 2 && tenthCountOfNumbersToRemove > tenthNumberToRemoveList.size()) {
                            tenthNumberToRemoveList.add(k);
                        }
                    });

                    ninethNumberToBetList.removeAll(ninethNumberToRemoveList);
                    tenthNumberToBetList.removeAll(tenthNumberToRemoveList);

                    Collections.shuffle(ninethNumberToBetList);
                    Collections.shuffle(tenthNumberToBetList);

                    betForNineth(bet, chip, ninethNumberToBetList.subList(0, Math.min(ninethNumberToBetList.size(), Config.getNinethTenthMaxBetCount())), driver);
                    money = calculateMoney(money, -Math.min(ninethNumberToBetList.size(), Config.getNinethTenthMaxBetCount()) * chip);
                    betForTenth(bet, chip, tenthNumberToBetList.subList(0, Math.min(tenthNumberToBetList.size(), Config.getNinethTenthMaxBetCount())), driver);
                    money = calculateMoney(money, -Math.min(tenthNumberToBetList.size(), Config.getNinethTenthMaxBetCount()) * chip);

                }
            } else {
                int ninethCountOfNumbersToRemove = 3 - Config.getNinethTenthExcludeNumbers().size();
                int tenthCountOfNumbersToRemove = 3 - Config.getNinethTenthExcludeNumbers().size();
                Map<Integer, Integer> ninethNumberStatsMap = Utils.sortByValue(Utils.convertMap(ninethNumberCountMap), true);
                Map<Integer, Integer> tenthNumberStatsMap = Utils.sortByValue(Utils.convertMap(tenthNumberCountMap), true);

                List<Integer> ninethNumberToBetList = new ArrayList<>(numberBetList);
                List<Integer> tenthNumberToBetList = new ArrayList<>(numberBetList);
                List<Integer> ninethNumberToRemoveList = new ArrayList<>();
                List<Integer> tenthNumberToRemoveList = new ArrayList<>();

                ninethNumberStatsMap.forEach((k, v) -> {
                    if (v >= 2 && ninethCountOfNumbersToRemove > ninethNumberToRemoveList.size()) {
                        ninethNumberToRemoveList.add(k);
                    }
                });

                tenthNumberStatsMap.forEach((k, v) -> {
                    if (v >= 2 && tenthCountOfNumbersToRemove > tenthNumberToRemoveList.size()) {
                        tenthNumberToRemoveList.add(k);
                    }
                });
                ninethNumberToBetList.removeAll(ninethNumberToRemoveList);
                tenthNumberToBetList.removeAll(tenthNumberToRemoveList);

                Collections.shuffle(ninethNumberToBetList);
                Collections.shuffle(tenthNumberToBetList);

                Integer thirdMoneyBet = decideBetChip(lastLotteryResult.getNineth(), lastBet.getBetNineth(), isPlayTime);
                betForNineth(bet, thirdMoneyBet, ninethNumberToBetList.subList(0, Math.min(ninethNumberToBetList.size(), Config.getNinethTenthMaxBetCount())), driver);
                money = calculateMoney(money, -Math.min(ninethNumberToBetList.size(), Config.getNinethTenthMaxBetCount()) * thirdMoneyBet);
                Integer fourthMoneyBet = decideBetChip(lastLotteryResult.getTenth(), lastBet.getBetTenth(), isPlayTime);
                betForTenth(bet, fourthMoneyBet, tenthNumberToBetList.subList(0, Math.min(tenthNumberToBetList.size(), Config.getNinethTenthMaxBetCount())), driver);
                money = calculateMoney(money, -Math.min(tenthNumberToBetList.size(), Config.getNinethTenthMaxBetCount()) * fourthMoneyBet);

            }
        }

        logger.info("=============== 金额 (for test) ===============");
        logger.info("我的余额:{}", money);
        logger.info("====================================");
        ninethTenthBetRepository.save(bet);
        return true;

    }

    private Integer decideBetChip(Integer winningNumber, RankSingleBet lastRankSingleBet, boolean isPlayTime) {
        Integer chip = Config.getNinethTenthLevelAccList().get(0);
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
            for (int i = 0; i < Config.getNinethTenthLevelAccList().size(); i++) {
                if (Config.getNinethTenthLevelAccList().get(i) > betChip) {
                    if (i >= 4) {
                        String message = String.format("%s - %s 第%s关 当前每一注为 %s", Store.getAccountName(), PLAYGROUND, i, Config.getNinethTenthLevelAccList().get(i));
                        winLostMailNotificationJob.sendDangerousNotificationJobs(message);
                    }
                    return Config.getNinethTenthLevelAccList().get(i);
                }
            }
            return chip;
        }
    }

    private double calculateLastLotteryResult(NinethTenthBet lastBet, LotteryResult lotteryResult) {
        if (lastBet == null) {
            return 0;
        }
        double winningMoney = 0;
        NinethTenthRatio lastRatio = ninethTenthRatioRepository.findByRound(lastBet.getRound());
        if (lotteryResult.getFifth() > 5) {
            winningMoney += lastBet.getBetNineth().getDa() * lastRatio.getRatioNineth().getDa();
        } else {
            winningMoney += lastBet.getBetNineth().getXiao() * lastRatio.getRatioNineth().getXiao();
        }

        if (lotteryResult.getSixth() > 5) {
            winningMoney += lastBet.getBetTenth().getDa() * lastRatio.getRatioTenth().getDa();
        } else {
            winningMoney += lastBet.getBetTenth().getXiao() * lastRatio.getRatioTenth().getXiao();
        }

        if (lotteryResult.getFifth() == 1) {
            winningMoney += lastBet.getBetNineth().getFirst() * lastRatio.getRatioNineth().getFirst();
        } else if (lotteryResult.getThird() == 2) {
            winningMoney += lastBet.getBetNineth().getSecond() * lastRatio.getRatioNineth().getSecond();
        } else if (lotteryResult.getThird() == 3) {
            winningMoney += lastBet.getBetNineth().getThird() * lastRatio.getRatioNineth().getThird();
        } else if (lotteryResult.getThird() == 4) {
            winningMoney += lastBet.getBetNineth().getFourth() * lastRatio.getRatioNineth().getFourth();
        } else if (lotteryResult.getThird() == 5) {
            winningMoney += lastBet.getBetNineth().getFifth() * lastRatio.getRatioNineth().getFifth();
        } else if (lotteryResult.getThird() == 6) {
            winningMoney += lastBet.getBetNineth().getSixth() * lastRatio.getRatioNineth().getSixth();
        } else if (lotteryResult.getThird() == 7) {
            winningMoney += lastBet.getBetNineth().getSeventh() * lastRatio.getRatioNineth().getSeventh();
        } else if (lotteryResult.getThird() == 8) {
            winningMoney += lastBet.getBetNineth().getEighth() * lastRatio.getRatioNineth().getEighth();
        } else if (lotteryResult.getThird() == 9) {
            winningMoney += lastBet.getBetNineth().getNineth() * lastRatio.getRatioNineth().getNineth();
        } else if (lotteryResult.getThird() == 10) {
            winningMoney += lastBet.getBetNineth().getTenth() * lastRatio.getRatioNineth().getTenth();
        }

        if (lotteryResult.getSixth() == 1) {
            winningMoney += lastBet.getBetTenth().getFirst() * lastRatio.getRatioTenth().getFirst();
        } else if (lotteryResult.getFourth() == 2) {
            winningMoney += lastBet.getBetTenth().getSecond() * lastRatio.getRatioTenth().getSecond();
        } else if (lotteryResult.getFourth() == 3) {
            winningMoney += lastBet.getBetTenth().getThird() * lastRatio.getRatioTenth().getThird();
        } else if (lotteryResult.getFourth() == 4) {
            winningMoney += lastBet.getBetTenth().getFourth() * lastRatio.getRatioTenth().getFourth();
        } else if (lotteryResult.getFourth() == 5) {
            winningMoney += lastBet.getBetTenth().getFifth() * lastRatio.getRatioTenth().getFifth();
        } else if (lotteryResult.getFourth() == 6) {
            winningMoney += lastBet.getBetTenth().getSixth() * lastRatio.getRatioTenth().getSixth();
        } else if (lotteryResult.getFourth() == 7) {
            winningMoney += lastBet.getBetTenth().getSeventh() * lastRatio.getRatioTenth().getSeventh();
        } else if (lotteryResult.getFourth() == 8) {
            winningMoney += lastBet.getBetTenth().getEighth() * lastRatio.getRatioTenth().getEighth();
        } else if (lotteryResult.getFourth() == 9) {
            winningMoney += lastBet.getBetTenth().getNineth() * lastRatio.getRatioTenth().getNineth();
        } else if (lotteryResult.getFourth() == 10) {
            winningMoney += lastBet.getBetTenth().getTenth() * lastRatio.getRatioTenth().getTenth();
        }

        logger.info("[Operation - Summary] Winning money for round {} is {}", lastBet.getRound(), winningMoney);

        return winningMoney;
    }

    private void markNumber(LotteryResult lotteryResult) {
        if (ninethNumberCountMap.containsKey(lotteryResult.getNineth())) {
            ninethNumberCountMap.get(lotteryResult.getNineth()).getAndIncrement();
        } else {
            ninethNumberCountMap.put(lotteryResult.getNineth(), new AtomicInteger(1));
        }
        if (tenthNumberCountMap.containsKey(lotteryResult.getTenth())) {
            tenthNumberCountMap.get(lotteryResult.getTenth()).getAndIncrement();
        } else {
            tenthNumberCountMap.put(lotteryResult.getTenth(), new AtomicInteger(1));
        }
    }

    private double calculateMoney(double money, double value) {
        return money + value;
    }

    private void betForNineth(NinethTenthBet bet, Integer chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 幸运飞艇 - {} - 期数 {} - 金额 - {}", "第九名", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetNineth(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetNineth();
        if (singleBet.getFirst() > 0) {
            String dataId = "B9_1";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String dataId = "B9_2";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String dataId = "B9_3";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String dataId = "B9_4";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String dataId = "B9_5";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String dataId = "B9_6";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String dataId = "B9_7";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String dataId = "B9_8";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String dataId = "B9_9";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String dataId = "B9_10";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
    }

    private void betForTenth(NinethTenthBet bet, Integer chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 幸运飞艇 - {} - 期数 {} - 金额 - {}", "第十名", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetTenth(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetTenth();
        if (singleBet.getFirst() > 0) {
            String dataId = "B10_1";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String dataId = "B10_2";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String dataId = "B10_3";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String dataId = "B10_4";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String dataId = "B10_5";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String dataId = "B10_6";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String dataId = "B10_7";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String dataId = "B10_8";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String dataId = "B10_9";
            sendKeys(driver, dataId, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String dataId = "B10_10";
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

    private void sendKeys(WebDriver driver, String name, String chip) {
        DriverUtils.returnOnFindingElementEqualsDataId(driver, By.tagName("input"), name).sendKeys(chip);
    }
}
