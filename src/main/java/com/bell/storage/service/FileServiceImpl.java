package com.bell.storage.service;

import com.bell.storage.dto.UserDto;
import com.bell.storage.dto.UsersFileDto;
import com.bell.storage.model.User;
import com.bell.storage.model.UsersFile;
import com.bell.storage.repository.UserRepo;
import com.bell.storage.repository.UsersFileRepo;
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
import java.util.Optional;

/**
 * {@inheritDoc}
 */
@Service
public class FileServiceImpl implements FileService {

    private final UsersFileRepo usersFileRepo;
    private final UserRepo userRepo;

    public FileServiceImpl(UsersFileRepo usersFileRepo, UserRepo userRepo) {
        this.usersFileRepo = usersFileRepo;
        this.userRepo = userRepo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getMyFiles(UserDto userDto, Model model) {
        if (userDto == null || model == null) {
            throw new RuntimeException("Empty parameters");
        }
        getListOfFilesAndRequests(userRepo.findByUsername(userDto.getUsername()), model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFile(UserDto userDto,
                        UsersFileDto usersFileDto,
                        Model model,
                        MultipartFile file) {
        if (userDto == null || usersFileDto == null || model == null || file == null) {
            throw new RuntimeException("Empty parameters");
        }
        User user = userRepo.findByUsername(userDto.getUsername());
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

            usersFileRepo.save(usersFile);
        }
        getListOfFilesAndRequests(user, model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void userFiles(UserDto currentUserDto,
                          Long id,
                          Model model) {
        if (currentUserDto == null || id == null || id < 1 || model == null) {
            throw new RuntimeException("Empty parameters");
        }
        User user;
        User currentUser;
        Optional<User> userOptional = userRepo.findById(id);
        Optional<User> currentUserOptional = userRepo.findById(currentUserDto.getId());

        if (userOptional.isPresent() & currentUserOptional.isPresent()) {
            user = userOptional.get();
            currentUser = currentUserOptional.get();
        } else {
            throw new RuntimeException("User not found");
        }

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
    @Override
    public void getAllUsers(UserDto currentUser, Model model) {
        if (currentUser == null || model == null) {
            throw new RuntimeException("Empty parameters");
        }
        Map<UserDto, Integer> users = new HashMap<>();
        UserDto userDto;
        for (User user : userRepo.findAll()) {
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
    @Override
    public String deleteFileById(UserDto currentUserDto, Long id, Model model) {
        if (currentUserDto == null || id == null || id < 1 || model == null) {
            throw new RuntimeException("Empty parameters");
        }
        String result;
        UsersFile usersFile;
        Optional<UsersFile> usersFileOptional = usersFileRepo.findById(id);

        if (usersFileOptional.isPresent()) {
            usersFile = usersFileOptional.get();
        } else {
            throw new RuntimeException("File not found");
        }
        Long ownerId = usersFile.getOwner().getId();
        usersFileRepo.deleteById(id);
        if (ownerId.equals(currentUserDto.getId())) {
            result = "redirect:/main";
        } else {
            result = "redirect:/user-files/" + ownerId;
        }
        getListOfFilesAndRequests(userRepo.findByUsername(currentUserDto.getUsername()), model);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity downloadFileById(Long id, UserDto currentUserDto, Model model) {
        if (currentUserDto == null || id == null || id < 1 || model == null) {
            throw new RuntimeException("Empty parameters");
        }

        UsersFile usersFile;
        Optional<UsersFile> usersFileOptional = usersFileRepo.findById(id);

        if (usersFileOptional.isPresent()) {
            usersFile = usersFileOptional.get();
        } else {
            throw new RuntimeException("File not found");
        }
        getListOfFilesAndRequests(userRepo.findByUsername(currentUserDto.getUsername()), model);

        usersFile.setDownloadCount(usersFile.getDownloadCount() + 1);
        usersFileRepo.save(usersFile);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + usersFile.getFileName())
                .body(new ByteArrayResource(usersFile.getFileInBytes()));
    }

    private void getListOfFilesAndRequests(User user, Model model) {
        model.addAttribute("numberOfRequests", user.getRequestsToVisible().size() + user.getRequestsToDownload().size());
        model.addAttribute("files", usersFileRepo.findByOwner(user));
    }
}
