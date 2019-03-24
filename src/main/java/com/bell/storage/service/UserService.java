package com.bell.storage.service;

import com.bell.storage.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Map;

/**
 * Сервис операций с пользователем
 */
public interface UserService extends UserDetailsService {

    /**
     * Регистрация нового пользователя
     * @param passwordConfirm
     * @param userDto
     * @param bindingResult
     * @param model
     * @return
     */
    String addUser(String passwordConfirm, UserDto userDto, BindingResult bindingResult, Model model);

    /**
     * Активация пользователя. Если с момента регистрации до момента активации пользователя прошло 24 часа - пользователь удаляется
     * @param code
     * @param model
     */
    void activateUser(String code, Model model);

    /**
     * Найти всех пользователей
     * @param model
     */
    void findAll(Model model);

    /**
     * Изменение роли пользователя. В дополнение к роли USER (по-умолчанию) позволяет установить пользователю роли ANALYST и ADMIN
     * @param userDto
     * @param username
     * @param form
     */
    void changeUserRole(UserDto userDto, String username, Map<String, String> form);

    /**
     * Получить профиль текущего пользователя
     * @param model
     * @param userDto
     */
    void getProfile(Model model, UserDto userDto);

    /**
     * Инменение пароля и почты пользователя
     * @param userDto
     * @param password
     * @param email
     */
    void updateProfile(UserDto userDto, String password, String email);

    /**
     * Отображение формы редактирования ролей пользователя
     * @param userDto
     * @param model
     */
    void userEditForm(UserDto userDto, Model model);


}
