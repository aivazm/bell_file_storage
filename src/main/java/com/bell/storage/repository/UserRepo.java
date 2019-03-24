package com.bell.storage.repository;

import com.bell.storage.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByUsername(String userName);

    User findByActivationCode(String code);
}
