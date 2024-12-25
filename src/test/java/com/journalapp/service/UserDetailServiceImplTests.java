package com.journalapp.service;

import com.journalapp.model.User;
import com.journalapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

// Here in this code we will learn about mockito that how we can inject mocks in dependencies and test things
// rather than injecting real dependencies by making database connection n all
// InjectMocks -> By this we can inject mocks to the dependencies.
// Mock -> By this the dependency become a mock.
// BeforeEach -> This will run before each, and it initializes the all mocks in this instance.

public class UserDetailServiceImplTests {

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadUserByUsernameTest(){
        when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(Optional.of(
                User.builder()
                        .userName("aditya234")
                        .password("123445")
                        .roles(new ArrayList<>())
                        .build()
        ));
        UserDetails user = userDetailService.loadUserByUsername("amit");
        Assertions.assertNotNull(user);
    }
}
