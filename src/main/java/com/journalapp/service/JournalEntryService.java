package com.journalapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.journalapp.model.JournalEntry;
import com.journalapp.model.User;
import com.journalapp.repository.JournalEntryRepository;

@Service
public class JournalEntryService {
    
    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;

    JournalEntryService(JournalEntryRepository journalEntryRepository, UserService userService){
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }

    @Transactional
    public JournalEntry saveJournal(JournalEntry journalEntry , String username){
        try {
            Optional<User> entry = userService.getUser(username);
            if(entry.isPresent()){
                User user = entry.get();
                journalEntry.setDate(LocalDate.now());
                JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
                user.getJournalEntries().add(savedEntry);
                userService.saveUser(user);
                return savedEntry;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Something is gone wrong at the server side " , e);
        }
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public List<JournalEntry> getUserJournal(String username){
        Optional<User> entry = userService.getUser(username);
        if(entry.isPresent()){
            List<JournalEntry> journalEntries = entry.get().getJournalEntries();
            return journalEntries;
        }
        return null;
    }

    public Optional<JournalEntry> getJournal(ObjectId id){
        Optional<JournalEntry> entry = journalEntryRepository.findById(id);
        if(entry.isPresent()){
            return entry;
        }
        return Optional.empty();
    }

    
    public boolean updateJournal(String username , ObjectId id, JournalEntry newJournal){
        Optional<User> user = userService.getUser(username);
        if(user.isPresent()){
            Optional<JournalEntry> oldEntry = journalEntryRepository.findById(id);
            if(oldEntry.isPresent()){
                JournalEntry oldJournal = oldEntry.get();
                oldJournal.setTitle(newJournal.getTitle() != null && !newJournal.getTitle().isEmpty() ? newJournal.getTitle() : oldJournal.getTitle());
                oldJournal.setContent(newJournal.getContent() != null && !newJournal.getContent().isEmpty() ? newJournal.getContent() : oldJournal.getContent());
                journalEntryRepository.save(oldJournal);
                return true;
            }
            return false;
        }
        return false;
    }

    // Its the wrong way to code its functional but its not optimize
    // public boolean deleteJournal(ObjectId id , String username){
    //     Optional<User> user = userService.getUser(username);
    //     if(user.isPresent()){
    //         User requriedUser = user.get();
    //         Optional<JournalEntry> journalEntry = requriedUser.getJournalEntries().stream().filter(i -> i.getId() == id).findFirst();
    //         if(journalEntry.isPresent()){
    //             Optional<JournalEntry> requiredJournalEntry = journalEntryRepository.findById(id);
    //             if(requiredJournalEntry.isPresent()){
    //                 journalEntryRepository.delete(journalEntry.get());
    //                 requriedUser.getJournalEntries().removeIf(entry -> entry.getId() == id);
    //                 userService.saveUser(requriedUser);
    //                 return true;
    //             }
    //         }
    //         return false;
    //     }
    //     return false;
    // }

    @Transactional
    public boolean deleteJournal(ObjectId id , String username){
        Optional<User> user = userService.getUser(username);
        if(user.isEmpty()){
            return false;
        }
        Optional<JournalEntry> journalEntry = user.get().getJournalEntries().stream().filter(x -> x.getId().equals(id)).findFirst();
        
        if(journalEntry.isEmpty()){
            return false;
        }

        Optional<JournalEntry> deleteJournal = journalEntryRepository.findById(id);
        if(deleteJournal.isEmpty()){
            return false;
        }

        user.get().getJournalEntries().removeIf(entry -> entry.getId() == id);
        journalEntryRepository.deleteById(id);
        userService.saveUser(user.get());
        return true;
    }
    

    public void deleteJournalForUser(ObjectId id){
        Optional<JournalEntry> journalEntry = journalEntryRepository.findById(id);
        if(journalEntry.isPresent()){
            journalEntryRepository.delete(journalEntry.get());
        }
    }

}
