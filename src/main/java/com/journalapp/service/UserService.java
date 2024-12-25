package com.journalapp.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.journalapp.model.JournalEntry;
import com.journalapp.model.User;
import com.journalapp.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JournalEntryService journalEntryService;

    UserService(UserRepository userRepository, @Lazy JournalEntryService journalEntryService){
        this.userRepository = userRepository;
        this.journalEntryService = journalEntryService;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Value("${python.api}")
    private String pythonApiUrl;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User saveNewUser(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return user;
        }
        catch(Exception e){
            logger.info("Information - {} " , "error occured");
            logger.warn("Warning" );
            logger.error("Error : {}" , e.getMessage());
            logger.debug("this is the debug");
            logger.trace("this is the trace");
            throw new RuntimeException("Error occured");
        }
    }

    public User saveUser(User user){
        userRepository.save(user);
        return user;
    }

    public User saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER" , "ADMIN"));
        userRepository.save(user);
        return user;
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public Optional<User> getUser(String username){
        Optional<User> user = userRepository.findByUserName(username);
        if(user.isPresent()){
            return user;
        }
        return Optional.empty();
    }

    public boolean updateUser(User newUser , String username){
        Optional<User> user = userRepository.findByUserName(username);
        if(user.isPresent()){
            User oldUser = user.get();
            oldUser.setUserName(newUser.getUserName() != null && !newUser.getUserName().isEmpty() ? newUser.getUserName() : oldUser.getUserName());
            oldUser.setPassword(newUser.getPassword() != null && !newUser.getPassword().isEmpty() ? passwordEncoder.encode(newUser.getPassword()) : oldUser.getPassword());
            userRepository.save(oldUser);
            return true;
        }
        return false;
    }

    public boolean deleteUser(ObjectId id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            userRepository.delete(user.get());
            List<JournalEntry> journalEntries = user.get().getJournalEntries();
            for(JournalEntry journalEntry : journalEntries){
                journalEntryService.deleteJournalForUser(journalEntry.getId());
            }
            return true;
        }
        return false;
    }

    public List<JournalEntry> getJournalEntriesofUser(String username){
        Optional<User> entry = userRepository.findByUserName(username);
        if(entry.isPresent()){
            List<JournalEntry> journalEntries = entry.get().getJournalEntries();
            return journalEntries;
        }
        return null;
    }

    public User findByUsername(String username){
        return userRepository.findByUserName(username).get();
    }

    public Object hitPostApi(String data){

        Map<String , String> requestData = Map.of("title" , data);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestData);

        ResponseEntity<?> responseofApi = restTemplate.exchange(pythonApiUrl + "/api", HttpMethod.POST , requestEntity , String.class);
        return responseofApi;
    }

}