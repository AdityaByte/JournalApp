package com.journalapp.controller;

import java.util.Optional;

import org.bson.types.ObjectId;
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

import com.journalapp.api.WeatherResponse;
import com.journalapp.model.User;
import com.journalapp.service.UserService;
import com.journalapp.service.WeatherService;

@RestController
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;
    private final WeatherService weatherService;

    UserController(UserService userService , WeatherService weatherService) {
        this.userService = userService;
        this.weatherService = weatherService;
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


    @GetMapping("/all")
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

    @GetMapping("/greeting/{location}")
    public ResponseEntity<?> greeting(@PathVariable String location){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse response = weatherService.getWeather(location);
        String greeting = "";
        if(response == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(response != null){
            greeting = "Hi " + authentication.getName() + ", Weather feels like " + response.getMain().getFeelsLike();
        }
        return new ResponseEntity<>(greeting , HttpStatus.OK);
    }

    // If we want to consume a post api or we want to send a request to an api which is accepting post request then

    @GetMapping("/postApiIntegration/{data}")
    public ResponseEntity<?> postApi(@PathVariable String data){
        Object response = userService.hitPostApi(data);
        if(response != null){
            return new ResponseEntity<>(response , HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}