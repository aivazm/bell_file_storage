package com.bell.storage.controller;

import com.bell.storage.service.AccessService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class AccessControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccessService accessService;
    private final String USER_ID = "1";

    @InjectMocks
    private AccessController accessController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accessController)
                .build();
    }

    @Test
    public void getAccessAndRequest() throws Exception {
        mockMvc.perform(get("/accessAndRequestPage/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("accessAndRequestPage"));


    }

    @Test
    public void requestToVisibleAccess() throws Exception {
        mockMvc.perform(get("/request-visible-access/" + USER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/all-users"))
                .andReturn();
    }

    @Test
    public void requestToDownloadAccess() throws Exception {
        mockMvc.perform(get("/request-download-access/" + USER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/all-users"))
                .andReturn();
    }

    @Test
    public void confirmVisibleAccess() throws Exception {
        mockMvc.perform(get("/confirm-visible-access/" + USER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessAndRequestPage/"))
                .andReturn();
    }

    @Test
    public void confirmDownloadAccess() throws Exception {
        mockMvc.perform(get("/confirm-download-access/" + USER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessAndRequestPage/"))
                .andReturn();
    }

    @Test
    public void refuseVisibleAccess() throws Exception {
        mockMvc.perform(get("/refuse-visible-access/" + USER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessAndRequestPage/"))
                .andReturn();
    }

    @Test
    public void refuseDownloadAccess() throws Exception {
        mockMvc.perform(get("/refuse-download-access/" + USER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessAndRequestPage/"))
                .andReturn();

    }

    @Test
    public void cancelVisibleAccess() throws Exception {
        mockMvc.perform(get("/cancel-visible-access/" + USER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessAndRequestPage/"))
                .andReturn();
    }

    @Test
    public void cancelDownloadAccess() throws Exception {
        mockMvc.perform(get("/cancel-download-access/" + USER_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessAndRequestPage/"))
                .andReturn();
    }
}