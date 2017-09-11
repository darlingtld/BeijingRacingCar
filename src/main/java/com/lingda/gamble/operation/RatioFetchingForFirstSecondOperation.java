package com.lingda.gamble.operation;

import com.lingda.gamble.model.FirstSecondRatio;
import com.lingda.gamble.model.LotteryResult;
import com.lingda.gamble.model.RankSingleRatio;
import com.lingda.gamble.model.SMPRatio;
import com.lingda.gamble.model.SMPSingleRatio;
import com.lingda.gamble.repository.FirstSecondRatioRepository;
import com.lingda.gamble.repository.LotteryResultRepository;
import com.lingda.gamble.repository.SMPRatioRepository;
import com.lingda.gamble.util.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//抓取北京赛车 冠亚军赔率
@Component
public class RatioFetchingForFirstSecondOperation {

    private static final Logger logger = LoggerFactory.getLogger(RatioFetchingForFirstSecondOperation.class);

    private static final Pattern roundPattern = Pattern.compile("^([0-9]+)\\s+期");

    private static final String PLAYGROUND = "冠亚军";

    private final FirstSecondRatioRepository firstSecondRatioRepository;

    private final LotteryResultRepository lotteryResultRepository;

    @Autowired
    public RatioFetchingForFirstSecondOperation(FirstSecondRatioRepository firstSecondRatioRepository, LotteryResultRepository lotteryResultRepository) {
        this.firstSecondRatioRepository = firstSecondRatioRepository;
        this.lotteryResultRepository = lotteryResultRepository;
    }

