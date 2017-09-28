package com.lingda.gamble.operation;

import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleBet;
import com.lingda.gamble.model.FifthSixthBet;
import com.lingda.gamble.model.FifthSixthRatio;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.repository.FifthSixthBetRepository;
import com.lingda.gamble.repository.FifthSixthRatioRepository;
import com.lingda.gamble.repository.WinLostMoneyRepository;
import com.lingda.gamble.service.WinLostMailNotificationJob;
import com.lingda.gamble.util.DriverUtils;
import com.lingda.gamble.util.Store;
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

//北京赛车 五六名下注
@Component
public class BetForFifthSixthOperation {

    private static final Logger logger = LoggerFactory.getLogger(BetForFifthSixthOperation.class);

    private static final String PLAYGROUND = "五六名";

    @Value("${gamble.bet.money}")
    private double money;

    @Autowired
    private WinLostMailNotificationJob winLostMailNotificationJob;

    private LinkedHashMap<Integer, AtomicInteger> fifthNumberCountMap = new LinkedHashMap<>();

    private LinkedHashMap<Integer, AtomicInteger> sixthNumberCountMap = new LinkedHashMap<>();

    private final FifthSixthBetRepository fifthSixthBetRepository;

    private final FifthSixthRatioRepository fifthSixthRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    @Autowired
    public BetForFifthSixthOperation(FifthSixthBetRepository fifthSixthBetRepository,
                                     FifthSixthRatioRepository fifthSixthRatioRepository,
                                     LotteryResultRepository lotteryResultRepository) {
        this.fifthSixthBetRepository = fifthSixthBetRepository;
        this.fifthSixthRatioRepository = fifthSixthRatioRepository;
        this.lotteryResultRepository = lotteryResultRepository;
    }

