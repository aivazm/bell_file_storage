package com.bell.storage.service;

import com.bell.storage.dto.UserDto;
import com.bell.storage.dto.UsersFileDto;
import com.bell.storage.model.User;
import com.bell.storage.model.UsersFile;

import java.util.HashSet;
import java.util.Set;

class ModelToDtoConverterUtils {
    static UserDto convertUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .active(user.isActive())
                .roles(user.getRoles())
                .activationCode(user.getActivationCode())
                .password(user.getPassword())
                .dateOfRegistration(user.getDateOfRegistration())
                .downloadTenants(convertSetUserToSetUserDto(user.getDownloadTenants()))
                .visibleTenants(convertSetUserToSetUserDto(user.getVisibleTenants()))
                .requestsToDownload(convertSetUserToSetUserDto(user.getRequestsToDownload()))
                .requestsToVisible(convertSetUserToSetUserDto(user.getRequestsToVisible()))
                .files(convertSetUsersFileToSetUsersFileDto(user.getFiles()))
                .build();
    }

    private static UsersFileDto convertUsersFileToUsersFileDto(UsersFile usersFile) {
        return UsersFileDto.builder()
                .id(usersFile.getId())
                .fileName(usersFile.getFileName())
                .owner(usersFile.getOwner())
                .fileInBytes(usersFile.getFileInBytes())
                .downloadCount(usersFile.getDownloadCount())
                .build();
    }

    private static Set<UserDto> convertSetUserToSetUserDto(Set<User> userSet) {
        Set<UserDto> userDtoSet = new HashSet<>();
        for (User user : userSet) {
            userDtoSet.add(convertUserToUserDto(user));
        }
        return userDtoSet;
    }

    private static Set<UsersFileDto> convertSetUsersFileToSetUsersFileDto(Set<UsersFile> usersFileSet) {
        Set<UsersFileDto> usersFileDtoSet = new HashSet<>();
        for (UsersFile usersFile : usersFileSet) {
            usersFileDtoSet.add(convertUsersFileToUsersFileDto(usersFile));
        }
        return usersFileDtoSet;
    }
}
