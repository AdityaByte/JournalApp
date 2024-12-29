package com.journalapp.repository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.journalapp.model.User;

// Here we learnt about criteria api

@Component
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    public List<User> getUsersForSA(){
        Query query = new Query();

        query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

        // Here we are implementing and condition between them
        // query.addCriteria(Criteria.where("email").exists(true));
        // query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

        // For or condition
        // Criteria criteria = new Criteria();
        // query.addCriteria(criteria.orOperator(
        //     Criteria.where("email").exists(true), 
        //     Criteria.where("sentimentAnalysis").is(true))
        // );

        List<User> users = mongoTemplate.find(query, User.class);

        try(FileWriter writer = new FileWriter("output.txt" , true)){
            if(users != null && !users.isEmpty()){
                for(User user : users){
                    writer.append(user.toString()).append("\n");
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return users;
    }

}
