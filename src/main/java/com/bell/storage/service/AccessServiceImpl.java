package com.bell.storage.service;

import com.bell.storage.dao.UserDao;
import com.bell.storage.dto.UserDto;
import com.bell.storage.model.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

/**
 * {@inheritDoc}
 */
@Service
public class AccessServiceImpl implements AccessService {

    private final UserDao userDao;

    public AccessServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     */
    public void getAccessAndRequest(UserDto currentUserDto, Model model) {
        if (currentUserDto == null || model == null) {
            throw new RuntimeException("Empty parameters");
        }
        User user = userDao.loadUserByUsername(currentUserDto.getUsername());

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
    public void requestToVisibleAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userDao.loadUserByUsername(currentUserDto.getUsername());
        User user = userDao.getUserById(userId);
        if (!user.getRequestsToVisible().contains(currentUser)
                && !user.getVisibleTenants().contains(currentUser)) {
            user.getRequestsToVisible().add(currentUser);
            userDao.saveUser(user);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void requestToDownloadAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userDao.loadUserByUsername(currentUserDto.getUsername());
        User user = userDao.getUserById(userId);

        if (!user.getRequestsToDownload().contains(currentUser)
                && !user.getDownloadTenants().contains(currentUser)) {
            user.getRequestsToDownload().add(currentUser);
            userDao.saveUser(user);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void confirmVisibleAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userDao.loadUserByUsername(currentUserDto.getUsername());
        User user = userDao.getUserById(userId);
        currentUser.getVisibleTenants().add(user);
        currentUser.getRequestsToVisible().remove(user);
        userDao.saveUser(currentUser);
    }

    /**
     * {@inheritDoc}
     */
    public void confirmDownloadAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userDao.loadUserByUsername(currentUserDto.getUsername());
        User user = userDao.getUserById(userId);
        currentUser.getDownloadTenants().add(user);
        currentUser.getRequestsToDownload().remove(user);
        userDao.saveUser(currentUser);
    }

    /**
     * {@inheritDoc}
     */
    public void refuseVisibleAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userDao.loadUserByUsername(currentUserDto.getUsername());
        User user = userDao.getUserById(userId);
        if (currentUser.getRequestsToVisible().contains(user)) {
            currentUser.getRequestsToVisible().remove(user);
            userDao.saveUser(currentUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void refuseDownloadAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userDao.loadUserByUsername(currentUserDto.getUsername());
        User user = userDao.getUserById(userId);
        if (currentUser.getRequestsToDownload().contains(user)) {
            currentUser.getRequestsToDownload().remove(user);
            userDao.saveUser(currentUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void cancelVisibleAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userDao.loadUserByUsername(currentUserDto.getUsername());
        User user = userDao.getUserById(userId);
        if (currentUser.getVisibleTenants().contains(user)) {
            currentUser.getVisibleTenants().remove(user);
            userDao.saveUser(currentUser);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void cancelDownloadAccess(UserDto currentUserDto, Long userId) {
        if (currentUserDto == null || userId == null || userId < 1) {
            throw new RuntimeException("Empty parameters");
        }
        User currentUser = userDao.loadUserByUsername(currentUserDto.getUsername());
        User user = userDao.getUserById(userId);
        if (currentUser.getDownloadTenants().contains(user)) {
            currentUser.getDownloadTenants().remove(user);
            userDao.saveUser(currentUser);
        }
    }
}
