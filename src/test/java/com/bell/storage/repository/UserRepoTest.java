package com.bell.storage.repository;

import com.bell.storage.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = "/clear-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    private User user;

    private final String USERNAME = "Bob";
    private final String CODE = "code";

    @Before
    public void setUp() {
        user = User.builder()
                .username(USERNAME)
                .password("password")
                .email("email@email.net")
                .activationCode(CODE)
                .build();
    }

    @Test
    public void findByUsername() {
        userRepo.save(user);
        User fromDb = userRepo.findByUsername(USERNAME);
        assertEquals(fromDb, user);
    }

    @Test
    public void findByActivationCode() {
        userRepo.save(user);
        User fromDb = userRepo.findByActivationCode(CODE);
        assertEquals(fromDb, user);
    }
}