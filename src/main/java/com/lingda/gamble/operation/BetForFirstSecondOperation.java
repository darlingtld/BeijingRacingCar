package com.lingda.gamble.operation;

import com.lingda.gamble.model.FirstSecondBet;
import com.lingda.gamble.model.FirstSecondRatio;
import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleBet;
import com.lingda.gamble.repository.FirstSecondBetRepository;
import com.lingda.gamble.repository.FirstSecondRatioRepository;
import com.lingda.gamble.repository.LotteryResultRepository;
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

//北京赛车 冠亚军下注
@Component
public class BetForFirstSecondOperation {

    private static final Logger logger = LoggerFactory.getLogger(BetForFirstSecondOperation.class);

    private static final String PLAYGROUND = "冠亚军";

    @Value("${gamble.bet.money}")
    private double money;

    @Value("${gamble.bet.chip}")
    private double chip;

    @Value("${gamble.bet.smp.level}")
    private int level;

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

    public boolean doBet(WebDriver driver, Integer round) throws InterruptedException {
        if (round == null) {
            logger.info("[Operation - Bet] 当前无法下注");
            return false;
        }
        firstNumberCountMap.clear();
        secondNumberCountMap.clear();

        logger.info("[Operation - Bet] Bet for 北京赛车 - {}", PLAYGROUND);

        logger.info("[Operation - Bet] Get fetched ratio for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
        FirstSecondRatio ratio = firstSecondRatioRepository.findByRound(round);
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
//        take the last 5 lottery result into consideration
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
        logger.info("[Operation - Bet] Last 5 lottery result for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
        for (Map.Entry<Integer, AtomicInteger> entry : firstNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 5 lottery result 冠军 {}:{}次 for 北京赛车 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }
        for (Map.Entry<Integer, AtomicInteger> entry : secondNumberCountMap.entrySet()) {
            logger.info("[Operation - Bet] Last 5 lottery result 亚军 {}:{}次 for 北京赛车 - {} - 期数 {}", entry.getKey(), entry.getValue().intValue(), PLAYGROUND, round - 1, entry.getKey(), entry.getValue().intValue());
        }

//      check if the bet is already done
        if (firstSecondBetRepository.findByRound(round) != null) {
            logger.info("[Operation - Bet] Already bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round);
            return false;
        }

        logger.info("[Operation - Bet] Get last bet information for 北京赛车 - {}", PLAYGROUND);
        FirstSecondBet lastBet = firstSecondBetRepository.findByRound(round - 1);
        //            结算上次中奖情况
        logger.info("=============== 金额 ===============");
        money = calculateMoney(money, calculateLastLotteryResult(lastBet, lastLotteryResult));
        logger.info("我的余额:{}", money);
        logger.info("====================================");

        FirstSecondBet bet = new FirstSecondBet();
        bet.setRound(round);
//        ============== 策略逻辑 ==============
        if (lastBet == null) {
            logger.info("[Operation - Bet] No last bet for 北京赛车 - {} - 期数 {}", PLAYGROUND, round - 1);
            //      投注 冠-五 大

            betForFirst(bet, chip, Arrays.asList(1, 2, 3, 4, 5, 6, 7), driver);
            betForSecond(bet, chip, Arrays.asList(1, 2, 3, 4, 5, 6, 7), driver);

            money = calculateMoney(money, -14 * chip);
        } else {
            List<Integer> firstNumberToBetList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            List<Integer> secondNumberToBetList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            List<Integer> firstNumberToRemoveList = new ArrayList<>();
            List<Integer> secondNumberToRemoveList = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                if (firstNumberCountMap.containsKey(i) && firstNumberCountMap.get(i).intValue() > 1) {
                    firstNumberToRemoveList.add(i);
                }
                if (secondNumberCountMap.containsKey(i) && secondNumberCountMap.get(i).intValue() > 1) {
                    secondNumberToRemoveList.add(i);
                }
            }
            firstNumberToBetList.removeAll(firstNumberToRemoveList);
            secondNumberToBetList.removeAll(secondNumberToRemoveList);

            Collections.shuffle(firstNumberToBetList);
            Collections.shuffle(secondNumberToBetList);

            Double firstMoneyBet = decideBetChip(lastLotteryResult.getFirst(), lastBet.getBetFirst());
            betForFirst(bet, firstMoneyBet, firstNumberToBetList.subList(0, 7), driver);
            money = calculateMoney(money, -7 * firstMoneyBet);
            Double secondMoneyBet = decideBetChip(lastLotteryResult.getSecond(), lastBet.getBetSecond());
            betForSecond(bet, secondMoneyBet, secondNumberToBetList.subList(0, 7), driver);
            money = calculateMoney(money, -7 * secondMoneyBet);

        }

        logger.info("=============== 金额 ===============");
        logger.info("我的余额:{}", money);
        logger.info("====================================");
        firstSecondBetRepository.save(bet);
        return true;

    }

    private Double decideBetChip(Integer winningNumber, RankSingleBet lastRankSingleBet) {
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
            return chip;
        } else if (lastRankSingleBet.getSecond() > 0 && winningNumber == 2) {
            return chip;
        } else if (lastRankSingleBet.getThird() > 0 && winningNumber == 3) {
            return chip;
        } else if (lastRankSingleBet.getFourth() > 0 && winningNumber == 4) {
            return chip;
        } else if (lastRankSingleBet.getFifth() > 0 && winningNumber == 5) {
            return chip;
        } else if (lastRankSingleBet.getSixth() > 0 && winningNumber == 6) {
            return chip;
        } else if (lastRankSingleBet.getSeventh() > 0 && winningNumber == 7) {
            return chip;
        } else if (lastRankSingleBet.getEighth() > 0 && winningNumber == 8) {
            return chip;
        } else if (lastRankSingleBet.getNineth() > 0 && winningNumber == 9) {
            return chip;
        } else if (lastRankSingleBet.getTenth() > 0 && winningNumber == 10) {
            return chip;
        } else {
//            if (betChip / chip == 1) {
//                return chip * 5;
//            } else if (betChip / chip == 5) {
//                return chip * 17;
//            } else if (betChip / chip == 17) {
//                return chip * 57;
//            } else if (betChip / chip == 57) {
//                return chip * 193;
//            } else if (betChip / chip == 193) {
//                return chip * 572;
//            } else {
//                return chip;
//            }
            if (betChip / chip == 1) {
                return chip * 2;
            } else if (betChip / chip == 2) {
                return chip * 4;
            } else if (betChip / chip == 4) {
                return chip * 8;
            } else if (betChip / chip == 8) {
                return chip * 16;
            } else if (betChip / chip == 16) {
                return chip * 32;
            } else if (betChip / chip == 32) {
                return chip * 64;
            } else {
                return chip;
            }
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

    private void betForFirst(FirstSecondBet bet, Double chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "冠军", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetFirst(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetFirst();
        if (singleBet.getFirst() > 0) {
            String name = getInputName(1, 1);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String name = getInputName(2, 1);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String name = getInputName(3, 1);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String name = getInputName(4, 1);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String name = getInputName(5, 1);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String name = getInputName(6, 1);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String name = getInputName(7, 1);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String name = getInputName(8, 1);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String name = getInputName(9, 1);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String name = getInputName(10, 1);
            sendKeys(driver, name, String.valueOf(chip));
        }
    }

    private void betForSecond(FirstSecondBet bet, Double chip, List<Integer> numbers, WebDriver driver) {
        logger.info("[Operation - Bet] Bet [{} {}] for 北京赛车 - {} - 期数 {} - 金额 - {}", "亚军", numbers, PLAYGROUND, bet.getRound(), chip);
        bet.setBetSecond(generateSingleBet(numbers, chip));
        RankSingleBet singleBet = bet.getBetSecond();
        if (singleBet.getFirst() > 0) {
            String name = getInputName(1, 2);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSecond() > 0) {
            String name = getInputName(2, 2);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getThird() > 0) {
            String name = getInputName(3, 2);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFourth() > 0) {
            String name = getInputName(4, 2);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getFifth() > 0) {
            String name = getInputName(5, 2);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSixth() > 0) {
            String name = getInputName(6, 2);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getSeventh() > 0) {
            String name = getInputName(7, 2);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getEighth() > 0) {
            String name = getInputName(8, 2);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getNineth() > 0) {
            String name = getInputName(9, 2);
            sendKeys(driver, name, String.valueOf(chip));
        }
        if (singleBet.getTenth() > 0) {
            String name = getInputName(10, 2);
            sendKeys(driver, name, String.valueOf(chip));
        }
    }

    private RankSingleBet generateSingleBet(List<Integer> numberList, Double chip) {
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
