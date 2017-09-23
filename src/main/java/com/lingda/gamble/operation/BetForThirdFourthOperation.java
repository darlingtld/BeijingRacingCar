package com.lingda.gamble.operation;

import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleBet;
import com.lingda.gamble.model.ThirdFourthBet;
import com.lingda.gamble.model.ThirdFourthRatio;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.repository.ThirdFourthBetRepository;
import com.lingda.gamble.repository.ThirdFourthRatioRepository;
import com.lingda.gamble.util.DriverUtils;
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
import java.util.stream.Stream;

//北京赛车 三四名下注
@Component
public class BetForThirdFourthOperation {

    private static final Logger logger = LoggerFactory.getLogger(BetForThirdFourthOperation.class);

    private static final String PLAYGROUND = "三四名";

    @Value("${gamble.bet.money}")
    private double money;

    private LinkedHashMap<Integer, AtomicInteger> thirdNumberCountMap = new LinkedHashMap<>();

    private LinkedHashMap<Integer, AtomicInteger> fourthNumberCountMap = new LinkedHashMap<>();

    private final ThirdFourthBetRepository thirdFourthBetRepository;

    private final ThirdFourthRatioRepository thirdFourthRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    @Autowired
    public BetForThirdFourthOperation(ThirdFourthBetRepository thirdFourthBetRepository,
                                      ThirdFourthRatioRepository thirdFourthRatioRepository,
                                      LotteryResultRepository lotteryResultRepository) {
        this.thirdFourthBetRepository = thirdFourthBetRepository;
        this.thirdFourthRatioRepository = thirdFourthRatioRepository;
        this.lotteryResultRepository = lotteryResultRepository;
    }

    public boolean doBet(WebDriver driver, Integer round, boolean isPlayTime) throws InterruptedException {
        Integer chip = Config.getThirdFourthChip();
        logger.info("[Operation - Bet] Base chip is {}", chip);
        logger.info("[Operation - Bet] Play Time is {}", isPlayTime);
        if (round == null) {
            logger.info("[Operation - Bet] 当前无法下注");
            return false;
        }
        thirdNumberCountMap.clear();
        fourthNumberCountMap.clear();
        logger.info("[Operation - Bet] Third fourth numbers to exclude is {}", Config.getThirdFourthExcludeNumbers());

        logger.info("[Operation - Bet] Bet for 北京赛车 - {}", PLAYGROUND);

        logger.info("[Operation - Bet] Get fetched ratio for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
        ThirdFourthRatio ratio = thirdFourthRatioRepository.findByRound(round);
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
        for (Map.Entry<Integer, AtomicInteger> entry : thirdNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 5 lottery result 第三名 {}:{}次 for 北京赛车 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }
        for (Map.Entry<Integer, AtomicInteger> entry : fourthNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 5 lottery result 第四名 {}:{}次 for 北京赛车 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }

//      check if the bet is already done
        if (thirdFourthBetRepository.findByRound(round) != null) {
            logger.info("[Operation - Bet] Already bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
            return false;
        }

        logger.info("[Operation - Bet] Get last bet information for 北京赛车 - {}", PLAYGROUND);
        ThirdFourthBet lastBet = thirdFourthBetRepository.findByRound(round - 1);
        //            结算上次中奖情况
        logger.info("=============== 金额 (for test) ===============");
        money = calculateMoney(money, calculateLastLotteryResult(lastBet, lastLotteryResult));
        logger.info("我的余额:{}", money);
        logger.info("====================================");

        List<Integer> numberBetList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        numberBetList.removeAll(Config.getThirdFourthExcludeNumbers());

        ThirdFourthBet bet = new ThirdFourthBet();
        bet.setRound(round);
//        ============== 策略逻辑 ==============
        if (lastBet == null) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
            } else {
                logger.info("[Operation - Bet] No last bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
                //      投注 冠-五 大
                Collections.shuffle(numberBetList);
                betForThird(bet, chip, numberBetList.subList(0, Math.min(numberBetList.size(), 7)), driver);
                Collections.shuffle(numberBetList);
                betForFourth(bet, chip, numberBetList.subList(0, Math.min(numberBetList.size(), 7)), driver);
                money = calculateMoney(money, -2 * Math.min(numberBetList.size(), 7) * chip);
            }
        } else {
            List<Integer> thirdNumberToBetList = new ArrayList<>(numberBetList);
            List<Integer> fourthNumberToBetList = new ArrayList<>(numberBetList);
            List<Integer> thirdNumberToRemoveList = new ArrayList<>();
            List<Integer> fourthNumberToRemoveList = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                if (thirdNumberCountMap.containsKey(i) && thirdNumberCountMap.get(i).intValue() > 2) {
                    thirdNumberToRemoveList.add(i);
                }
                if (fourthNumberCountMap.containsKey(i) && fourthNumberCountMap.get(i).intValue() > 2) {
                    fourthNumberToRemoveList.add(i);
                }
            }
            thirdNumberToBetList.removeAll(thirdNumberToRemoveList);
            fourthNumberToBetList.removeAll(fourthNumberToRemoveList);

            Collections.shuffle(thirdNumberToBetList);
            Collections.shuffle(fourthNumberToBetList);

            Integer thirdMoneyBet = decideBetChip(lastLotteryResult.getThird(), lastBet.getBetThird(), isPlayTime);
            betForThird(bet, thirdMoneyBet, thirdNumberToBetList.subList(0, Math.min(thirdNumberToBetList.size(), 7)), driver);
            money = calculateMoney(money, -Math.min(thirdNumberToBetList.size(), 7) * thirdMoneyBet);
            Integer fourthMoneyBet = decideBetChip(lastLotteryResult.getFourth(), lastBet.getBetFourth(), isPlayTime);
            betForFourth(bet, fourthMoneyBet, fourthNumberToBetList.subList(0, Math.min(fourthNumberToBetList.size(), 7)), driver);
            money = calculateMoney(money, -Math.min(fourthNumberToBetList.size(), 7) * fourthMoneyBet);

        }

        logger.info("=============== 金额 (for test) ===============");
        logger.info("我的余额:{}", money);
        logger.info("====================================");
        thirdFourthBetRepository.save(bet);
        return true;

    }

