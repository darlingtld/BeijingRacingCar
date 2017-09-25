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
        Config.setThirdFourthSmartMode(true);
    }

    @RequestMapping(value = "disable/smart_mode", method = RequestMethod.POST)
    public void disableThirdFourthSmartMode() {
        logger.info("Disable third_fourth smart mode");
        Config.setThirdFourthSmartMode(false);
    }

    @RequestMapping(value = "smart_switch", method = RequestMethod.POST)
    public void setThirdFourthSmartSwitch(@RequestParam("step1") String step1, @RequestParam("step2") String step2) {
        logger.info("set Third_Fourth smart switch={}-{}", step1, step2);
        Config.setThirdFourthSmartSwitch(Arrays.asList(step1, step2));
    }
}
