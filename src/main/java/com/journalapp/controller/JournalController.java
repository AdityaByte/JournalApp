package com.journalapp.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalapp.model.JournalEntry;
import com.journalapp.model.User;
import com.journalapp.service.JournalEntryService;
import com.journalapp.service.UserService;

@RestController
@RequestMapping(value = "/journal")
public class JournalController{

    // @Autowired
    // private JournalEntryService journalEntryService;

    private final JournalEntryService journalEntryService;

    // @Autowired
    // private UserService userService;

    private final UserService userService;

    JournalController(JournalEntryService journalEntryService, UserService userService){
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseEntity<JournalEntry> addJournal(@RequestBody JournalEntry journalEntry){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            JournalEntry entry = journalEntryService.saveJournal(journalEntry, authentication.getName());
            if(entry != null){
                return new ResponseEntity<>(entry , HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Only admin can see all the Journal entries not any user
    // @GetMapping
    // public ResponseEntity<List<JournalEntry>> getAllJournal(){
    //     try{
    //         List<JournalEntry> journalEntries = journalEntryService.getAll();
    //         if(journalEntries != null && !journalEntries.isEmpty()){
    //             return new ResponseEntity<>(journalEntries , HttpStatus.OK);
    //         }
    //         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    //     }catch(Exception e){
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getUserJournal(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            List<JournalEntry> journalEntries = journalEntryService.getUserJournal(username);
            if(journalEntries != null && !journalEntries.isEmpty()){
                return new ResponseEntity<>(journalEntries , HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Here we are getting journal entries of particular user 
    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getJournal(@PathVariable("id") ObjectId id){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
            if(!collect.isEmpty()){
                Optional<JournalEntry> journalEntry = journalEntryService.getJournal(id);
                return new ResponseEntity<>(journalEntry.get() , HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings("rawtypes")
    @PutMapping("/id/{id}")
    public ResponseEntity updateJournal(@PathVariable ObjectId id , @RequestBody JournalEntry journalEntry){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            if(username != null){
                journalEntryService.updateJournal(username , id, journalEntry);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/id/{id}")
    public ResponseEntity deleteJournal(@PathVariable ObjectId id){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            if(username != null){
                if(journalEntryService.deleteJournal(id, username)){
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}