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
     * @param passwordConfirm значение повторного ввода пароля при регистрации
     * @param userDto объект UserDto - сохраняемый пользователь
     * @param bindingResult объект BindingResult
     * @param model объект Model
     * @return при успешной регистрации редирект на url /login, иначе - страница registration.ftl
     */
    String addUser(String passwordConfirm, UserDto userDto, BindingResult bindingResult, Model model);

    /**
     * Активация пользователя. Если с момента регистрации до момента активации пользователя прошло 24 часа - пользователь удаляется
     * @param code Код активации
     * @param model объект Model
     */
    void activateUser(String code, Model model);

    /**
     * Найти всех пользователей
     * @param model объект Model
     */
    void findAll(Model model);

    /**
     * Изменение роли пользователя. В дополнение к роли USER (по-умолчанию) позволяет установить пользователю роли ANALYST и ADMIN
     * @param userId Идентификатор редактируемого пользователя
     * @param username имя пользователя
     * @param form Мапа с ролями
     */
    void changeUserRole(Long userId, String username, Map<String, String> form);

    /**
     * Получить профиль текущего пользователя
     * @param model объект Model
     * @param userDto объект UserDto
     */
    void getProfile(Model model, UserDto userDto);

    /**
     * Инменение пароля и почты пользователя
     * @param userDto объект UserDto
     * @param password новый пароль
     * @param email новый email
     */
    void updateProfile(UserDto userDto, String password, String email);

    /**
     * Отображение формы редактирования ролей пользователя
//     * @param userDto объект UserDto
     * @param model объект Model
     */
    void userEditForm(Long id, Model model);


}
