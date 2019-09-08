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
@RequestMapping("/fifth_sixth")
public class FifthSixthController {

    private static Logger logger = LoggerFactory.getLogger(FifthSixthController.class);

    @RequestMapping(value="enable", method = RequestMethod.POST)
    public void enable() {
        logger.info("Enable fifth_sixth");
        Config.setFifthSixthEnabled(true);
    }

    @RequestMapping(value="disable", method = RequestMethod.POST)
    public void disable() {
        logger.info("Disable fifth_sixth");
        Config.setFifthSixthEnabled(false);
    }

    @RequestMapping(value="exclude", method = RequestMethod.POST)
    public void excludeNumbers(@RequestParam("nums") String excludeNumStr) {
        logger.info("Exclude numbers={}", excludeNumStr);
        List<Integer> excludeNumberList = Arrays.stream(excludeNumStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        Config.setFifthSixthExcludeNumbers(excludeNumberList);
    }

    @RequestMapping(value="level_chip", method = RequestMethod.POST)
    public void setFifthSixthLevel(@RequestParam("level_chip") String levelChips) {
        logger.info("Set fifth_sixth level_chips={}", levelChips);
        Config.setFifthSixthLevelAccList(Arrays.stream(levelChips.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

    @RequestMapping(value="enable/smart_mode", method = RequestMethod.POST)
    public void enableFifthSixthSmartMode() {
        logger.info("Enable fifth_sixth smart mode");
        Config.setFifthSixthStrategyMode(StrategyMode.SMART);
    }

    @RequestMapping(value="disable/smart_mode", method = RequestMethod.POST)
    public void disableFifthSixthSmartMode() {
        logger.info("Disable fifth_sixth smart mode");
        Config.setFifthSixthStrategyMode(StrategyMode.DISABLED);
    }

    @RequestMapping(value="smart_switch", method = RequestMethod.POST)
    public void setFifthSixthSmartSwitch(@RequestParam("step1") String step1, @RequestParam("step2") String step2) {
        logger.info("set Fifth_Sixth smart switch={} - {}", step1, step2);
        Config.setFifthSixthSmartSwitch(Arrays.asList(step1, step2));
    }

    @RequestMapping(value="smart_mode_detect_round", method = RequestMethod.POST)
    public void setSmartModeDetectRoundNumber(@RequestParam("round") Integer round) {
        logger.info("Set FifthSixth smart mode detect round={}", round);
        Config.setFifthSixthSmartDetectRoundNumber(round);
    }

    @RequestMapping(value="max_bet_count", method = RequestMethod.POST)
    public void setMaxBetCount(@RequestParam("count") Integer count) {
        logger.info("Set fifth_sixth max bet count={}", count);
        Config.setFifthSixthMaxBetCount(count);
    }

    @RequestMapping(value="pair_detect_round_number", method = RequestMethod.POST)
    public void setPairDetectRoundNumber(@RequestParam("number") Integer number) {
        logger.info("Set Fifth_Sixth pair_detect_round_number={}", number);
        Config.setFifthSixthPairModeDetectRoundNumber(number);
    }

    @RequestMapping(value="pair_gap_round_number", method = RequestMethod.POST)
    public void setPairGapRoundNumber(@RequestParam("number") Integer number) {
        logger.info("Set Fifth_Sixth pair_gap_round_number={}", number);
        Config.setFifthSixthGapRoundsForConsecutiveNumbers(number);
    }
}
