package com.lingda.gamble.controller;

import com.lingda.gamble.model.ConfigDTO;
import com.lingda.gamble.param.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/config")
public class ConfigController {

    private static Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ConfigDTO readConfig() {
        logger.info("Read config={}", new ConfigDTO());
        return new ConfigDTO();
    }

    @RequestMapping(value = "email", method = RequestMethod.POST)
    public void setNotificationEmail(@RequestParam("email") String email) {
        logger.info("Set notification email={}", email);
        Config.setEmail(email);
    }

    @RequestMapping(value = "lost_threshold", method = RequestMethod.POST)
    public void setLostThreshold(@RequestParam("lost_threshold") Integer threshold) {
        logger.info("Set lost threshold={}", threshold);
        Config.setLostThreshold(threshold);
    }

}
