package com.bell.storage.service;

import com.bell.storage.configuration.ControllerUtils;
import com.bell.storage.dto.UserDto;
import com.bell.storage.model.Role;
import com.bell.storage.model.User;
import com.bell.storage.repository.UserRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * {@inheritDoc}
 */
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Value("${host.path}")
    private String hostPath;

    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public UserServiceImpl(MailSender mailSender, PasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    /**
     * ПОлучение юзера по имени.
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new RuntimeException("Empty parameters");
        }
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return ModelToDtoConverterUtils.convertUserToUserDto(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String addUser(String passwordConfirm, @Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (StringUtils.isBlank(passwordConfirm) || userDto == null || bindingResult == null || model == null) {
            throw new RuntimeException("Empty parameters");
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }
        if (userDto.getPassword() != null && !userDto.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords are different!");
        }
        if (isConfirmEmpty || bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        User userFromDb = userRepo.findByUsername(userDto.getUsername());
        if (userFromDb != null) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }
        User user = User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .active(true)
                .roles(Collections.singleton(Role.USER))
                .activationCode(UUID.randomUUID().toString())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .dateOfRegistration(System.currentTimeMillis())
                .build();

        userRepo.save(user);
        sendMessage(user);
        return "redirect:/login";

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activateUser(String code, Model model) {
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("Empty parameters");
        }
        User user = userRepo.findByActivationCode(code);
        if (System.currentTimeMillis() - user.getDateOfRegistration() > 86_400_000) {
            userRepo.delete(user);
            model.addAttribute("message", "Activation code is not valid or not found.");
            return;
        }
        user.setActivationCode(null);
        user.setActive(true);

        if (userRepo.save(user) != null) {
            model.addAttribute("message", "User successfully activated");
        } else {
            throw new RuntimeException("User activation failed");
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findAll(Model model) {
        model.addAttribute("users", userRepo.findAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeUserRole(UserDto userDto, String username, Map<String, String> form) {
        if (StringUtils.isBlank(username) || userDto == null || form == null) {
            throw new RuntimeException("Empty parameters");
        }
        User user = userRepo.findByUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        if (userRepo.save(user) == null) {
            throw new RuntimeException("User change failed");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getProfile(Model model, UserDto userDto) {
        model.addAttribute("username", userDto.getUsername());
        model.addAttribute("email", userDto.getEmail());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateProfile(UserDto userDto, String password, String email) {
        if (StringUtils.isBlank(password) || userDto == null || StringUtils.isBlank(email)) {
            throw new RuntimeException("Empty parameters");
        }
        User user = userRepo.findByUsername(userDto.getUsername());
        String userEmail = userDto.getEmail();
        if (!StringUtils.isBlank(email)) {
            if (!email.equals(userEmail) || !userEmail.equals(email)) {
                user.setEmail(email);
                user.setActivationCode(UUID.randomUUID().toString());
                user.setActive(false);
                user.setDateOfRegistration(System.currentTimeMillis());
            }
        }

        if (!StringUtils.isBlank(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if (userRepo.save(user) == null) {
            throw new RuntimeException("User update error");
        }
        sendMessage(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void userEditForm(UserDto userDto, Model model) {
        model.addAttribute("user", userDto);
        model.addAttribute("roles", Role.values());
    }

    private void sendMessage(User user) {
        if (!StringUtils.isBlank(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Bell File Storage. Please, visit next link: " + hostPath + "user/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

}
