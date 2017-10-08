package com.lingda.gamble.controller;

import com.lingda.gamble.param.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/seventh_eighth")
public class SeventhEighthController {

    private static Logger logger = LoggerFactory.getLogger(SeventhEighthController.class);

    @RequestMapping(value="enable", method = RequestMethod.POST)
    public void enable() {
        logger.info("Enable seventh_eighth");
        Config.setSeventhEighthEnabled(true);
    }

    @RequestMapping(value="disable", method = RequestMethod.POST)
    public void disable() {
        logger.info("Disable seventh_eighth");
        Config.setSeventhEighthEnabled(false);
    }

    @RequestMapping(value="exclude", method = RequestMethod.POST)
    public void excludeNumbers(@RequestParam("nums") String excludeNumStr) {
        logger.info("Exclude numbers={}", excludeNumStr);
        List<Integer> excludeNumberList = Arrays.stream(excludeNumStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        Config.setSeventhEighthExcludeNumbers(excludeNumberList);
    }

    @RequestMapping(value="level_chip", method = RequestMethod.POST)
    public void setSeventhEighthLevel(@RequestParam("level_chip") String levelChips) {
        logger.info("Set seventh_eighth level_chips={}", levelChips);
        Config.setSeventhEighthLevelAccList(Arrays.stream(levelChips.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

    @RequestMapping(value="enable/smart_mode", method = RequestMethod.POST)
    public void enableSeventhEighthSmartMode() {
        logger.info("Enable seventh_eighth smart mode");
        Config.setSeventhEighthSmartMode(true);
    }

    @RequestMapping(value="disable/smart_mode", method = RequestMethod.POST)
    public void disableSeventhEighthSmartMode() {
        logger.info("Disable seventh_eighth smart mode");
        Config.setSeventhEighthSmartMode(false);
    }

    @RequestMapping(value="smart_switch", method = RequestMethod.POST)
    public void setSeventhEighthSmartSwitch(@RequestParam("step1") String step1, @RequestParam("step2") String step2) {
        logger.info("set Seventh_Eighth smart switch={}-{}",step1, step2);
        Config.setSeventhEighthSmartSwitch(Arrays.asList(step1, step2));
    }
    @RequestMapping(value="smart_mode_detect_round", method = RequestMethod.POST)
    public void setSmartModeDetectRoundNumber(@RequestParam("round") Integer round) {
        logger.info("Set SeventhEighth smart mode detect round={}", round);
        Config.setSeventhEighthSmartDetectRoundNumber(round);
    }

    @RequestMapping(value="max_bet_count", method = RequestMethod.POST)
    public void setMaxBetCount(@RequestParam("count") Integer count) {
        logger.info("Set seventh_eighth max bet count={}", count);
        Config.setSeventhEighthMaxBetCount(count);
    }
}
