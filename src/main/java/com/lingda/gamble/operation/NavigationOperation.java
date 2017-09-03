package com.lingda.gamble.operation;

import com.lingda.gamble.util.DriverUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NavigationOperation {

    private static final Logger logger = LoggerFactory.getLogger(NavigationOperation.class);

    @Value("${gamble.website}")
    private String website;

    public void doNavigate(WebDriver driver) {
        driver.get(String.format("%s/op.php?op=member", website));
        DriverUtils.returnOnFindingFrame(driver, "topFrame");
        WebElement klsfBtn = DriverUtils.returnOnFindingElementContainingValue(driver, By.tagName("a"), "快乐十分");
        klsfBtn.click();
        WebElement bjscBtn = DriverUtils.returnOnFindingElementContainingValue(driver, By.tagName("a"), "北京赛车");
        bjscBtn.click();
        logger.info("[Operation - Navigate] Navigate to 北京赛车");
    }
}
