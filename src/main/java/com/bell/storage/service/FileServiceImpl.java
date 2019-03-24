package com.bell.storage.service;

import com.bell.storage.dao.UserDao;
import com.bell.storage.dao.UsersFileDao;
import com.bell.storage.dto.UserDto;
import com.bell.storage.dto.UsersFileDto;
import com.bell.storage.model.User;
import com.bell.storage.model.UsersFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Service
public class FileServiceImpl implements FileService {

    private final UsersFileDao usersFileDao;
    private final UserDao userDao;

    public FileServiceImpl(UsersFileDao usersFileDao, UserDao userDao) {
        this.usersFileDao = usersFileDao;
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     */
    public void getMyFiles(UserDto userDto, Model model) {
        if (userDto == null || model == null) {
            throw new RuntimeException("Empty parameters");
        }
        getListOfFilesAndRequests(userDao.loadUserByUsername(userDto.getUsername()), model);
    }

    /**
     * {@inheritDoc}
     */
    public void addFile(UserDto userDto,
                        UsersFileDto usersFileDto,
                        Model model,
                        MultipartFile file) {
        if (userDto == null || usersFileDto == null || model == null || file == null) {
            throw new RuntimeException("Empty parameters");
        }
        User user = userDao.loadUserByUsername(userDto.getUsername());
        if (userDto.isActive() && !StringUtils.isBlank(file.getOriginalFilename())) {
            try {
                usersFileDto.setFileInBytes(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error while trying get bytes form file: ", e);
            }

            UsersFile usersFile = UsersFile.builder()
                    .fileName(file.getOriginalFilename())
                    .owner(user)
                    .fileInBytes(usersFileDto.getFileInBytes())
                    .downloadCount(usersFileDto.getDownloadCount())
                    .build();

            usersFileDao.saveFile(usersFile);
        }
        getListOfFilesAndRequests(user, model);
    }

    /**
     * {@inheritDoc}
     */
    public void userFiles(UserDto currentUserDto,
                          Long id,
                          Model model) {
        if (currentUserDto == null || id == null || id < 1 || model == null) {
            throw new RuntimeException("Empty parameters");
        }

        User user = userDao.getUserById(id);
        User currentUser = userDao.getUserById(currentUserDto.getId());

        Map<UsersFile, Integer> files = new HashMap<>();
        for (UsersFile usersFile : user.getFiles()) {
            files.put(usersFile, usersFile.getDownloadCount());
        }
        model.addAttribute("files", files);
        model.addAttribute("isCurrentUserOrAdmin", currentUser.equals(user) || currentUser.isAdmin());
        model.addAttribute("isVisibleAccessOrAnalyst", user.getVisibleTenants().contains(currentUser) || currentUser.isAnalyst());
        model.addAttribute("isDownloadAccess", user.getDownloadTenants().contains(currentUser) || currentUser.isAdmin());
    }

    /**
     * {@inheritDoc}
     */
    public void getAllUsers(UserDto currentUser, Model model) {
        if (currentUser == null || model == null) {
            throw new RuntimeException("Empty parameters");
        }
        Map<UserDto, Integer> users = new HashMap<>();
        UserDto userDto;
        for (User user : userDao.findAllUsers()) {
            userDto = ModelToDtoConverterUtils.convertUserToUserDto(user);
            users.put(userDto, userDto.getFiles().size());
        }
        model.addAttribute("users", users);
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("numberOfUsers", users.size());
    }

    /**
     * {@inheritDoc}
     */
    public String deleteFileById(UserDto currentUserDto, Long id, Model model) {
        if (currentUserDto == null || id == null || id < 1 || model == null) {
            throw new RuntimeException("Empty parameters");
        }
        String result;
        Long ownerId = usersFileDao.getFilesById(id).getOwner().getId();
        usersFileDao.deleteFileById(id);
        if (ownerId.equals(currentUserDto.getId())) {
            result = "redirect:/main";
        } else {
            result = "redirect:/user-files/" + ownerId;
        }
        getListOfFilesAndRequests(userDao.loadUserByUsername(currentUserDto.getUsername()), model);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseEntity downloadFileById(Long id, UserDto userDto, Model model) {
        if (userDto == null || id == null || id < 1 || model == null) {
            throw new RuntimeException("Empty parameters");
        }

        UsersFile usersFile = usersFileDao.getFilesById(id);
        getListOfFilesAndRequests(userDao.loadUserByUsername(userDto.getUsername()), model);

        usersFile.setDownloadCount(usersFile.getDownloadCount() + 1);
        if (usersFileDao.saveFile(usersFile) == null) {
            throw new RuntimeException("Error saving file");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + usersFile.getFileName())
                .body(new ByteArrayResource(usersFile.getFileInBytes()));
    }

    private void getListOfFilesAndRequests(User user, Model model) {
        model.addAttribute("numberOfRequests", user.getRequestsToVisible().size() + user.getRequestsToDownload().size());
        model.addAttribute("files", usersFileDao.getFilesByOwner(user));
    }
}
