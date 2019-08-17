package com.lingda.gamble.operation;

import com.lingda.gamble.util.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NavigationSMPOperation {

    private static final Logger logger = LoggerFactory.getLogger(NavigationSMPOperation.class);

    public void doNavigate(WebDriver driver) throws InterruptedException {
        WebElement smpBtn = DriverUtils.returnOnFindingElementEqualsValue(driver, By.tagName("a"), "两面盘");
        smpBtn.click();
        Thread.sleep(500);
        logger.info("[Operation - Navigate] Navigate to 两面盘");


    }
}
