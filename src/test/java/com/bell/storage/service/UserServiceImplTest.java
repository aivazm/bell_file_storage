package com.bell.storage.service;

import com.bell.storage.dto.UserDto;
import com.bell.storage.model.Role;
import com.bell.storage.model.User;
import com.bell.storage.repository.UserRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private final MailSender mailSender = mock(MailSender.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final UserRepo userRepoMock = mock(UserRepo.class);
    private final User userMock = mock(User.class);
    private final UserDto userDtoMock = mock(UserDto.class);
    private final BindingResult bindingResult = mock(BindingResult.class);
    private final Model model = mock(Model.class);
    private Iterable<User> users;
    private Map<String, String> form;

    private UserServiceImpl service;
    private final String USERNAME = "username";
    private final String PASSWORD_CONFIRM = "password";
    private final String CODE = "code";
    private final String EMAIL = "email";


    @Before
    public void setUp() {
        service = new UserServiceImpl(mailSender, passwordEncoder, userRepoMock);
        userDtoMock.setPassword(PASSWORD_CONFIRM);
        userDtoMock.setUsername(USERNAME);
        users = new ArrayList<>();
        form = new HashMap<>();
        userMock.setDateOfRegistration(System.currentTimeMillis());
        when(userRepoMock.findByUsername(USERNAME)).thenReturn(userMock);
        when(userDtoMock.getUsername()).thenReturn(USERNAME);
        when(userDtoMock.getPassword()).thenReturn(PASSWORD_CONFIRM);
        when(userRepoMock.findByActivationCode(CODE)).thenReturn(userMock);
        when(userRepoMock.save(userMock)).thenReturn(userMock);
        when(userRepoMock.findAll()).thenReturn(users);
        when(userDtoMock.getEmail()).thenReturn(EMAIL);

    }

    @Test
    public void loadUserByUsername() {
        service.loadUserByUsername(USERNAME);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
    }

    @Test(expected = RuntimeException.class)
    public void loadUserByUsernameException() {
        when(userRepoMock.findByUsername(USERNAME)).thenReturn(null);
        service.loadUserByUsername(USERNAME);
    }

    @Test
    public void addUser() {
        service.addUser(PASSWORD_CONFIRM, userDtoMock, bindingResult, model);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);

    }

    @Test
    public void addUserDifferentPasswords() {
        when(userDtoMock.getPassword()).thenReturn("no_password");
        service.addUser(PASSWORD_CONFIRM, userDtoMock, bindingResult, model);
        Mockito.verify(model, Mockito.times(1)).addAttribute("passwordError", "Passwords are different!");
    }

    @Test
    public void activateUser() {
        when(userMock.getDateOfRegistration()).thenReturn(System.currentTimeMillis());
        service.activateUser(CODE, model);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByActivationCode(CODE);
        Mockito.verify(userMock, Mockito.times(1)).setActivationCode(null);
        Mockito.verify(userMock, Mockito.times(1)).setActive(true);
        Mockito.verify(userRepoMock, Mockito.times(1)).save(userMock);
        Mockito.verify(model, Mockito.times(1)).addAttribute("message", "User successfully activated");
    }

    @Test(expected = RuntimeException.class)
    public void activateUserException() {
        when(userMock.getDateOfRegistration()).thenReturn(System.currentTimeMillis());
        when(userRepoMock.save(userMock)).thenReturn(null);
        service.activateUser(CODE, model);
    }

    @Test
    public void activateUserCodeNotFound() {
        service.activateUser(CODE, model);
        Mockito.verify(model, Mockito.times(1)).addAttribute("message", "Activation code is not valid or not found.");

    }


    @Test
    public void findAll() {
        service.findAll(model);
        Mockito.verify(model, Mockito.times(1)).addAttribute("users", users);
    }

    @Test
    public void changeUserRole() {
        service.changeUserRole(userDtoMock, USERNAME, form);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
        Mockito.verify(userRepoMock, Mockito.times(1)).save(userMock);
    }

    @Test
    public void getProfile() {
        service.getProfile(model, userDtoMock);
        Mockito.verify(model, Mockito.times(1)).addAttribute("username", USERNAME);
        Mockito.verify(model, Mockito.times(1)).addAttribute("email", EMAIL);
    }

    @Test
    public void updateProfile() {
        service.updateProfile(userDtoMock, PASSWORD_CONFIRM, EMAIL);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(PASSWORD_CONFIRM);
        Mockito.verify(userRepoMock, Mockito.times(1)).save(userMock);
    }

    @Test
    public void userEditForm() {
        service.userEditForm(userDtoMock, model);
        Mockito.verify(model, Mockito.times(1)).addAttribute("user", userDtoMock);
        Mockito.verify(model, Mockito.times(1)).addAttribute("roles", Role.values());

    }
}