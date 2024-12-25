package com.journalapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.journalapp.model.User;
import com.journalapp.repository.UserRepository;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    //@BeforeEach , @BeforeAll and @AfterEach , @AfterAll
    
    //@Disabled
    @Test
    public void testFindByUsername(){
        assertEquals(4 , 2+2);
        assertNotNull(userRepository.findByUserName("aditya").get());
    }
    // here if any case in a particular component fails then the whole test fails

    // Parametarized example of testFindByUsername
    
    //@Disabled
    @ParameterizedTest
    @ValueSource(strings = {
        "aditya",
        "yeshank",
        "kartik"
    })
    // @CsvSource({
    //     "aditya",
    //     "yeshank",
    //     "kartik",
    //     "abhinav"
    // })
    public void testFindByUsername(String name){
        assertNotNull(userRepository.findByUserName(name).get() , "Failed for : " + name);
    }

    // Here we are using argumentsource
    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void testArguments(User user){
        assertNotNull(userService.saveNewUser(user));
    }

    //@Disabled // Here we can disable any test by this annotation
    @Test
    public void testNotNullJournalEntries(){
        User user = userRepository.findByUserName("yeshank").get();
        assertTrue(!user.getJournalEntries().isEmpty());
    }

    //@Disabled
    @ParameterizedTest
    @CsvSource({
        "1,1,2",
        "2,3,5",
        "3,3,4"
    })
    public void testParametarizedTest(int a , int b , int expected){
        assertEquals(expected, a+b);
    }

}
