package com.bell.storage.service;

import com.bell.storage.dto.UserDto;
import com.bell.storage.dto.UsersFileDto;
import com.bell.storage.model.User;
import com.bell.storage.model.UsersFile;
import com.bell.storage.repository.UserRepo;
import com.bell.storage.repository.UsersFileRepo;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileServiceImplTest {

    private final UsersFileRepo usersFileRepoMock = mock(UsersFileRepo.class);
    private final UserRepo userRepoMock = mock(UserRepo.class);
    private final Model model = mock(Model.class);
    private final MultipartFile file = mock(MultipartFile.class);
    private final UsersFile usersFileMock = mock(UsersFile.class);
    private final Long ID = 1L;
    private final User userMock = mock(User.class);
    private final User currentUserMock = mock(User.class);
    private final UserDto currentUserDtoMock = mock(UserDto.class);
    private final UserDto userDtoMock = mock(UserDto.class);
    private final UsersFileDto usersFileDtoMock = mock(UsersFileDto.class);
    private final String USERNAME = "username";
    private UserDto userDto;
    private UserDto currentUserDto;

    private FileServiceImpl service;
    private Set<UsersFile> usersFileSet;


    @Before
    public void setUp() throws IOException {

        byte[] array = new byte[]{(byte) 0xe0};
        Long ownerId = 2L;
        service = new FileServiceImpl(usersFileRepoMock, userRepoMock);
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
        Long CURRENT_USER_ID = 2L;
        currentUserDtoMock.setId(CURRENT_USER_ID);

        when(userRepoMock.findByUsername(USERNAME)).thenReturn(user);
        when(file.getBytes()).thenReturn(array);
        String FILENAME = "filename";
        when(file.getOriginalFilename()).thenReturn(FILENAME);
        when(usersFileRepoMock.save(usersFileMock)).thenReturn(usersFileMock);

        Optional<User> userOptional = Optional.of(userMock);
        Optional<User> currentUserOptional = Optional.of(currentUserMock);
        when(userRepoMock.findById(ID)).thenReturn(userOptional);
        when(userRepoMock.findById(CURRENT_USER_ID)).thenReturn(currentUserOptional);

        Optional<UsersFile> usersFileOptional = Optional.of(usersFileMock);
        when(usersFileRepoMock.findById(ID)).thenReturn(usersFileOptional);

        when(usersFileMock.getOwner()).thenReturn(userMock);
        when(userMock.getId()).thenReturn(ownerId);
        when(currentUserDtoMock.getId()).thenReturn(ownerId);
        when(currentUserDtoMock.getUsername()).thenReturn(USERNAME);
        when(userRepoMock.findByUsername(USERNAME)).thenReturn(userMock);
        when(usersFileMock.getFileInBytes()).thenReturn(array);
        when(userDtoMock.getUsername()).thenReturn(USERNAME);
    }

    @Test
    public void addFile() {
        when(userDtoMock.getUsername()).thenReturn(USERNAME);
        when(userDtoMock.isActive()).thenReturn(true);
        service.addFile(userDtoMock,usersFileDtoMock,model,file);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
        Mockito.verify(usersFileRepoMock).save(Mockito.isA(UsersFile.class));
    }

    @Test(expected = RuntimeException.class)
    public void addFileException() throws IOException {
        when(userDtoMock.getUsername()).thenReturn(USERNAME);
        when(userDtoMock.isActive()).thenReturn(true);
        when(file.getBytes()).thenThrow(new IOException("Expected exception"));
        service.addFile(userDtoMock,usersFileDtoMock,model,file);
    }

    @Test
    public void getMyFiles() {
        service.getMyFiles(userDtoMock, model);
        verify(userRepoMock).findByUsername(userDtoMock.getUsername());
    }

    @Test(expected = RuntimeException.class)
    public void getMyFilesException() {
        service.getMyFiles(userDto, null);
    }

    @Test
    public void userFiles() {
        when(userMock.getFiles()).thenReturn(usersFileSet);
        service.userFiles(currentUserDto, ID, model);
        verify(userRepoMock).findById(ID);
        verify(userRepoMock).findById(currentUserDto.getId());
    }

    @Test(expected = RuntimeException.class)
    public void userFilesException() {
        service.userFiles(null, ID, model);

    }

    @Test
    public void getAllUsers() {
        service.getAllUsers(currentUserDto, model);
        Mockito.verify(userRepoMock, Mockito.times(1)).findAll();
    }

    @Test
    public void deleteFileById() {
        service.deleteFileById(currentUserDtoMock, ID, model);
        Mockito.verify(usersFileRepoMock, Mockito.times(1)).findById(ID);
        Mockito.verify(usersFileRepoMock, Mockito.times(1)).deleteById(ID);

    }

    @Test
    public void deleteFileByIdNotCurrentUser() {
        when(currentUserDtoMock.getId()).thenReturn(3L);
        service.deleteFileById(currentUserDtoMock, ID, model);
        Mockito.verify(usersFileRepoMock, Mockito.times(1)).findById(ID);
        Mockito.verify(usersFileRepoMock, Mockito.times(1)).deleteById(ID);

    }

    @Test
    public void downloadFileById() {
        service.downloadFileById(ID, currentUserDtoMock, model);
        Mockito.verify(usersFileRepoMock, Mockito.times(1)).findById(ID);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
    }

}