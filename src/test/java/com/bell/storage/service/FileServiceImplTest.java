package com.bell.storage.service;

import com.bell.storage.dao.UserDao;
import com.bell.storage.dao.UsersFileDao;
import com.bell.storage.dto.UserDto;
import com.bell.storage.model.User;
import com.bell.storage.model.UsersFile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileServiceImplTest {

    private final UsersFileDao usersFileDao = mock(UsersFileDao.class);
    private final UserDao userDao = mock(UserDao.class);
    private final Model model = mock(Model.class);
    private final MultipartFile file = mock(MultipartFile.class);
    private final UsersFile usersFileMock = mock(UsersFile.class);
    private final Long ID = 1L;
    private final User userMock = mock(User.class);
    private final User currentUserMock = mock(User.class);
    private final UserDto currentUserDtoMock = mock(UserDto.class);
    private final UserDto userDtoMock = mock(UserDto.class);
    private final String USERNAME = "username";

    private UserDto userDto;
    private UserDto currentUserDto;

    private FileServiceImpl service;
    private Set<UsersFile> usersFileSet;


    @Before
    public void setUp() throws IOException {

        byte[] array = new byte[]{(byte) 0xe0};
        Long ownerId = 2L;
        service = new FileServiceImpl(usersFileDao, userDao);
        User user = User.builder()
                .username("username")
                .password("password")
                .active(true)
                .requestsToVisible(new HashSet<>())
                .requestsToDownload(new HashSet<>())
                .visibleTenants(new HashSet<>())
                .downloadTenants(new HashSet<>())
                .build();
        userDto = UserDto.builder()
                .id(2L)
                .password("password")
                .active(true)
                .requestsToVisible(new HashSet<>())
                .requestsToDownload(new HashSet<>())
                .visibleTenants(new HashSet<>())
                .downloadTenants(new HashSet<>())
                .build();
        UsersFile usersFile = UsersFile.builder()
                .owner(user)
                .fileName(file.getOriginalFilename())
                .fileInBytes(array)
                .downloadCount(1)
                .build();
        currentUserDto = userDto;
        usersFileSet = new HashSet<>();
        usersFileSet.add(usersFile);
        usersFileMock.setOwner(user);
        currentUserDtoMock.setId(2L);

        when(userDao.loadUserByUsername(USERNAME)).thenReturn(user);
        when(file.getBytes()).thenReturn(array);
        String FILENAME = "filename";
        when(file.getOriginalFilename()).thenReturn(FILENAME);
        when(usersFileDao.saveFile(usersFileMock)).thenReturn(usersFileMock);
        when(userDao.getUserById(ID)).thenReturn(user);
        when(userDao.getUserById(currentUserDto.getId())).thenReturn(currentUserMock);
        when(usersFileDao.getFilesById(ID)).thenReturn(usersFileMock);
        when(usersFileMock.getOwner()).thenReturn(userMock);
        when(userMock.getId()).thenReturn(ownerId);
        when(currentUserDtoMock.getId()).thenReturn(ownerId);
        when(currentUserDtoMock.getUsername()).thenReturn(USERNAME);
        when(userDao.loadUserByUsername(USERNAME)).thenReturn(userMock);
        when(usersFileMock.getFileInBytes()).thenReturn(array);
        when(userDtoMock.getUsername()).thenReturn(USERNAME);
    }

    @Test
    public void getMyFiles() {
        service.getMyFiles(userDtoMock, model);
        verify(userDao).loadUserByUsername(userDtoMock.getUsername());
    }

    @Test(expected = RuntimeException.class)
    public void getMyFilesException() {
        service.getMyFiles(userDto, null);
    }

    @Test
    public void userFiles() {
        when(userDao.getUserById(ID)).thenReturn(userMock);
        when(userMock.getFiles()).thenReturn(usersFileSet);
        service.userFiles(currentUserDto, ID, model);
        verify(userDao).getUserById(ID);
        verify(userDao).getUserById(currentUserDto.getId());
    }

    @Test(expected = RuntimeException.class)
    public void userFilesException() {
        service.userFiles(null, ID, model);

    }

    @Test
    public void getAllUsers() {
        service.getAllUsers(currentUserDto, model);
        Mockito.verify(userDao, Mockito.times(1)).findAllUsers();
    }

    @Test
    public void deleteFileById() {
        service.deleteFileById(currentUserDtoMock, ID, model);
        Mockito.verify(usersFileDao, Mockito.times(1)).getFilesById(ID);
        Mockito.verify(usersFileDao, Mockito.times(1)).deleteFileById(ID);

    }

    @Test
    public void deleteFileByIdNotCurrentUser() {
        when(currentUserDtoMock.getId()).thenReturn(3L);
        service.deleteFileById(currentUserDtoMock, ID, model);
        Mockito.verify(usersFileDao, Mockito.times(1)).getFilesById(ID);
        Mockito.verify(usersFileDao, Mockito.times(1)).deleteFileById(ID);

    }

    @Test
    public void downloadFileById() {
        service.downloadFileById(ID, currentUserDtoMock, model);
        Mockito.verify(usersFileDao, Mockito.times(1)).getFilesById(ID);
        Mockito.verify(userDao, Mockito.times(1)).loadUserByUsername(USERNAME);
    }
}