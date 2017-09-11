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
public class NavigationOperation {

    private static final Logger logger = LoggerFactory.getLogger(NavigationOperation.class);

    @Value("${gamble.website}")
    private String website;

    public void doNavigate(WebDriver driver) throws InterruptedException {
        driver.get(String.format("%s/op.php?op=member", website));
        DriverUtils.returnOnFindingFrame(driver, "topFrame");
        WebElement klsfBtn = DriverUtils.returnOnFindingElementEqualsValue(driver, By.tagName("a"), "快乐十分");
        klsfBtn.click();
        Thread.sleep(500);
        WebElement bjscBtn = DriverUtils.returnOnFindingElementEqualsValue(driver, By.tagName("a"), "北京赛车");
        bjscBtn.click();
        Thread.sleep(500);
        logger.info("[Operation - Navigate] Navigate to 北京赛车");
//        make sure 北京赛车 is loaded
        driver.switchTo().parentFrame();
        DriverUtils.returnOnFindingFrame(driver, "mainFrame");
        DriverUtils.returnOnFindingElementContainsValue(driver, By.tagName("td"), "北京赛车");
//        driver.switchTo().parentFrame().switchTo().frame("topFrame");
//        WebElement smpBtn = DriverUtils.returnOnFindingElementEqualsValue(driver, By.tagName("a"), "双面盘");
//        smpBtn.click();
//        Thread.sleep(500);
//        logger.info("[Operation - Navigate] Navigate to 双面盘");


    }
}
