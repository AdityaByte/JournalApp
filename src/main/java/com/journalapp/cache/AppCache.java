package com.journalapp.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.journalapp.model.ConfigJournalApp;
import com.journalapp.repository.ConfigJournalAppRepository;

import jakarta.annotation.PostConstruct;

// AppCache class is an inmemory cache

@Component
public class AppCache {

    public enum keys{
        WEATHER_API
    }
    
    public Map<String, String> appCache;

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    // This method runs when the bean of the AppCache is created and it will load the api configuration and all to a list 
    // The advantage of using in memory cache is that we reduce the database queries so that our application works fastly and efficiently.
    @PostConstruct
    public void init(){
        appCache = new HashMap<>(); // Here we define it because of re-initialization concept.
        List<ConfigJournalApp> all = configJournalAppRepository.findAll();
        for(ConfigJournalApp configJournalApp : all){
            appCache.put(configJournalApp.getKey(), configJournalApp.getValue());
        }
    }

}
