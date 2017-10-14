package com.lingda.gamble.operation;

import com.lingda.gamble.model.NinethTenthRatio;
import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleRatio;
import com.lingda.gamble.model.WinLostMoney;
import com.lingda.gamble.param.Config;
import com.lingda.gamble.repository.NinethTenthRatioRepository;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.repository.WinLostMoneyRepository;
import com.lingda.gamble.util.DriverUtils;
import com.lingda.gamble.util.Store;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//抓取北京赛车 九十赔率
@Component
public class RatioFetchingForNinethTenthOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForNinethTenthOperation.class);

    private static final String PLAYGROUND = "九十名";

    private final NinethTenthRatioRepository ninethTenthRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    private final WinLostMoneyRepository winLostMoneyRepository;

    @Autowired
    public RatioFetchingForNinethTenthOperation(NinethTenthRatioRepository ninethTenthRatioRepository, LotteryResultRepository lotteryResultRepository, WinLostMoneyRepository winLostMoneyRepository) {
        this.ninethTenthRatioRepository = ninethTenthRatioRepository;
        this.lotteryResultRepository = lotteryResultRepository;
        this.winLostMoneyRepository = winLostMoneyRepository;
    }

    public Integer doFetchRatio(WebDriver driver) throws InterruptedException {
        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {}", PLAYGROUND);

        logger.info("[Operation - FetchRatio] Fetch lottery result for 北京赛车 - {} - 期数", PLAYGROUND);
//        获取之前开奖信息
        LotteryResult lotteryResult = new LotteryResult();
        WebElement round = DriverUtils.returnOnFindingElement(driver, By.id("newPhase"));
        lotteryResult.setRound(Integer.parseInt(round.getText()));
        WebElement lotteryResultEle = DriverUtils.returnOnFindingElement(driver, By.id("prevBall"));
        List<WebElement> spanList = lotteryResultEle.findElements(By.tagName("span"));

        lotteryResult.setFirst(Integer.parseInt(spanList.get(0).getAttribute("class").substring(3)));
        lotteryResult.setSecond(Integer.parseInt(spanList.get(1).getAttribute("class").substring(3)));
        lotteryResult.setThird(Integer.parseInt(spanList.get(2).getAttribute("class").substring(3)));
        lotteryResult.setFourth(Integer.parseInt(spanList.get(3).getAttribute("class").substring(3)));
        lotteryResult.setFifth(Integer.parseInt(spanList.get(4).getAttribute("class").substring(3)));
        lotteryResult.setSixth(Integer.parseInt(spanList.get(5).getAttribute("class").substring(3)));
        lotteryResult.setSeventh(Integer.parseInt(spanList.get(6).getAttribute("class").substring(3)));
        lotteryResult.setEighth(Integer.parseInt(spanList.get(7).getAttribute("class").substring(3)));
        lotteryResult.setNineth(Integer.parseInt(spanList.get(8).getAttribute("class").substring(3)));
        lotteryResult.setTenth(Integer.parseInt(spanList.get(9).getAttribute("class").substring(3)));
        logger.info(lotteryResult.toString());
        if (lotteryResultRepository.findByRound(lotteryResult.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save lotteryResult to DB for 北京赛车 - {} - 开奖信息", PLAYGROUND);
            lotteryResultRepository.save(lotteryResult);
        }

        logger.info("[Operation - FetchRatio] Fetch round for 北京赛车 - {} - 期数", PLAYGROUND);
//        获取当前下注期数
        WebElement element = DriverUtils.returnOnFindingElement(driver, By.id("NowJq"));

        NinethTenthRatio ratio = new NinethTenthRatio();
        ratio.setRound(Integer.parseInt(element.getText()));

        //        获取当前输赢情况
        WebElement todayWinLost = DriverUtils.returnOnFindingElement(driver, By.id("profit"));
        Double todayWinLostMoney = Double.parseDouble(todayWinLost.getText());
        logger.info("[Operation - FetchRatio] Today win/lost for 北京赛车 - {} - {}", PLAYGROUND, todayWinLostMoney);
        WinLostMoney winLostMoney = new WinLostMoney();
        winLostMoney.setAccountName(Store.getAccountName());
        winLostMoney.setRound(ratio.getRound());
        winLostMoney.setWinLostMoney(todayWinLostMoney);
        if (winLostMoneyRepository.findByRoundAndAccountName(winLostMoney.getRound(), winLostMoney.getAccountName()) == null) {
            logger.info("[Operation - FetchRatio] Save today win/lost for 北京赛车 - {}", PLAYGROUND);
            winLostMoneyRepository.save(winLostMoney);
        }

        WebElement gameBox = DriverUtils.returnOnFindingElement(driver, By.id("gameBox"));
        RankSingleRatio ninethRatio = new RankSingleRatio();
        WebElement ratioTest = gameBox.findElement(By.id("odds_30_123"));
        if (ratioTest.getText().equals("-")) {
//                正在开奖 或者 正在刷新
            return null;
        }

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.cssSelector("div.game_box[data-title=第九名]"));
        try {
            ninethRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("odds_30_123")).getText()));
            ninethRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("odds_30_124")).getText()));
            ninethRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("odds_30_125")).getText()));
            ninethRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("odds_30_126")).getText()));
            ninethRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("odds_30_127")).getText()));
            ninethRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("odds_30_128")).getText()));
            ninethRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("odds_30_129")).getText()));
            ninethRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("odds_30_130")).getText()));
            ninethRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("odds_30_131")).getText()));
            ninethRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("odds_30_132")).getText()));
            ninethRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("odds_32_135")).getText()));
            ninethRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("odds_32_136")).getText()));
            ninethRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("odds_31_133")).getText()));
            ninethRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("odds_31_134")).getText()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Can not find lottery result");
        }
        ratio.setRatioNineth(ninethRatio);

        ratioTable = DriverUtils.returnOnFindingElement(driver, By.cssSelector("div.game_box[data-title=第十名]"));
        RankSingleRatio tenthRatio = new RankSingleRatio();
        tenthRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("odds_33_137")).getText()));
        tenthRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("odds_33_138")).getText()));
        tenthRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("odds_33_139")).getText()));
        tenthRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("odds_33_140")).getText()));
        tenthRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("odds_33_141")).getText()));
        tenthRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("odds_33_142")).getText()));
        tenthRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("odds_33_143")).getText()));
        tenthRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("odds_33_144")).getText()));
        tenthRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("odds_33_145")).getText()));
        tenthRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("odds_33_145")).getText()));
        tenthRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("odds_35_149")).getText()));
        tenthRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("odds_35_150")).getText()));
        tenthRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("odds_34_147")).getText()));
        tenthRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("odds_34_148")).getText()));
        ratio.setRatioTenth(tenthRatio);

        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {} - 赔率", PLAYGROUND);
        logger.info(ratio.toString());
        if (ninethTenthRatioRepository.findByRound(ratio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 北京赛车 - {} - 赔率", PLAYGROUND);
            ninethTenthRatioRepository.save(ratio);
        }
        if (winLostMoney.getWinLostMoney() + Config.getLostThreshold() < 0) {
            throw new RuntimeException(String.format("!!!!!!!!! Blast %s !!!!!!!!!", winLostMoney.getWinLostMoney()));
        }
        if (winLostMoney.getWinLostMoney() - Config.getWinThreshold() > 0) {
            throw new RuntimeException(String.format("!!!!!!!!! Wow %s !!!!!!!!!", winLostMoney.getWinLostMoney()));
        }
        return ratio.getRound();
    }
}
