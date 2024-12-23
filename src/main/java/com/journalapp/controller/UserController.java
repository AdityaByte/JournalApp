package com.journalapp.controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalapp.model.User;
import com.journalapp.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    
    // Here in the spring we don't prefer field injection we prefer constructor injection.
    // @Autowired
    // private UserService userService;

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }



    // @PostMapping
    // public ResponseEntity<User> saveUser(@RequestBody User user){
    //     try {
    //         return new ResponseEntity<>(userService.saveUser(user) , HttpStatus.CREATED);
    //     }
    //     catch(DuplicateKeyException e){
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    //     catch (Exception e) {
    //         System.out.println(e);
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    // }


    @GetMapping
    public ResponseEntity<User> getUsers(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            if(user != null){
                return new ResponseEntity<>(user , HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username){
        try{
            Optional<User> user = userService.getUser(username);
            if(user.isPresent()){
                return new ResponseEntity<>(user.get() , HttpStatus.OK);
            } 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings("rawtypes")
    @PutMapping
    public ResponseEntity updateUser(@RequestBody User user){
        try{
            org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            if(userService.updateUser(user , username)){
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable ObjectId id){
        try {
            if(userService.deleteUser(id)){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}