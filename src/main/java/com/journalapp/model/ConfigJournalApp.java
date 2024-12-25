package com.journalapp.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "configJournalApp")
@Data
@NoArgsConstructor
public class ConfigJournalApp {
    
    private String key;
    private String value;

}
