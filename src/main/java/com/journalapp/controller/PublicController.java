package com.journalapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalapp.model.User;
import com.journalapp.service.UserService;
import com.mongodb.DuplicateKeyException;

@RestController
@RequestMapping("/public")
public class PublicController {
    
    // @Autowired
    // private UserService userService;

    // Constructor injection 

    private final UserService userService;

    PublicController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        try {
            return new ResponseEntity<>(userService.saveNewUser(user) , HttpStatus.CREATED);
        }
        catch(DuplicateKeyException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @SuppressWarnings("rawtypes")
    @GetMapping("/health-check")
    public ResponseEntity healthCheck(){
        return ResponseEntity.ok("Health is OK");
    }

}
