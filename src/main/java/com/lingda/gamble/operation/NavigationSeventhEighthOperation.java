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
public class NavigationSeventhEighthOperation {

    private static final Logger logger = LoggerFactory.getLogger(NavigationSeventhEighthOperation.class);

    @Value("${gamble.website}")
    private String website;

    public void doNavigate(WebDriver driver) throws InterruptedException {
        WebElement navBtn = DriverUtils.returnOnFindingElementContainsValue(driver, By.tagName("a"), "七、八、九、十名");
        navBtn.click();
        Thread.sleep(500);
        logger.info("[Operation - Navigate] Navigate to 七、八、九、十名");
    }
}