    public boolean doBet(WebDriver driver, Integer round, boolean isPlayTime) throws InterruptedException {
        Integer chip = Config.getFifthSixthLevelAccList().get(0);
        logger.info("[Operation - Bet] Base chip is {}", chip);
        logger.info("[Operation - Bet] Play Time is {}", isPlayTime);
        if (round == null) {
            logger.info("[Operation - Bet] 当前无法下注");
            return false;
        }
        fifthNumberCountMap.clear();
        sixthNumberCountMap.clear();
        logger.info("[Operation - Bet] Third fourth numbers to exclude is {}", Config.getFifthSixthExcludeNumbers());

        logger.info("[Operation - Bet] Bet for 北京赛车 - {}", PLAYGROUND);

        logger.info("[Operation - Bet] Get fetched ratio for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
        FifthSixthRatio ratio = fifthSixthRatioRepository.findByRound(round);
        if (ratio == null) {
            logger.info("[Operation - Bet] No ratio information for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
            return false;
        }

        logger.info("[Operation - Bet] Get last lottery result for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
        LotteryResult lastLotteryResult = lotteryResultRepository.findByRound(round - 1);
        if (lastLotteryResult == null) {
            logger.info("[Operation - Bet] No last lottery result for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
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

        logger.info("[Operation - Bet] Last 5 lottery result for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
        for (Map.Entry<Integer, AtomicInteger> entry : fifthNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 5 lottery result 第五名 {}:{}次 for 北京赛车 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }
        for (Map.Entry<Integer, AtomicInteger> entry : sixthNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 5 lottery result 第六名 {}:{}次 for 北京赛车 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }

//      check if the bet is already done
        if (fifthSixthBetRepository.findByRound(round) != null) {
            logger.info("[Operation - Bet] Already bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
            return false;
        }

        logger.info("[Operation - Bet] Get last bet information for 北京赛车 - {}", PLAYGROUND);
        FifthSixthBet lastBet = fifthSixthBetRepository.findByRound(round - 1);
        //            结算上次中奖情况
        logger.info("=============== 金额 (for test) ===============");
        money = calculateMoney(money, calculateLastLotteryResult(lastBet, lastLotteryResult));
        logger.info("我的余额:{}", money);
        logger.info("====================================");


        FifthSixthBet bet = new FifthSixthBet();
        bet.setRound(round);
        if (Config.getFifthSixthSmartMode()) {
            logger.info("[Operation - Bet] Bet in smart mode");
            List<Integer> stepIntegerList1 = Arrays.stream(Config.getFifthSixthSmartSwitch().get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> stepIntegerList2 = Arrays.stream(Config.getFifthSixthSmartSwitch().get(1).split(",")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> allNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            if (lotteryResult2 == null) {
                logger.info("[Operation - Bet] Cannot find lottery result for 2 consecutive round");
                return false;
            }
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
                return false;
            }
//            no last bet or last time is a win
            if (lastBet == null || decideBetChip(lastLotteryResult.getFifth(), lastBet.getBetFifth(), isPlayTime).equals(chip)) {
//            First
                if (stepIntegerList1.contains(lastLotteryResult.getFifth()) && stepIntegerList2.contains(lotteryResult2.getFifth())) {
                    logger.info("[Operation - Bet] Bingo! Bet for Fifth exclude {}", stepIntegerList2);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList2);
                    logger.info("[Operation - Bet] Bet Fifth for 北京赛车 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForFifth(bet, chip, numberBetList, driver);
                    money = calculateMoney(money, -7 * chip);
                    if (bet.getBetSixth() == null) {
                        betForSixth(bet, chip, Collections.emptyList(), driver);
                    }
                } else if (stepIntegerList2.contains(lastLotteryResult.getFifth()) && stepIntegerList1.contains(lotteryResult2.getFifth())) {
                    logger.info("[Operation - Bet] Bingo! Bet for Fifth exclude {}", stepIntegerList1);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList1);
                    logger.info("[Operation - Bet] Bet Fifth for 北京赛车 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForFifth(bet, chip, numberBetList, driver);
                    money = calculateMoney(money, -7 * chip);
                    if (bet.getBetSixth() == null) {
                        betForSixth(bet, chip, Collections.emptyList(), driver);
                    }
                }
            } else {
//                last bet is a loser
//                exclude 6,8,10
                if ((stepIntegerList1.contains(1) && lastBet.getBetFifth().getFirst() > 0)
                        || (stepIntegerList1.contains(2) && lastBet.getBetFifth().getSecond() > 0)
                        || (stepIntegerList1.contains(3) && lastBet.getBetFifth().getThird() > 0)
                        || (stepIntegerList1.contains(4) && lastBet.getBetFifth().getFourth() > 0)
                        || (stepIntegerList1.contains(5) && lastBet.getBetFifth().getFifth() > 0)
                        || (stepIntegerList1.contains(6) && lastBet.getBetFifth().getSixth() > 0)
                        || (stepIntegerList1.contains(7) && lastBet.getBetFifth().getSeventh() > 0)
                        || (stepIntegerList1.contains(8) && lastBet.getBetFifth().getEighth() > 0)
                        || (stepIntegerList1.contains(9) && lastBet.getBetFifth().getNineth() > 0)
                        || (stepIntegerList1.contains(10) && lastBet.getBetFifth().getTenth() > 0)) {
                    logger.info("[Operation - Bet] Continue! Bet for Fifth exclude {}", stepIntegerList1);
                    Integer betChip = decideBetChip(lastLotteryResult.getFifth(), lastBet.getBetFifth(), isPlayTime);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList1);
                    logger.info("[Operation - Bet] Bet Fifth for 北京赛车 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForFifth(bet, betChip, numberBetList, driver);
                    money = calculateMoney(money, -7 * betChip);
                    if (bet.getBetSixth() == null) {
                        betForSixth(bet, chip, Collections.emptyList(), driver);
                    }
                }
                //                exclude 1,3,5
                else if ((stepIntegerList2.contains(1) && lastBet.getBetFifth().getFirst() > 0)
                        || (stepIntegerList2.contains(2) && lastBet.getBetFifth().getSecond() > 0)
                        || (stepIntegerList2.contains(3) && lastBet.getBetFifth().getThird() > 0)
                        || (stepIntegerList2.contains(4) && lastBet.getBetFifth().getFourth() > 0)
                        || (stepIntegerList2.contains(5) && lastBet.getBetFifth().getFifth() > 0)
                        || (stepIntegerList2.contains(6) && lastBet.getBetFifth().getSixth() > 0)
                        || (stepIntegerList2.contains(7) && lastBet.getBetFifth().getSeventh() > 0)
                        || (stepIntegerList2.contains(8) && lastBet.getBetFifth().getEighth() > 0)
                        || (stepIntegerList2.contains(9) && lastBet.getBetFifth().getNineth() > 0)
                        || (stepIntegerList2.contains(10) && lastBet.getBetFifth().getTenth() > 0)) {
                    logger.info("[Operation - Bet] Continue! Bet for Fifth exclude {}", stepIntegerList2);
                    Integer betChip = decideBetChip(lastLotteryResult.getFifth(), lastBet.getBetFifth(), isPlayTime);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList2);
                    logger.info("[Operation - Bet] Bet Fifth for 北京赛车 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForFifth(bet, betChip, numberBetList, driver);
                    money = calculateMoney(money, -7 * betChip);
                    if (bet.getBetSixth() == null) {
                        betForSixth(bet, chip, Collections.emptyList(), driver);
                    }
                }
            }
            //            no last bet or last time is a win
            if (lastBet == null || decideBetChip(lastLotteryResult.getSixth(), lastBet.getBetSixth(), isPlayTime).equals(chip)) {
//            Sixth
                if (stepIntegerList1.contains(lastLotteryResult.getSixth()) && stepIntegerList2.contains(lotteryResult2.getSixth())) {
                    logger.info("[Operation - Bet] Bingo! Bet for Sixth exclude {}", stepIntegerList2);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList2);
                    logger.info("[Operation - Bet] Bet Sixth for 北京赛车 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForSixth(bet, chip, numberBetList, driver);
                    money = calculateMoney(money, -7 * chip);
                    if (bet.getBetFifth() == null) {
                        betForFifth(bet, chip, Collections.emptyList(), driver);
                    }
                } else if (stepIntegerList2.contains(lastLotteryResult.getSixth()) && stepIntegerList1.contains(lotteryResult2.getSixth())) {
                    logger.info("[Operation - Bet] Bingo! Bet for Sixth exclude {}", stepIntegerList1);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList1);
                    logger.info("[Operation - Bet] Bet Sixth for 北京赛车 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForSixth(bet, chip, numberBetList, driver);
                    money = calculateMoney(money, -7 * chip);
                    if (bet.getBetFifth() == null) {
                        betForFifth(bet, chip, Collections.emptyList(), driver);
                    }
                }
            } else {
//                last bet is a loser
//                exclude 6,8,10
                if ((stepIntegerList1.contains(1) && lastBet.getBetSixth().getFirst() > 0)
                        || (stepIntegerList1.contains(2) && lastBet.getBetSixth().getSecond() > 0)
                        || (stepIntegerList1.contains(3) && lastBet.getBetSixth().getThird() > 0)
                        || (stepIntegerList1.contains(4) && lastBet.getBetSixth().getFourth() > 0)
                        || (stepIntegerList1.contains(5) && lastBet.getBetSixth().getFifth() > 0)
                        || (stepIntegerList1.contains(6) && lastBet.getBetSixth().getSixth() > 0)
                        || (stepIntegerList1.contains(7) && lastBet.getBetSixth().getSeventh() > 0)
                        || (stepIntegerList1.contains(8) && lastBet.getBetSixth().getEighth() > 0)
                        || (stepIntegerList1.contains(9) && lastBet.getBetSixth().getNineth() > 0)
                        || (stepIntegerList1.contains(10) && lastBet.getBetSixth().getTenth() > 0)) {
                    logger.info("[Operation - Bet] Continue! Bet for Sixth exclude {}", stepIntegerList1);
                    Integer betChip = decideBetChip(lastLotteryResult.getSixth(), lastBet.getBetSixth(), isPlayTime);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList1);
                    logger.info("[Operation - Bet] Bet Sixth for 北京赛车 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForSixth(bet, betChip, numberBetList, driver);
                    money = calculateMoney(money, -7 * betChip);
                    if (bet.getBetFifth() == null) {
                        betForFifth(bet, chip, Collections.emptyList(), driver);
                    }
                } else if ((stepIntegerList2.contains(1) && lastBet.getBetSixth().getFirst() > 0)
                        || (stepIntegerList2.contains(2) && lastBet.getBetSixth().getSecond() > 0)
                        || (stepIntegerList2.contains(3) && lastBet.getBetSixth().getThird() > 0)
                        || (stepIntegerList2.contains(4) && lastBet.getBetSixth().getFourth() > 0)
                        || (stepIntegerList2.contains(5) && lastBet.getBetSixth().getFifth() > 0)
                        || (stepIntegerList2.contains(6) && lastBet.getBetSixth().getSixth() > 0)
                        || (stepIntegerList2.contains(7) && lastBet.getBetSixth().getSeventh() > 0)
                        || (stepIntegerList2.contains(8) && lastBet.getBetSixth().getEighth() > 0)
                        || (stepIntegerList2.contains(9) && lastBet.getBetSixth().getNineth() > 0)
                        || (stepIntegerList2.contains(10) && lastBet.getBetSixth().getTenth() > 0)) {
                    logger.info("[Operation - Bet] Continue! Bet for Sixth exclude {}", stepIntegerList2);
                    Integer betChip = decideBetChip(lastLotteryResult.getSixth(), lastBet.getBetSixth(), isPlayTime);
                    List<Integer> numberBetList = new ArrayList<>(allNumbers);
                    numberBetList.removeAll(stepIntegerList2);
                    logger.info("[Operation - Bet] Bet Sixth for 北京赛车 - {} - 期数 {} - {}", PLAYGROUND, round, numberBetList);
                    betForSixth(bet, betChip, numberBetList, driver);
                    money = calculateMoney(money, -7 * betChip);
                    if (bet.getBetFifth() == null) {
                        betForFifth(bet, chip, Collections.emptyList(), driver);
                    }
                }
            }
            if(bet.getBetFifth() == null || bet.getBetSixth()==null){
                return false;
            }

        } else {
            logger.info("[Operation - Bet] Bet in basic mode");
            List<Integer> numberBetList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            numberBetList.removeAll(Config.getFifthSixthExcludeNumbers());
//        ============== 策略逻辑 ==============
            if (lastBet == null) {
                if (!isPlayTime) {
                    logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                } else {
                    logger.info("[Operation - Bet] No last bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                    //      投注 冠-五 大
                    Collections.shuffle(numberBetList);
                    betForFifth(bet, chip, numberBetList.subList(0, Math.min(numberBetList.size(), 7)), driver);
                    Collections.shuffle(numberBetList);
                    betForSixth(bet, chip, numberBetList.subList(0, Math.min(numberBetList.size(), 7)), driver);
                    money = calculateMoney(money, -2 * Math.min(numberBetList.size(), 7) * chip);
                }
            } else {
                List<Integer> fifthNumberToBetList = new ArrayList<>(numberBetList);
                List<Integer> sixthNumberToBetList = new ArrayList<>(numberBetList);
                List<Integer> fifthNumberToRemoveList = new ArrayList<>();
                List<Integer> sixthNumberToRemoveList = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    if (fifthNumberCountMap.containsKey(i) && fifthNumberCountMap.get(i).intValue() > 2) {
                        fifthNumberToRemoveList.add(i);
                    }
                    if (sixthNumberCountMap.containsKey(i) && sixthNumberCountMap.get(i).intValue() > 2) {
                        sixthNumberToRemoveList.add(i);
                    }
                }
                fifthNumberToBetList.removeAll(fifthNumberToRemoveList);
                sixthNumberToBetList.removeAll(sixthNumberToRemoveList);

                Collections.shuffle(fifthNumberToBetList);
                Collections.shuffle(sixthNumberToBetList);

                Integer fifthMoneyBet = decideBetChip(lastLotteryResult.getFifth(), lastBet.getBetFifth(), isPlayTime);
                betForFifth(bet, fifthMoneyBet, fifthNumberToBetList.subList(0, Math.min(fifthNumberToBetList.size(), 7)), driver);
                money = calculateMoney(money, -Math.min(fifthNumberToBetList.size(), 7) * fifthMoneyBet);
                Integer sixthMoneyBet = decideBetChip(lastLotteryResult.getSixth(), lastBet.getBetSixth(), isPlayTime);
                betForSixth(bet, sixthMoneyBet, sixthNumberToBetList.subList(0, Math.min(sixthNumberToBetList.size(), 7)), driver);
                money = calculateMoney(money, -Math.min(sixthNumberToBetList.size(), 7) * sixthMoneyBet);
            }
        }

        logger.info("=============== 金额 (for test) ===============");
        logger.info("我的余额:{}", money);
        logger.info("====================================");
        fifthSixthBetRepository.save(bet);
        return true;

    }

    private Integer decideBetChip(Integer winningNumber, RankSingleBet lastRankSingleBet, boolean isPlayTime) {
        Integer chip = Config.getFifthSixthLevelAccList().get(0);
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
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getSecond() > 0 && winningNumber == 2) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getThird() > 0 && winningNumber == 5) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getFourth() > 0 && winningNumber == 6) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getFifth() > 0 && winningNumber == 5) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getSixth() > 0 && winningNumber == 6) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getSeventh() > 0 && winningNumber == 7) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getEighth() > 0 && winningNumber == 8) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getNineth() > 0 && winningNumber == 9) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getTenth() > 0 && winningNumber == 10) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else {
            for (int i = 0; i < Config.getFifthSixthLevelAccList().size(); i++) {
                if (Config.getFifthSixthLevelAccList().get(i) > betChip) {
                    if (i >= 4) {
                        String message = String.format("%s - %s 第%s关 当前每一注为 %s", Store.getAccountName(), PLAYGROUND, i, Config.getFifthSixthLevelAccList().get(i));
                        winLostMailNotificationJob.sendDangerousNotificationJobs(message);
                    }
                    return Config.getFifthSixthLevelAccList().get(i);
                }
            }
            return chip;
        }
    }

    private double calculateLastLotteryResult(FifthSixthBet lastBet, LotteryResult lotteryResult) {
        if (lastBet == null) {
            return 0;
        }
        double winningMoney = 0;
        FifthSixthRatio lastRatio = fifthSixthRatioRepository.findByRound(lastBet.getRound());
        if (lotteryResult.getFifth() > 5) {
            winningMoney += lastBet.getBetFifth().getDa() * lastRatio.getRatioFifth().getDa();
        } else {
            winningMoney += lastBet.getBetFifth().getXiao() * lastRatio.getRatioFifth().getXiao();
        }

        if (lotteryResult.getSixth() > 5) {
            winningMoney += lastBet.getBetSixth().getDa() * lastRatio.getRatioSixth().getDa();
        } else {
            winningMoney += lastBet.getBetSixth().getXiao() * lastRatio.getRatioSixth().getXiao();
        }

        if (lotteryResult.getFifth() == 1) {
            winningMoney += lastBet.getBetFifth().getFirst() * lastRatio.getRatioFifth().getFirst();
        } else if (lotteryResult.getThird() == 2) {
            winningMoney += lastBet.getBetFifth().getSecond() * lastRatio.getRatioFifth().getSecond();
        } else if (lotteryResult.getThird() == 3) {
            winningMoney += lastBet.getBetFifth().getThird() * lastRatio.getRatioFifth().getThird();
        } else if (lotteryResult.getThird() == 4) {
            winningMoney += lastBet.getBetFifth().getFourth() * lastRatio.getRatioFifth().getFourth();
        } else if (lotteryResult.getThird() == 5) {
            winningMoney += lastBet.getBetFifth().getFifth() * lastRatio.getRatioFifth().getFifth();
        } else if (lotteryResult.getThird() == 6) {
            winningMoney += lastBet.getBetFifth().getSixth() * lastRatio.getRatioFifth().getSixth();
        } else if (lotteryResult.getThird() == 7) {
            winningMoney += lastBet.getBetFifth().getSeventh() * lastRatio.getRatioFifth().getSeventh();
        } else if (lotteryResult.getThird() == 8) {
            winningMoney += lastBet.getBetFifth().getEighth() * lastRatio.getRatioFifth().getEighth();
        } else if (lotteryResult.getThird() == 9) {
            winningMoney += lastBet.getBetFifth().getNineth() * lastRatio.getRatioFifth().getNineth();
        } else if (lotteryResult.getThird() == 10) {
            winningMoney += lastBet.getBetFifth().getTenth() * lastRatio.getRatioFifth().getTenth();
        }

        if (lotteryResult.getSixth() == 1) {
            winningMoney += lastBet.getBetSixth().getFirst() * lastRatio.getRatioSixth().getFirst();
        } else if (lotteryResult.getFourth() == 2) {
            winningMoney += lastBet.getBetSixth().getSecond() * lastRatio.getRatioSixth().getSecond();
        } else if (lotteryResult.getFourth() == 3) {
            winningMoney += lastBet.getBetSixth().getThird() * lastRatio.getRatioSixth().getThird();
        } else if (lotteryResult.getFourth() == 4) {
            winningMoney += lastBet.getBetSixth().getFourth() * lastRatio.getRatioSixth().getFourth();
        } else if (lotteryResult.getFourth() == 5) {
            winningMoney += lastBet.getBetSixth().getFifth() * lastRatio.getRatioSixth().getFifth();
        } else if (lotteryResult.getFourth() == 6) {
            winningMoney += lastBet.getBetSixth().getSixth() * lastRatio.getRatioSixth().getSixth();
        } else if (lotteryResult.getFourth() == 7) {
            winningMoney += lastBet.getBetSixth().getSeventh() * lastRatio.getRatioSixth().getSeventh();
        } else if (lotteryResult.getFourth() == 8) {
            winningMoney += lastBet.getBetSixth().getEighth() * lastRatio.getRatioSixth().getEighth();
        } else if (lotteryResult.getFourth() == 9) {
            winningMoney += lastBet.getBetSixth().getNineth() * lastRatio.getRatioSixth().getNineth();
        } else if (lotteryResult.getFourth() == 10) {
            winningMoney += lastBet.getBetSixth().getTenth() * lastRatio.getRatioSixth().getTenth();
        }

        logger.info("[Operation - Summary] Winning money for round {} is {}", lastBet.getRound(), winningMoney);

        return winningMoney;
    }

    private void markNumber(LotteryResult lotteryResult) {
        if (fifthNumberCountMap.containsKey(lotteryResult.getFifth())) {
            fifthNumberCountMap.get(lotteryResult.getFifth()).getAndIncrement();
        } else {
            fifthNumberCountMap.put(lotteryResult.getFifth(), new AtomicInteger(1));
        }
        if (sixthNumberCountMap.containsKey(lotteryResult.getSixth())) {
            sixthNumberCountMap.get(lotteryResult.getSixth()).getAndIncrement();
        } else {
            sixthNumberCountMap.put(lotteryResult.getSixth(), new AtomicInteger(1));
        }
    }

    private double calculateMoney(double money, double value) {
        return money + value;
    }

    private void betForFifth(FifthSixthBet bet, Integer chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "第五名", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetFifth(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetFifth();
        if (singleBet.getFirst() > 0) {
            String name = getInputName(1, 5);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String name = getInputName(2, 5);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String name = getInputName(3, 5);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String name = getInputName(4, 5);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String name = getInputName(5, 5);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String name = getInputName(6, 5);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String name = getInputName(7, 5);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String name = getInputName(8, 5);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String name = getInputName(9, 5);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String name = getInputName(10, 5);
            sendKeys(driver, name, String.valueOf(chip));
        }
    }

    private void betForSixth(FifthSixthBet bet, Integer chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "第六名", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetSixth(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetSixth();
        if (singleBet.getFirst() > 0) {
            String name = getInputName(1, 6);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String name = getInputName(2, 6);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String name = getInputName(3, 6);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String name = getInputName(4, 6);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String name = getInputName(5, 6);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String name = getInputName(6, 6);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String name = getInputName(7, 6);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String name = getInputName(8, 6);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String name = getInputName(9, 6);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String name = getInputName(10, 6);
            sendKeys(driver, name, String.valueOf(chip));
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
        DriverUtils.returnOnFindingElementEqualsName(driver, By.tagName("input"), name).sendKeys(chip);
    }

    private String getInputName(Integer number, Integer position) {
        String name = "";
        switch (number) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                name = String.format("b_%s_0_0%s", position, number);
                break;
            case 10:
                name = String.format("b_%s_0_%s", position, number);
                break;
        }
        return name;
    }
}