    private Integer decideBetChip(Integer winningNumber, RankSingleBet lastRankSingleBet, boolean isPlayTime) {
        Integer chip = Config.getThirdFourthChip();
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
        } else if (lastRankSingleBet.getThird() > 0 && winningNumber == 3) {
            if (!isPlayTime) {
                logger.info("[Operation - Bet] Not in play time.  Do not bet for 北京赛车 - {}", PLAYGROUND);
                return 0;
            }
            return chip;
        } else if (lastRankSingleBet.getFourth() > 0 && winningNumber == 4) {
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
            if (betChip / chip == 1) {
                return chip * Config.getThirdFourthLevelAccList().get(0);
            } else if (betChip / chip == Config.getThirdFourthLevelAccList().get(0)) {
                return chip * Config.getThirdFourthLevelAccList().get(1);
            } else if (betChip / chip == Config.getThirdFourthLevelAccList().get(1)) {
                return chip * Config.getThirdFourthLevelAccList().get(2);
            } else if (betChip / chip == Config.getThirdFourthLevelAccList().get(2)) {
                return chip * Config.getThirdFourthLevelAccList().get(3);
            } else if (betChip / chip == Config.getThirdFourthLevelAccList().get(3)) {
                return chip * Config.getThirdFourthLevelAccList().get(4);
            } else {
                return chip;
            }
        }
    }

    private double calculateLastLotteryResult(ThirdFourthBet lastBet, LotteryResult lotteryResult) {
        if (lastBet == null) {
            return 0;
        }
        double winningMoney = 0;
        ThirdFourthRatio lastRatio = thirdFourthRatioRepository.findByRound(lastBet.getRound());
        if (lotteryResult.getThird() > 5) {
            winningMoney += lastBet.getBetThird().getDa() * lastRatio.getRatioThird().getDa();
        } else {
            winningMoney += lastBet.getBetThird().getXiao() * lastRatio.getRatioThird().getXiao();
        }

        if (lotteryResult.getFourth() > 5) {
            winningMoney += lastBet.getBetFourth().getDa() * lastRatio.getRatioFourth().getDa();
        } else {
            winningMoney += lastBet.getBetFourth().getXiao() * lastRatio.getRatioFourth().getXiao();
        }

        if (lotteryResult.getThird() == 1) {
            winningMoney += lastBet.getBetThird().getFirst() * lastRatio.getRatioThird().getFirst();
        } else if (lotteryResult.getThird() == 2) {
            winningMoney += lastBet.getBetThird().getSecond() * lastRatio.getRatioThird().getSecond();
        } else if (lotteryResult.getThird() == 3) {
            winningMoney += lastBet.getBetThird().getThird() * lastRatio.getRatioThird().getThird();
        } else if (lotteryResult.getThird() == 4) {
            winningMoney += lastBet.getBetThird().getFourth() * lastRatio.getRatioThird().getFourth();
        } else if (lotteryResult.getThird() == 5) {
            winningMoney += lastBet.getBetThird().getFifth() * lastRatio.getRatioThird().getFifth();
        } else if (lotteryResult.getThird() == 6) {
            winningMoney += lastBet.getBetThird().getSixth() * lastRatio.getRatioThird().getSixth();
        } else if (lotteryResult.getThird() == 7) {
            winningMoney += lastBet.getBetThird().getSeventh() * lastRatio.getRatioThird().getSeventh();
        } else if (lotteryResult.getThird() == 8) {
            winningMoney += lastBet.getBetThird().getEighth() * lastRatio.getRatioThird().getEighth();
        } else if (lotteryResult.getThird() == 9) {
            winningMoney += lastBet.getBetThird().getNineth() * lastRatio.getRatioThird().getNineth();
        } else if (lotteryResult.getThird() == 10) {
            winningMoney += lastBet.getBetThird().getTenth() * lastRatio.getRatioThird().getTenth();
        }

        if (lotteryResult.getFourth() == 1) {
            winningMoney += lastBet.getBetFourth().getFirst() * lastRatio.getRatioFourth().getFirst();
        } else if (lotteryResult.getFourth() == 2) {
            winningMoney += lastBet.getBetFourth().getSecond() * lastRatio.getRatioFourth().getSecond();
        } else if (lotteryResult.getFourth() == 3) {
            winningMoney += lastBet.getBetFourth().getThird() * lastRatio.getRatioFourth().getThird();
        } else if (lotteryResult.getFourth() == 4) {
            winningMoney += lastBet.getBetFourth().getFourth() * lastRatio.getRatioFourth().getFourth();
        } else if (lotteryResult.getFourth() == 5) {
            winningMoney += lastBet.getBetFourth().getFifth() * lastRatio.getRatioFourth().getFifth();
        } else if (lotteryResult.getFourth() == 6) {
            winningMoney += lastBet.getBetFourth().getSixth() * lastRatio.getRatioFourth().getSixth();
        } else if (lotteryResult.getFourth() == 7) {
            winningMoney += lastBet.getBetFourth().getSeventh() * lastRatio.getRatioFourth().getSeventh();
        } else if (lotteryResult.getFourth() == 8) {
            winningMoney += lastBet.getBetFourth().getEighth() * lastRatio.getRatioFourth().getEighth();
        } else if (lotteryResult.getFourth() == 9) {
            winningMoney += lastBet.getBetFourth().getNineth() * lastRatio.getRatioFourth().getNineth();
        } else if (lotteryResult.getFourth() == 10) {
            winningMoney += lastBet.getBetFourth().getTenth() * lastRatio.getRatioFourth().getTenth();
        }

        logger.info("[Operation - Summary] Winning money for round {} is {}", lastBet.getRound(), winningMoney);

        return winningMoney;
    }

    private void markNumber(LotteryResult lotteryResult) {
        if (thirdNumberCountMap.containsKey(lotteryResult.getThird())) {
            thirdNumberCountMap.get(lotteryResult.getThird()).getAndIncrement();
        } else {
            thirdNumberCountMap.put(lotteryResult.getThird(), new AtomicInteger(1));
        }
        if (fourthNumberCountMap.containsKey(lotteryResult.getFourth())) {
            fourthNumberCountMap.get(lotteryResult.getFourth()).getAndIncrement();
        } else {
            fourthNumberCountMap.put(lotteryResult.getFourth(), new AtomicInteger(1));
        }
    }

    private double calculateMoney(double money, double value) {
        return money + value;
    }

    private void betForThird(ThirdFourthBet bet, Integer chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "第三名", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetThird(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetThird();
        if (singleBet.getFirst() > 0) {
            String name = getInputName(1, 3);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String name = getInputName(2, 3);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String name = getInputName(3, 3);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String name = getInputName(4, 3);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String name = getInputName(5, 3);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String name = getInputName(6, 3);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String name = getInputName(7, 3);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String name = getInputName(8, 3);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String name = getInputName(9, 3);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String name = getInputName(10, 3);
            sendKeys(driver, name, String.valueOf(chip));
        }
    }

    private void betForFourth(ThirdFourthBet bet, Integer chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "第四名", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetFourth(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetFourth();
        if (singleBet.getFirst() > 0) {
            String name = getInputName(1, 4);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String name = getInputName(2, 4);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String name = getInputName(3, 4);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String name = getInputName(4, 4);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String name = getInputName(5, 4);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String name = getInputName(6, 4);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String name = getInputName(7, 4);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String name = getInputName(8, 4);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String name = getInputName(9, 4);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String name = getInputName(10, 4);
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
