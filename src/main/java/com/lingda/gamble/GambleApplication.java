package com.lingda.gamble;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class GambleApplication {

    private static final Logger logger = LoggerFactory.getLogger(GambleApplication.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void postConstruct() throws IOException {
        String chromeDriverPath = resourceLoader.getResource("classpath:chromedriver").getFile().getPath();
        logger.info("Chrome driver path is {}", chromeDriverPath);
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    public static void main(String[] args) {
        SpringApplication.run(GambleApplication.class, args);
    }
}
