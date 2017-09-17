package com.lingda.gamble.repository;

import com.lingda.gamble.model.WinLostMoney;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WinLostMoneyRepositoryTest {

    @Autowired
    private WinLostMoneyRepository winLostMoneyRepository;

    @Test
    public void shouldFindFirstByAccountNameDesc() {
        WinLostMoney winLostMoney = winLostMoneyRepository.findFirstByAccountNameOrderByRoundDesc("上测 (d88567)");
        System.out.println(winLostMoney);
    }

    @Test
    public void shouldFindFirstByRoundDesc() {
        WinLostMoney winLostMoney = winLostMoneyRepository.findByRoundAndAccountName(640362, "上测 (d88567)");
        System.out.println(winLostMoney);
    }

}
