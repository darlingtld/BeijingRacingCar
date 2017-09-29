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
@RequestMapping("/nineth_tenth")
public class NinethTenthController {

    private static Logger logger = LoggerFactory.getLogger(NinethTenthController.class);

    @RequestMapping(value="enable", method = RequestMethod.POST)
    public void enable() {
        logger.info("Enable nineth_tenth");
        Config.setNinethTenthEnabled(true);
    }

    @RequestMapping(value="disable", method = RequestMethod.POST)
    public void disable() {
        logger.info("Disable nineth_tenth");
        Config.setNinethTenthEnabled(false);
    }

    @RequestMapping(value="exclude", method = RequestMethod.POST)
    public void excludeNumbers(@RequestParam("nums") String excludeNumStr) {
        logger.info("Exclude numbers={}", excludeNumStr);
        List<Integer> excludeNumberList = Arrays.stream(excludeNumStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        Config.setNinethTenthExcludeNumbers(excludeNumberList);
    }

    @RequestMapping(value="level_chip", method = RequestMethod.POST)
    public void setNinethTenthLevel(@RequestParam("level_chip") String levelChips) {
        logger.info("Set nineth_tenth level_chips={}", levelChips);
        Config.setNinethTenthLevelAccList(Arrays.stream(levelChips.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

    @RequestMapping(value="enable/smart_mode", method = RequestMethod.POST)
    public void enableNinethTenthSmartMode() {
        logger.info("Enable nineth_tenth smart mode");
        Config.setNinethTenthSmartMode(true);
    }

    @RequestMapping(value="disable/smart_mode", method = RequestMethod.POST)
    public void disableNinethTenthSmartMode() {
        logger.info("Disable nineth_tenth smart mode");
        Config.setNinethTenthSmartMode(false);
    }

    @RequestMapping(value="smart_switch", method = RequestMethod.POST)
    public void setNinethTenthSmartSwitch(@RequestParam("step1") String step1, @RequestParam("step2") String step2) {
        logger.info("set Nineth_Tenth smart switch={} - {}", step1, step2);
        Config.setNinethTenthSmartSwitch(Arrays.asList(step1, step2));
    }

}
