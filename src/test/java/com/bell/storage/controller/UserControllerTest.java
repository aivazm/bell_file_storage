package com.bell.storage.controller;

import com.bell.storage.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/resources/templates/");
        viewResolver.setSuffix(".ftl");
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void registration() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void addUser() throws Exception {
        mockMvc.perform(post("/registration")
                .param("password2", "any password")
                .param("userDto", "any userDto")
                .param("bindingResult", "any email")
                .param("model", "any model"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));


    }

    @Test
    public void activate() throws Exception {
        mockMvc.perform(get("/user/activate/any_code"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void userList() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("userList"));
    }

    @Test
    public void userEditForm() throws Exception {
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("userEdit"));
    }

    @Test
    public void changeUserRole() throws Exception {
        mockMvc.perform(post("/user")
                .param("username", "any name")
                .param("form", "any form")
                .param("userId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user"));

    }

    @Test
    public void getProfile() throws Exception {
        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    public void updateProfile() throws Exception {
        mockMvc.perform(post("/user/profile")
                .param("userDto", "any userDto")
                .param("password", "any password")
                .param("email", "any email"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"));

    }
}