package com.bell.storage.repository;

import com.bell.storage.model.User;
import com.bell.storage.model.UsersFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersFileRepo extends CrudRepository<UsersFile, Long> {

    Iterable<UsersFile> findByOwner(User user);
}
