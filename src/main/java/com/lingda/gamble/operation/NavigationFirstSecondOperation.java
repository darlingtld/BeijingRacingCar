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
public class NavigationFirstSecondOperation {

    private static final Logger logger = LoggerFactory.getLogger(NavigationFirstSecondOperation.class);

    @Value("${gamble.website}")
    private String website;

    public void doNavigate(WebDriver driver) throws InterruptedException {
        WebElement smpBtn = DriverUtils.returnOnFindingElementContainsValue(driver, By.tagName("a"), "冠、亞軍 組合");
        smpBtn.click();
        Thread.sleep(500);
        logger.info("[Operation - Navigate] Navigate to 冠、亞軍 組合");
    }
}
