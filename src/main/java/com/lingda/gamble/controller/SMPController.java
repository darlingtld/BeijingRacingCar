package com.lingda.gamble.controller;

import com.lingda.gamble.param.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/smp")
public class SMPController {

    private static Logger logger = LoggerFactory.getLogger(SMPController.class);

    @RequestMapping(value="chip", method = RequestMethod.POST)
    public void adjustChip(@RequestParam("chip") Integer chip) {
        logger.info("Adjust smp chip={}", chip);
        Config.setSmpChip(chip);
    }

    @RequestMapping(value="enable", method = RequestMethod.POST)
    public void enableSMP() {
        logger.info("Enable smp");
        Config.setSmpEnabled(true);
    }

    @RequestMapping(value="disable", method = RequestMethod.POST)
    public void disableSMP() {
        logger.info("Disable smp");
        Config.setSmpEnabled(false);
    }

    @RequestMapping(value="level", method = RequestMethod.POST)
    public void setSMPLevel(@RequestParam("level") Integer level) {
        logger.info("Set smp level={}", level);
        Config.setSmpLevels(level);
    }

}
