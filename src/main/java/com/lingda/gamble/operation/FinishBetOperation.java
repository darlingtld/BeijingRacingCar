package com.lingda.gamble.operation;

import com.lingda.gamble.util.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

//北京赛车 确认下注
@Component
public class FinishBetOperation {

    private static final Logger logger = LoggerFactory.getLogger(FinishBetOperation.class);

    @Value("${gamble.bet.mimic}")
    private boolean isMimic;

    public void doFinish(WebDriver driver, String playground) throws InterruptedException {
        logger.info("[Operation - Finish Bet] 确认下注");
        driver = driver.switchTo().parentFrame();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, Math.max(document.documentElement.scrollHeight, document.body.scrollHeight, document.documentElement.clientHeight));");
//        js.executeScript("window.scrollBy(0, 1500);","");
        DriverUtils.returnOnFindingFrame(driver, "mainIframe");
        boolean retrySubmit = true;
        while (retrySubmit) {
            try {
                WebElement submitBtn = DriverUtils.returnOnFindingElement(driver, By.id("gameSubmit"));
                Thread.sleep(1000);
                submitBtn.click();
                retrySubmit = false;
            } catch (Exception e) {
                logger.error("[Operation - Finish Bet] 确认下注 error {}", e.getMessage());
            }
        }
        DriverUtils.returnOnFinishLoadingGivenFinishingIndicator(driver, "確認下單");
        WebElement form = DriverUtils.returnOnFindingElement(driver, By.id("myWarp"));
        try {
            if (isMimic) {
                Thread.sleep(4000);
                logger.info("[Operation - Finish Bet] 模拟下注");
                List<WebElement> aList = form.findElements(By.tagName("a"));
                for (WebElement a : aList) {
                    if (a.getText().equals("取消")) {
                        a.click();
                        break;
                    }
                }
            } else {
                logger.info("[Operation - Finish Bet] 真实下注");
                List<WebElement> aList = form.findElements(By.tagName("a"));
                for (WebElement a : aList) {
                    if (a.getText().equals("提交")) {
                        a.click();
                        break;
                    }
                }
            }
            logger.info("[Operation - Finish Bet] 下注成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("[Operation - Finish Bet] 下注失败");
            List<WebElement> aList = form.findElements(By.tagName("a"));
            for (WebElement a : aList) {
                if (a.getText().equals("取消")) {
                    a.click();
                    break;
                }
            }
        } finally {
            DriverUtils.returnOnFinishLoadingGivenFinishingIndicator(driver, playground);
        }
    }
}
