package com.bell.storage.dao;

import com.bell.storage.model.User;
import com.bell.storage.model.UsersFile;

/**
 * DAO для операций с Entity UsersFile
 */
public interface UsersFileDao {

    /**
     * Получить файл по собственнику
     * @param user
     * @return
     */
    Iterable<UsersFile> getFilesByOwner(User user);

    /**
     * Сохранить файл
     * @param usersFile
     * @return
     */
    UsersFile saveFile(UsersFile usersFile);

    /**
     * Удалить файл по id
     * @param id
     */
    void deleteFileById(Long id);

    /**
     * Получить файл по id
     * @param id
     * @return
     */
    UsersFile getFilesById(Long id);
}
