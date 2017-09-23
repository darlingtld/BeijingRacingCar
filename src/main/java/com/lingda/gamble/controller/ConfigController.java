package com.lingda.gamble.controller;

import com.lingda.gamble.model.ConfigDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

}
