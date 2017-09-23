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

    @RequestMapping(value="chip", method = RequestMethod.POST)
    public void adjustChip(@RequestParam("chip") Integer chip) {
        logger.info("Adjust third fourth chip={}", chip);
        Config.setThirdFourthChip(chip);
    }

    @RequestMapping(value="enable", method = RequestMethod.POST)
    public void enable() {
        logger.info("Enable third fourth");
        Config.setThirdFourthEnabled(true);
    }

    @RequestMapping(value="disable", method = RequestMethod.POST)
    public void disable() {
        logger.info("Disable third fourth");
        Config.setThirdFourthEnabled(false);
    }

    @RequestMapping(value="exclude", method = RequestMethod.POST)
    public void excludeNumbers(@RequestParam("nums") String excludeNumStr) {
        logger.info("Exclude numbers={}", excludeNumStr);
        List<Integer> excludeNumberList = Arrays.stream(excludeNumStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        Config.setThirdFourthExcludeNumbers(excludeNumberList);
    }

}
