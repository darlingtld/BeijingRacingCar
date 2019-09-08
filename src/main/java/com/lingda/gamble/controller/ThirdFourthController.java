package com.lingda.gamble.controller;

import com.lingda.gamble.param.Config;
import com.lingda.gamble.param.StrategyMode;
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
@RequestMapping("/third_fourth")
public class ThirdFourthController {

    private static Logger logger = LoggerFactory.getLogger(ThirdFourthController.class);

    @RequestMapping(value = "enable", method = RequestMethod.POST)
    public void enable() {
        logger.info("Enable third fourth");
        Config.setThirdFourthEnabled(true);
    }

    @RequestMapping(value = "disable", method = RequestMethod.POST)
    public void disable() {
        logger.info("Disable third fourth");
        Config.setThirdFourthEnabled(false);
    }

    @RequestMapping(value = "exclude", method = RequestMethod.POST)
    public void excludeNumbers(@RequestParam("nums") String excludeNumStr) {
        logger.info("Exclude numbers={}", excludeNumStr);
        List<Integer> excludeNumberList = Arrays.stream(excludeNumStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        Config.setThirdFourthExcludeNumbers(excludeNumberList);
    }

    @RequestMapping(value = "level_chip", method = RequestMethod.POST)
    public void setThirdFourthLevel(@RequestParam("level_chip") String levelChips) {
        logger.info("Set third_fourth level_chips={}", levelChips);
        Config.setThirdFourthLevelAccList(Arrays.stream(levelChips.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

    @RequestMapping(value = "enable/smart_mode", method = RequestMethod.POST)
    public void enableThirdFourthSmartMode() {
        logger.info("Enable third_fourth smart mode");
        Config.setThirdFourthStrategyMode(StrategyMode.SMART);
    }

    @RequestMapping(value = "disable/smart_mode", method = RequestMethod.POST)
    public void disableThirdFourthSmartMode() {
        logger.info("Disable third_fourth smart mode");
        Config.setThirdFourthStrategyMode(StrategyMode.DISABLED);
    }

    @RequestMapping(value = "smart_switch", method = RequestMethod.POST)
    public void setThirdFourthSmartSwitch(@RequestParam("step1") String step1, @RequestParam("step2") String step2) {
        logger.info("set Third_Fourth smart switch={}-{}", step1, step2);
        Config.setThirdFourthSmartSwitch(Arrays.asList(step1, step2));
    }
    @RequestMapping(value="smart_mode_detect_round", method = RequestMethod.POST)
    public void setSmartModeDetectRoundNumber(@RequestParam("round") Integer round) {
        logger.info("Set Third_Fourth smart mode detect round={}", round);
        Config.setThirdFourthSmartDetectRoundNumber(round);
    }

    @RequestMapping(value="max_bet_count", method = RequestMethod.POST)
    public void setMaxBetCount(@RequestParam("count") Integer count) {
        logger.info("Set third_fourth max bet count={}", count);
        Config.setThirdFourthMaxBetCount(count);
    }

    @RequestMapping(value="pair_detect_round_number", method = RequestMethod.POST)
    public void setPairDetectRoundNumber(@RequestParam("number") Integer number) {
        logger.info("Set Third_Fourth pair_detect_round_number={}", number);
        Config.setThirdFourthPairModeDetectRoundNumber(number);
    }

    @RequestMapping(value="pair_gap_round_number", method = RequestMethod.POST)
    public void setPairGapRoundNumber(@RequestParam("number") Integer number) {
        logger.info("Set Third_Fourth pair_gap_round_number={}", number);
        Config.setThirdFourthGapRoundsForConsecutiveNumbers(number);
    }
}
