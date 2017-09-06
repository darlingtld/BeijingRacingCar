package com.lingda.gamble.repository;

import com.lingda.gamble.model.SMPRatio;
import com.lingda.gamble.model.SMPSingleRatio;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SMPRatioRepositoryTest {

    @Autowired
    private SMPRatioRepository smpRatioRepository;

    @Test
    public void shouldSaveSMPRatio() {
        SMPRatio smpRatio = new SMPRatio();
        smpRatio.setRound(1);
        smpRatio.setRatioFirst(new SMPSingleRatio(1.98, 1.98, 1.98, 1.98, 1.98, 1.98));
        smpRatio.setRatioSecond(new SMPSingleRatio(1.98, 1.98, 1.98, 1.98, 1.98, 1.98));
        smpRatio.setRatioThird(new SMPSingleRatio(1.98, 1.98, 1.98, 1.98, 1.98, 1.98));
        smpRatio.setRatioFourth(new SMPSingleRatio(1.98, 1.98, 1.98, 1.98, 1.98, 1.98));
        smpRatio.setRatioFifth(new SMPSingleRatio(1.98, 1.98, 1.98, 1.98, 1.98, 1.98));
        smpRatio.setRatioSixth(new SMPSingleRatio(1.98, 1.98, 1.98, 1.98, 1.98, 1.98));
        smpRatio.setRatioSeventh(new SMPSingleRatio(1.98, 1.98, 1.98, 1.98, 1.98, 1.98));
        smpRatio.setRatioEighth(new SMPSingleRatio(1.98, 1.98, 1.98, 1.98, 1.98, 1.98));
        smpRatio.setRatioNinth(new SMPSingleRatio(1.98, 1.98, 1.98, 1.98, 1.98, 1.98));
        smpRatio.setRatioTenth(new SMPSingleRatio(1.98, 1.98, 1.98, 1.98, 1.98, 1.98));
        smpRatioRepository.save(smpRatio);
    }
}
