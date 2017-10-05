package com.lingda.gamble.service;

import com.lingda.gamble.mail.SimpleMailSender;
import com.lingda.gamble.model.WinLostMoney;
import com.lingda.gamble.param.Config;
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

    private final WinLostMoneyRepository winLostMoneyRepository;

    @Value("${gamble.notification.email}")
    private String email;

    @Value("${gamble.notification.admin}")
    private String admin;

    private boolean isBigMomentNotifiedToAdmin = false;

    @Autowired
    public WinLostMailNotificationJob(WinLostMoneyRepository winLostMoneyRepository) {
        this.winLostMoneyRepository = winLostMoneyRepository;
    }

    @Scheduled(fixedRate = 15 * 60 * 1000, initialDelay = 5 * 60 * 1000)
    public void scheduleWinLostMoneyNotificationJobs() {
        logger.info("[Operation - Win/Lost notification]");
        for (String mailAddress : Config.getEmail().split(",")) {
            WinLostMoney winLostMoney = winLostMoneyRepository.findFirstByAccountNameOrderByRoundDesc(Store.getAccountName());
            String subject = String.format("%s - Win/Lost: %s", Store.getAccountName(), winLostMoney.getWinLostMoney());
            SimpleMailSender.send(mailAddress, subject, "fyi");
            SimpleMailSender.send(email, subject, "fyi");
            try {
                if (Math.abs(winLostMoney.getWinLostMoney()) / 1000 > 8 && !isBigMomentNotifiedToAdmin) {
                    String bigMoment = String.format("[Big moment] %s - Win/Lost: %s", Store.getAccountName(), winLostMoney.getWinLostMoney());
                    SimpleMailSender.send(admin, bigMoment, "fyi");
                    isBigMomentNotifiedToAdmin = true;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void sendDangerousNotificationJobs(String message) {
        logger.info("[Operation - Dangerous notification]");
        for (String mailAddress : Config.getEmail().split(",")) {
            String subject = String.format("%s - %s", Store.getAccountName(), message);
            SimpleMailSender.send(mailAddress, subject, "fyi");
            SimpleMailSender.send(email, subject, "fyi");
        }
    }

}
