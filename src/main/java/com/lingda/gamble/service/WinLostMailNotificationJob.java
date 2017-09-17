package com.lingda.gamble.service;

import com.lingda.gamble.mail.SimpleMailSender;
import com.lingda.gamble.model.WinLostMoney;
import com.lingda.gamble.repository.WinLostMoneyRepository;
import com.lingda.gamble.util.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WinLostMailNotificationJob {

    private static final Logger logger = LoggerFactory.getLogger(WinLostMailNotificationJob.class);

    @Value("${gamble.winlost.notification.email}")
    private String email;

    @Autowired
    private WinLostMoneyRepository winLostMoneyRepository;

    @Scheduled(fixedRate = 30 * 60 * 1000, initialDelay = 30 * 60 * 1000)
    public void scheduleWinLostMoneyNotificationJobs() {
        logger.info("[Operation - Win/Lost notification]");
        WinLostMoney winLostMoney = winLostMoneyRepository.findFirstByAccountNameOrderByRoundDesc(Store.getAccountName());
        String subject = String.format("%s - 今日输赢: %s", Store.getAccountName(), winLostMoney.getWinLostMoney());
        SimpleMailSender.send(email, subject, "fyi");
    }
}
