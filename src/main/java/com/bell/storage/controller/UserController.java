package com.bell.storage.controller;

import com.bell.storage.dto.UserDto;
import com.bell.storage.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

/**
 * Контроллер операций с пользователем
 */
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Регистрация пользователя
     * @return String имя страницы registration.ftl
     */
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    /**
     * Регистрация пользователя
     * @param passwordConfirm Значение повторного ввода пароля
     * @param userDto объект класса UserDto
     * @param bindingResult объект BindingResult
     * @param model объект Model
     * @return результат работы метода addUser(String passwordConfirm, UserDto userDto, BindingResult bindingResult, Model model)
     */
    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @Valid UserDto userDto,
            BindingResult bindingResult,
            Model model) {

        return userService.addUser(passwordConfirm, userDto, bindingResult, model);
    }

    /**
     * Активация пользователя
     * @param model объект Model
     * @param code код активации
     * @return страница login.ftl
     */
    @GetMapping("/user/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        userService.activateUser(code, model);
        return "login";
    }

    /**
     * Представление списка пользователей
     * @param model объект Model
     * @return страница userList.ftl
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user")
    public String userList(Model model) {
        userService.findAll(model);
        return "userList";
    }

    /**
     * Форма с ролями пользователя
     * @param id идентификатор редактируемого пользователя
     * @param model объект Model
     * @return страница userEdit.ftl
     */

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/{id}")
    public String userEditForm(@PathVariable Long id, Model model) {
        userService.userEditForm(id, model);
        return "userEdit";
    }

    /**
     * Изменение ролей пользователя
     * @param username имя пользователя
     * @param form объект Map с ролями и менами пользователя
     * @param userId идентификатор редактируемого пользователя
     * @return редирект на url /user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user")
    public String changeUserRole(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam Long userId
    ) {
        userService.changeUserRole(userId, username, form);
        return "redirect:/user";
    }

    /**
     * Профиль пользователя
     * @param model объект Model
     * @param userDto объект UserDto
     * @return страница profile.ftl
     */
    @GetMapping("/user/profile")
    public String getProfile(Model model, @AuthenticationPrincipal UserDto userDto) {
        userService.getProfile(model, userDto);
        return "profile";
    }

    /**
     * Изменение пароля и почты пользователя
     * @param userDto объект UserDto
     * @param password Новый пароль
     * @param email Новый email
     * @return редирект на url /user/profile
     */
    @PostMapping("/user/profile")
    public String updateProfile(
            @AuthenticationPrincipal UserDto userDto,
            @RequestParam String password,
            @RequestParam String email
    ) {
        userService.updateProfile(userDto, password, email);

        return "redirect:/user/profile";
    }
}