    public Integer doFetchRatio(WebDriver driver) throws InterruptedException {
        driver.switchTo().parentFrame();
        DriverUtils.returnOnFindingFrame(driver, "mainFrame");
        Thread.sleep(1000);

        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {}", PLAYGROUND);

        logger.info("[Operation - FetchRatio] Fetch lottery result for 北京赛车 - {} - 期数", PLAYGROUND);
//        获取之前开奖信息
        WebElement lotteryResultEle = DriverUtils.returnOnFindingElement(driver, By.id("betmyOpenRoundData"));
        List<WebElement> spanList = lotteryResultEle.findElements(By.tagName("span"));
        LotteryResult lotteryResult = new LotteryResult();
        Matcher m = roundPattern.matcher(spanList.get(0).getText());
        if (m.find()) {
            lotteryResult.setRound(Integer.parseInt(m.group(1)));
        } else {
            throw new RuntimeException("Can not find lottery result");
        }
        lotteryResult.setFirst(Integer.parseInt(spanList.get(1).getText()));
        lotteryResult.setSecond(Integer.parseInt(spanList.get(2).getText()));
        lotteryResult.setThird(Integer.parseInt(spanList.get(3).getText()));
        lotteryResult.setFourth(Integer.parseInt(spanList.get(4).getText()));
        lotteryResult.setFifth(Integer.parseInt(spanList.get(5).getText()));
        lotteryResult.setSixth(Integer.parseInt(spanList.get(6).getText()));
        lotteryResult.setSeventh(Integer.parseInt(spanList.get(7).getText()));
        lotteryResult.setEighth(Integer.parseInt(spanList.get(8).getText()));
        lotteryResult.setNineth(Integer.parseInt(spanList.get(9).getText()));
        lotteryResult.setTenth(Integer.parseInt(spanList.get(10).getText()));
        logger.info(lotteryResult.toString());
        if (lotteryResultRepository.findByRound(lotteryResult.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save lotteryResult to DB for 北京赛车 - {} - 开奖信息", PLAYGROUND);
            lotteryResultRepository.save(lotteryResult);
        }

        logger.info("[Operation - FetchRatio] Fetch round for 北京赛车 - {} - 期数", PLAYGROUND);
//        获取当前下注期数
        WebElement element = DriverUtils.returnOnFindingElementContainsValue(driver, By.tagName("td"), "北京赛车");
        FirstSecondRatio ratio = new FirstSecondRatio();
        ratio.setRound(Integer.parseInt(element.findElements(By.tagName("span")).get(0).getText()));

        WebElement ratioTable = DriverUtils.returnOnFindingElement(driver, By.id("tblMy3DArea"));
        RankSingleRatio firstRatio = new RankSingleRatio();
        firstRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("setR_1_0_01")).getText()));
        firstRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("setR_1_0_02")).getText()));
        firstRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("setR_1_0_03")).getText()));
        firstRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("setR_1_0_04")).getText()));
        firstRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("setR_1_0_05")).getText()));
        firstRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("setR_1_0_06")).getText()));
        firstRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("setR_1_0_07")).getText()));
        firstRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("setR_1_0_08")).getText()));
        firstRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("setR_1_0_09")).getText()));
        firstRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("setR_1_0_10")).getText()));
        firstRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("setR_1_1_1")).getText()));
        firstRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("setR_1_1_2")).getText()));
        firstRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("setR_1_2_1")).getText()));
        firstRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("setR_1_2_2")).getText()));
        firstRatio.setLon(Double.parseDouble(ratioTable.findElement(By.id("setR_1_6_1")).getText()));
        firstRatio.setHu(Double.parseDouble(ratioTable.findElement(By.id("setR_1_6_2")).getText()));
        ratio.setRatioFirst(firstRatio);

        RankSingleRatio secondRatio = new RankSingleRatio();
        secondRatio.setFirst(Double.parseDouble(ratioTable.findElement(By.id("setR_2_0_01")).getText()));
        secondRatio.setSecond(Double.parseDouble(ratioTable.findElement(By.id("setR_2_0_02")).getText()));
        secondRatio.setThird(Double.parseDouble(ratioTable.findElement(By.id("setR_2_0_03")).getText()));
        secondRatio.setFourth(Double.parseDouble(ratioTable.findElement(By.id("setR_2_0_04")).getText()));
        secondRatio.setFifth(Double.parseDouble(ratioTable.findElement(By.id("setR_2_0_05")).getText()));
        secondRatio.setSixth(Double.parseDouble(ratioTable.findElement(By.id("setR_2_0_06")).getText()));
        secondRatio.setSeventh(Double.parseDouble(ratioTable.findElement(By.id("setR_2_0_07")).getText()));
        secondRatio.setEighth(Double.parseDouble(ratioTable.findElement(By.id("setR_2_0_08")).getText()));
        secondRatio.setNineth(Double.parseDouble(ratioTable.findElement(By.id("setR_2_0_09")).getText()));
        secondRatio.setTenth(Double.parseDouble(ratioTable.findElement(By.id("setR_2_0_10")).getText()));
        secondRatio.setDan(Double.parseDouble(ratioTable.findElement(By.id("setR_2_1_1")).getText()));
        secondRatio.setShuang(Double.parseDouble(ratioTable.findElement(By.id("setR_2_1_2")).getText()));
        secondRatio.setDa(Double.parseDouble(ratioTable.findElement(By.id("setR_2_2_1")).getText()));
        secondRatio.setXiao(Double.parseDouble(ratioTable.findElement(By.id("setR_2_2_2")).getText()));
        secondRatio.setLon(Double.parseDouble(ratioTable.findElement(By.id("setR_2_6_1")).getText()));
        secondRatio.setHu(Double.parseDouble(ratioTable.findElement(By.id("setR_2_6_2")).getText()));
        ratio.setRatioSecond(secondRatio);

        logger.info("[Operation - FetchRatio] Fetch ratio for 北京赛车 - {} - 赔率", PLAYGROUND);
        logger.info(ratio.toString());
        if (firstSecondRatioRepository.findByRound(ratio.getRound()) == null) {
            logger.info("[Operation - FetchRatio] Save ratio to DB for 北京赛车 - {} - 赔率", PLAYGROUND);
            firstSecondRatioRepository.save(ratio);
        }
        return ratio.getRound();
    }
}
