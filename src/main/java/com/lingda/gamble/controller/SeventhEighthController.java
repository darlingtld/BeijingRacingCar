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

    @RequestMapping(value="chip", method = RequestMethod.POST)
    public void adjustChip(@RequestParam("chip") Integer chip) {
        logger.info("Adjust seventh_eighth chip={}", chip);
        Config.setSeventhEighthChip(chip);
    }

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
    public void setSMPLevel(@RequestParam("level_chip") String levelChips) {
        logger.info("Set seventh_eighth level_chips={}", levelChips);
        Config.setSeventhEighthLevelAccList(Arrays.stream(levelChips.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

}
