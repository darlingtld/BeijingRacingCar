package com.lingda.gamble.operation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RatioFetchingOperationTest {

    @Autowired
    private LoginOperation loginOperation;

    @Autowired
    private NavigationOperation navigationOperation;

    @Autowired
    private RatioFetchingForSMPOperation ratioFetchingForSMPOperation;

    @Test
    public void shouldFetchRatio() throws InterruptedException {
        WebDriver driver = BrowserDriver.getDriver();
        loginOperation.doLogin(driver);
        navigationOperation.doNavigate(driver);
        ratioFetchingForSMPOperation.doFetchRatio(driver);
    }
}
