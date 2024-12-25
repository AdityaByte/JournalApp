package com.journalapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalapp.cache.AppCache;
import com.journalapp.model.User;
import com.journalapp.service.UserService;

@RestController
@Validated
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    AdminController(UserService userService){
        this.userService = userService;
    }

    @Autowired
    private AppCache appCache;
    
    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUser();
        if(users != null && !users.isEmpty()){
            return new ResponseEntity<>(users , HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<User> createAdmin(User user){ // Here we remove the request body and use the simple pojo
        try{
            User savedUser = userService.saveAdmin(user);
            if(savedUser != null){
                return new ResponseEntity<>(savedUser , HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/clear-app-cache")
    public void clearAppCache(){
        appCache.init();
    }

}
