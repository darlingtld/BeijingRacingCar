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
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/first_second")
public class FirstSecondController {

    private static Logger logger = LoggerFactory.getLogger(FirstSecondController.class);

    @RequestMapping(value="enable", method = RequestMethod.POST)
    public void enable() {
        logger.info("Enable first second");
        Config.setFirstSecondEnabled(true);
    }

    @RequestMapping(value="disable", method = RequestMethod.POST)
    public void disable() {
        logger.info("Disable first second");
        Config.setFirstSecondEnabled(false);
    }

    @RequestMapping(value="exclude", method = RequestMethod.POST)
    public void excludeNumbers(@RequestParam("nums") String excludeNumStr) {
        logger.info("Exclude numbers={}", excludeNumStr);
        List<Integer> excludeNumberList = Arrays.stream(excludeNumStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        Config.setFirstSecondExcludeNumbers(excludeNumberList);
    }

    @RequestMapping(value="level_chip", method = RequestMethod.POST)
    public void setFirstSecondLevelChip(@RequestParam("level_chip") String levelChips) {
        logger.info("Set first_second level_chips={}", levelChips);
        Config.setFirstSecondLevelAccList(Arrays.stream(levelChips.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

    @RequestMapping(value="enable/smart_mode", method = RequestMethod.POST)
    public void enableFirstSecondSmartMode() {
        logger.info("Enable first_second smart mode");
        Config.setFirstSecondSmartMode(true);
    }

    @RequestMapping(value="disable/smart_mode", method = RequestMethod.POST)
    public void disableFirstSecondSmartMode() {
        logger.info("Disable first_second smart mode");
        Config.setFirstSecondSmartMode(false);
    }

    @RequestMapping(value="smart_switch", method = RequestMethod.POST)
    public void setFirstSecondSmartSwitch(@RequestParam("step1") String step1, @RequestParam("step2") String step2) {
        logger.info("Set first_second smart switch={} - {}", step1, step2);
        Config.setFirstSecondSmartSwitch(Arrays.asList(step1, step2));
    }

    @RequestMapping(value="smart_mode_detect_round", method = RequestMethod.POST)
    public void setSmartModeDetectRoundNumber(@RequestParam("round") Integer round) {
        logger.info("Set first_second smart mode detect round={}", round);
        Config.setFirstSecondSmartDetectRoundNumber(round);
    }

    @RequestMapping(value="max_bet_count", method = RequestMethod.POST)
    public void setMaxBetCount(@RequestParam("count") Integer count) {
        logger.info("Set first_second max bet count={}", count);
        Config.setFirstSecondMaxBetCount(count);
    }
}
