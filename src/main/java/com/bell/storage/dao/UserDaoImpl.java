package com.bell.storage.dao;

import com.bell.storage.model.User;
import com.bell.storage.repository.UserRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * {@inheritDoc}
 */
@Repository
public class UserDaoImpl implements UserDao {

    private final UserRepo userRepo;

    public UserDaoImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * {@inheritDoc}
     */
    public User loadUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new RuntimeException("Empty parameters");
        }
        return userRepo.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    public User getUserById(Long id) {
        Optional<User> optional = userRepo.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("User with id " + id + " not found");
        }
    }

    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    /**
     * {@inheritDoc}
     */
    public User activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (System.currentTimeMillis() - user.getDateOfRegistration() > 86_400_000) {
            userRepo.delete(user);
            user = null;
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public Iterable<User> findAllUsers() {
        return userRepo.findAll();
    }
}
