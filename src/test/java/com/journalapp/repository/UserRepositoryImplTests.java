package com.journalapp.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryImplTests {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    
    @Test
    public void testGetUsersForSA(){
        assertNotNull(userRepositoryImpl.getUsersForSA());
        assertFalse(userRepositoryImpl.getUsersForSA().isEmpty());
    }

}
