package com.bell.storage.service;

import com.bell.storage.dto.UserDto;
import com.bell.storage.model.User;
import com.bell.storage.repository.UserRepo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AccessServiceImplTest {

    private final UserRepo userRepoMock = mock(UserRepo.class);
    private final UserDto currentUserDtoMock = mock(UserDto.class);
    private final Model model = mock(Model.class);
    private final User userMock = mock(User.class);
    private final User currentUserMock = mock(User.class);

    private final String USERNAME = "username";

    private AccessServiceImpl service;
    private Long userId = 1L;


    @Before
    public void setUp() {
        service = new AccessServiceImpl(userRepoMock);
        Set<User> requestsToVisible = new HashSet<>();
        Set<User> requestsToDownload = new HashSet<>();
        Set<User> accessToVisible = new HashSet<>();
        Set<User> accessToDownload = new HashSet<>();
        userMock.setRequestsToVisible(requestsToVisible);
        userMock.setRequestsToDownload(requestsToDownload);
        userMock.setVisibleTenants(accessToVisible);
        userMock.setDownloadTenants(accessToDownload);
        currentUserMock.setRequestsToVisible(requestsToVisible);
        currentUserMock.setRequestsToDownload(requestsToDownload);
        Optional<User> userOptional = Optional.of(userMock);
        when(userRepoMock.findByUsername(USERNAME)).thenReturn(userMock);
        when(currentUserDtoMock.getUsername()).thenReturn(USERNAME);
        when(userRepoMock.findById(userId)).thenReturn(userOptional);
        when(userMock.getRequestsToVisible()).thenReturn(requestsToVisible);
        when(userMock.getRequestsToDownload()).thenReturn(requestsToDownload);
        when(userMock.getVisibleTenants()).thenReturn(accessToVisible);
        when(userMock.getDownloadTenants()).thenReturn(accessToDownload);

    }

    @Test
    public void getAccessAndRequest() {
        service.getAccessAndRequest(currentUserDtoMock, model);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
    }

    @Test
    public void requestToVisibleAccess() {
        service.requestToVisibleAccess(currentUserDtoMock,userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
        Mockito.verify(userRepoMock, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).save(userMock);

    }

    @Test
    public void requestToDownloadAccess() {
        service.requestToDownloadAccess(currentUserDtoMock,userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
        Mockito.verify(userRepoMock, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).save(userMock);
    }

    @Test
    public void confirmVisibleAccess() {
        service.confirmVisibleAccess(currentUserDtoMock,userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
        Mockito.verify(userRepoMock, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).save(userMock);

    }

    @Test
    public void confirmDownloadAccess() {
        service.confirmDownloadAccess(currentUserDtoMock,userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
        Mockito.verify(userRepoMock, Mockito.times(1)).findById(userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).save(userMock);
    }

    @Test
    public void refuseVisibleAccess() {
        service.refuseVisibleAccess(currentUserDtoMock,userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
        Mockito.verify(userRepoMock, Mockito.times(1)).findById(userId);

    }

    @Test
    public void refuseDownloadAccess() {
        service.refuseDownloadAccess(currentUserDtoMock,userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
        Mockito.verify(userRepoMock, Mockito.times(1)).findById(userId);
    }

    @Test
    public void cancelVisibleAccess() {
        service.cancelVisibleAccess(currentUserDtoMock,userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
        Mockito.verify(userRepoMock, Mockito.times(1)).findById(userId);
    }

    @Test
    public void cancelDownloadAccess() {
        service.cancelDownloadAccess(currentUserDtoMock,userId);
        Mockito.verify(userRepoMock, Mockito.times(1)).findByUsername(USERNAME);
        Mockito.verify(userRepoMock, Mockito.times(1)).findById(userId);
    }

}
