package com.lingda.gamble.operation;

import com.lingda.gamble.util.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NavigationThirdFourthOperation {

    private static final Logger logger = LoggerFactory.getLogger(NavigationThirdFourthOperation.class);

    @Value("${gamble.website}")
    private String website;

    public void doNavigate(WebDriver driver) throws InterruptedException {
        driver.switchTo().parentFrame().switchTo().frame("topFrame");
        WebElement smpBtn = DriverUtils.returnOnFindingElementContainsValue(driver, By.tagName("a"), "三.四名");
        smpBtn.click();
        Thread.sleep(500);
        logger.info("[Operation - Navigate] Navigate to 三.四名");


    }
}
