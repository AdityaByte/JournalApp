package com.journalapp.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> users = userService.getAllUser();
        if(users != null && !users.isEmpty()){
            return new ResponseEntity<>(users , HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createAdmin(@RequestBody User user){
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

}
