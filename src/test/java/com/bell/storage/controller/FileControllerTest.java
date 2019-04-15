package com.bell.storage.controller;

import com.bell.storage.service.FileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
public class FileControllerTest {

    private MockMvc mockMvc;
    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    private final long ID = 1;

    @Before
    public void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/resources/templates/");
        viewResolver.setSuffix(".ftl");
        mockMvc = MockMvcBuilders.standaloneSetup(fileController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void greeting() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("greeting"));
    }

    @Test
    public void getMyFiles() throws Exception {
        mockMvc.perform(get("/main"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"));

    }

    @Test
    public void addFile() throws Exception {
        MockMultipartFile fileMock = new MockMultipartFile("file", "filename.txt", "text/plain", "some text".getBytes());
        mockMvc.perform(multipart("/main")
                .file(fileMock)
                .param("currentUserDto", "any userDto")
                .param("usersFileDto", "any usersFileDto")
                .param("model", "any model")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("main"));

    }

    @Test
    public void userFiles() throws Exception {
        mockMvc.perform(get("/user-files/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("userFiles"));
    }

    @Test
    public void getAllUsers() throws Exception {
        mockMvc.perform(get("/all-users"))
                .andExpect(status().isOk())
                .andExpect(view().name("allUsers"));
    }

    @Test
    public void deleteFileById() throws Exception {
        mockMvc.perform(get("/delete-file/1"))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    public void downloadFileById() throws Exception {
        mockMvc.perform(get("/download-file/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}