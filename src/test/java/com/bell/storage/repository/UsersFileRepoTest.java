package com.bell.storage.repository;

import com.bell.storage.model.User;
import com.bell.storage.model.UsersFile;
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
public class UsersFileRepoTest {

    @Autowired
    private UsersFileRepo usersFileRepo;

    @Autowired
    private UserRepo userRepo;

    private final User user = User.builder()
            .username("Bob")
            .password("password")
            .email("email@email.net")
            .activationCode("code")
            .build();

    private final UsersFile usersFile = UsersFile.builder()
            .fileName("filename")
            .owner(user)
            .build();

    @Test
    public void findByOwner() {
        userRepo.save(user);
        usersFileRepo.save(usersFile);
        Iterable<UsersFile> filesFromDb = usersFileRepo.findByOwner(user);
        for (UsersFile fileFromDb : filesFromDb) {
            assertEquals(usersFile, fileFromDb);
        }
    }
}