package com.bell.storage.dao;

import com.bell.storage.model.User;
import com.bell.storage.model.UsersFile;
import com.bell.storage.repository.UsersFileRepo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@inheritDoc}
 */
@Repository
public class UsersFileDaoImpl implements UsersFileDao {

    private final UsersFileRepo usersFileRepo;

    public UsersFileDaoImpl(UsersFileRepo usersFileRepo) {
        this.usersFileRepo = usersFileRepo;
    }

    /**
     * {@inheritDoc}
     */
    public Iterable<UsersFile> getFilesByOwner(User user) {
        return usersFileRepo.findByOwner(user);
    }

    /**
     * {@inheritDoc}
     */
    public UsersFile saveFile(UsersFile usersFile){
        return usersFileRepo.save(usersFile);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteFileById(Long id){
        usersFileRepo.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    public UsersFile getFilesById(Long id){
        Optional<UsersFile> optional = usersFileRepo.findById(id);
        if(optional.isPresent()){
            return optional.get();
        } else {
            throw new RuntimeException("Can't find file width id: " + id);
        }
    }

}
