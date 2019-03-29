package com.bell.storage.service;

import com.bell.storage.dto.UserDto;
import com.bell.storage.model.User;
import com.bell.storage.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * {@inheritDoc}
 */
@Service
public class AccessServiceImpl implements AccessService {

    private final UserRepo userRepo;

    public AccessServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getAccessAndRequest(UserDto currentUserDto, Model model) {
        if (currentUserDto == null || model == null) {
            throw new RuntimeException("Empty parameters");
        }
        User user = userRepo.findByUsername(currentUserDto.getUsername());

        Set<UserDto> requestsToVisibleDto = new HashSet<>();
        Set<UserDto> requestsToDownloadDto = new HashSet<>();
        Set<UserDto> accessToVisibleDto = new HashSet<>();
        Set<UserDto> accessToDownloadDto = new HashSet<>();

        for (User u : user.getRequestsToVisible()) {
            requestsToVisibleDto.add(ModelToDtoConverterUtils.convertUserToUserDto(u));
        }
        for (User u : user.getRequestsToDownload()) {
            requestsToDownloadDto.add(ModelToDtoConverterUtils.convertUserToUserDto(u));
        }
        for (User u : user.getVisibleTenants()) {
            accessToVisibleDto.add(ModelToDtoConverterUtils.convertUserToUserDto(u));
        }
        for (User u : user.getDownloadTenants()) {
            accessToDownloadDto.add(ModelToDtoConverterUtils.convertUserToUserDto(u));
        }

        if (!requestsToVisibleDto.isEmpty()) {
            model.addAttribute("requestsToVisible", requestsToVisibleDto);
        }
        if (!requestsToDownloadDto.isEmpty()) {
            model.addAttribute("requestsToDownload", requestsToDownloadDto);
        }
        if (!accessToVisibleDto.isEmpty()) {
            model.addAttribute("accessToVisible", accessToVisibleDto);
        }
        if (!accessToDownloadDto.isEmpty()) {
            model.addAttribute("accessToDownload", accessToDownloadDto);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void requestToVisibleAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userRepo.findByUsername(currentUserDto.getUsername());
        User user = getUserById(userId);
        if (!user.getRequestsToVisible().contains(currentUser)
                && !user.getVisibleTenants().contains(currentUser)) {
            user.getRequestsToVisible().add(currentUser);
            userRepo.save(user);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void requestToDownloadAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userRepo.findByUsername(currentUserDto.getUsername());
        User user = getUserById(userId);
        if (!user.getRequestsToDownload().contains(currentUser)
                && !user.getDownloadTenants().contains(currentUser)) {
            user.getRequestsToDownload().add(currentUser);
            userRepo.save(user);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void confirmVisibleAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userRepo.findByUsername(currentUserDto.getUsername());
        User user = getUserById(userId);
        currentUser.getVisibleTenants().add(user);
        currentUser.getRequestsToVisible().remove(user);
        userRepo.save(currentUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void confirmDownloadAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userRepo.findByUsername(currentUserDto.getUsername());
        User user = getUserById(userId);
        currentUser.getDownloadTenants().add(user);
        currentUser.getRequestsToDownload().remove(user);
        userRepo.save(currentUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refuseVisibleAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userRepo.findByUsername(currentUserDto.getUsername());
        User user = getUserById(userId);
        if (currentUser.getRequestsToVisible().contains(user)) {
            currentUser.getRequestsToVisible().remove(user);
            userRepo.save(currentUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refuseDownloadAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userRepo.findByUsername(currentUserDto.getUsername());
        User user = getUserById(userId);
        if (currentUser.getRequestsToDownload().contains(user)) {
            currentUser.getRequestsToDownload().remove(user);
            userRepo.save(currentUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelVisibleAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userRepo.findByUsername(currentUserDto.getUsername());
        User user = getUserById(userId);
        if (currentUser.getVisibleTenants().contains(user)) {
            currentUser.getVisibleTenants().remove(user);
            userRepo.save(currentUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelDownloadAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userRepo.findByUsername(currentUserDto.getUsername());
        User user = getUserById(userId);
        if (currentUser.getDownloadTenants().contains(user)) {
            currentUser.getDownloadTenants().remove(user);
            userRepo.save(currentUser);
        }
    }

    private User getUserById (Long id){
        Optional<User> optional = userRepo.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
