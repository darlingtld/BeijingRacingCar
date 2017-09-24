package com.lingda.gamble.controller;

import com.lingda.gamble.param.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;


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

    @RequestMapping(value="enable/category/{category}", method = RequestMethod.POST)
    public void enableSMPCategory(@PathVariable("category") String category) {
        logger.info("Enable smp category={}", category);
        switch (category){
            case "daxiao":
                Config.setSmpDaXiao(true);
                break;
            case "danshuang":
                Config.setSmpDanShuang(true);
                break;
        }
    }

    @RequestMapping(value="disable/category/{category}", method = RequestMethod.POST)
    public void disableSMPCategory(@PathVariable("category") String category) {
        logger.info("Disable smp category={}", category);
        switch (category){
            case "daxiao":
                Config.setSmpDaXiao(false);
                break;
            case "danshuang":
                Config.setSmpDanShuang(false);
                break;
        }
    }

    @RequestMapping(value="level_chip", method = RequestMethod.POST)
    public void setSMPLevel(@RequestParam("level_chip") String levelChips) {
        logger.info("Set smp level_chips={}", levelChips);
        Config.setSmpLevelChips(Arrays.stream(levelChips.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

}
