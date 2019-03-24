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
     * @return
     */
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    /**
     * Регистрация пользователя
     * @param passwordConfirm
     * @param userDto
     * @param bindingResult
     * @param model
     * @return
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
     * @param model
     * @param code
     * @return
     */
    @GetMapping("/user/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        userService.activateUser(code, model);
        return "login";
    }

    /**
     * Представление списка пользователей
     * @param model
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user")
    public String userList(Model model) {
        userService.findAll(model);
        return "userList";
    }

    /**
     * Форма с ролями пользователя
     * @param userDto
     * @param model
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/{user}")
    public String userEditForm(@PathVariable(name = "user") UserDto userDto, Model model) {
        userService.userEditForm(userDto, model);
        return "userEdit";
    }

    /**
     * Изменение ролей пользователя
     * @param username
     * @param form
     * @param userDto
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user")
    public String changeUserRole(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") UserDto userDto
    ) {
        userService.changeUserRole(userDto, username, form);
        return "redirect:/user";
    }

    /**
     * Профиль пользователя
     * @param model
     * @param userDto
     * @return
     */
    @GetMapping("/user/profile")
    public String getProfile(Model model, @AuthenticationPrincipal UserDto userDto) {
        userService.getProfile(model, userDto);
        return "profile";
    }

    /**
     * Изменение пароля и почты пользователя
     * @param userDto
     * @param password
     * @param email
     * @return
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
