package com.journalapp.scheduler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.journalapp.cache.AppCache;
import com.journalapp.model.User;
import com.journalapp.repository.UserRepositoryImpl;
import com.journalapp.service.EmailService;
import com.journalapp.service.SentimentAnalysisService;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private AppCache appCache;
    
    /** 
     * Here by the help of scheduled annotation or we can call that schedular
     * so that we dont have to manually call things spring can automatically call 
     * that particular method after a particular interval or time.
     * It accepts a cron which is a cronous means time so we have to pass a cron expression
     * there.
     * At last we have to give a annotation at the main method for telling that 
     * in our application we have scheduled methods too.
     * @EnableScheduling
    **/
    @Scheduled(cron = "0 0 9 * * SUN") // 0 - second 0 -minutes 9 - hour  * - day of the month * - month SUN,0,7 - day of the week
    // @Scheduled(cron = "0 * * ? * *")
    public void fetchUserAndSendSaMail(){
        List<User> users = userRepositoryImpl.getUsersForSA();
        for(User user : users){
            List<String> filteredEntries = user.getJournalEntries().stream()
                .filter(x -> x.getDate().isAfter(LocalDate.now().minus(7, ChronoUnit.DAYS)))
                .map(x -> x.getContent())
                .collect(Collectors.toList());
            String entry = String.join(" ", filteredEntries);
            String sentiment = sentimentAnalysisService.getSentiment(entry);
            emailService.sendEmail(user.getEmail(), "Sentiment of last 7 days", sentiment);
            }
    }

    @Scheduled(cron = "0 0/10 * ? * *") // Cron for every 5 minute
    public void clearAppCache(){
        appCache.init();
    }
}
