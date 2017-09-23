package com.lingda.gamble.operation;

import com.lingda.gamble.util.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//北京赛车 确认下注
@Component
public class FinishBetOperation {

    private static final Logger logger = LoggerFactory.getLogger(FinishBetOperation.class);

    @Value("${gamble.bet.mimic}")
    private boolean isMimic;

    public void doFinish(WebDriver driver, String playground) throws InterruptedException {
        logger.info("[Operation - Finish Bet] 确认下注");
        DriverUtils.returnOnFindingElementEqualsType(driver, By.tagName("input"), "submit").click();
        DriverUtils.returnOnFinishLoadingGivenFinishingIndicator(driver, "下注的是");
        try {
            Thread.sleep(6000);
            if (isMimic) {
                logger.info("[Operation - Finish Bet] 模拟下注");
                DriverUtils.returnOnFindingElementEqualsName(driver, By.tagName("input"), "reset").click();
            } else {
                logger.info("[Operation - Finish Bet] 真实下注");
                DriverUtils.returnOnFindingElementEqualsName(driver, By.tagName("input"), "submit").click();
            }
            logger.info("[Operation - Finish Bet] 下注成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("[Operation - Finish Bet] 下注失败");
            DriverUtils.returnOnFindingElementEqualsValue(driver, By.tagName("input"), "回上一页").click();
        } finally {
            DriverUtils.returnOnFinishLoadingGivenFinishingIndicator(driver, playground);
        }
    }
}
