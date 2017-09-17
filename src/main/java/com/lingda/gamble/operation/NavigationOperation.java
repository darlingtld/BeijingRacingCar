package com.lingda.gamble.operation;

import com.lingda.gamble.util.DriverUtils;
import com.lingda.gamble.util.Store;
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
        logger.info("[Operation - Navigate] Get username of 北京赛车");
        DriverUtils.returnOnFindingFrame(driver, "leftFrame");
        WebElement accountEle = DriverUtils.returnOnFindingElement(driver, By.className("row1")).findElements(By.tagName("td")).get(1);
        logger.info("[Operation - Navigate] Username is {}", accountEle.getText());
        Store.setAccountName(accountEle.getText());

        driver.switchTo().parentFrame();

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
    }
}
