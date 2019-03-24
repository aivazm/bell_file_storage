package com.bell.storage.dao;

import com.bell.storage.model.User;

/**
 * DAO для операций с Entity User
 */
public interface UserDao {
    /**
     * Получить пользователя по имени
     * @param username
     * @return
     */
    User loadUserByUsername(String username);

    /**
     * Получить пользователя по id
     * @param id
     * @return
     */
    User getUserById(Long id);

    /**
     * Сохранить пользователя
     * @param user
     * @return
     */
    User saveUser(User user);

    /**
     * Активация пользователя
     * @param code
     * @return
     */
    User activateUser(String code);

    /**
     * Получить всех пользователей
     * @return
     */
    Iterable<User> findAllUsers();

}
