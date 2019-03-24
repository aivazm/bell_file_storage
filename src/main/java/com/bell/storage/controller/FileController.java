package com.bell.storage.controller;

import com.bell.storage.dto.UserDto;
import com.bell.storage.dto.UsersFileDto;
import com.bell.storage.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер запросов на отображение и операции с файлами
 */
@Controller
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Обработчик домашней страницы сервиса
     *
     * @return
     */
    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    /**
     * Домашняя страница пользователя. Представляет файлы пользователя
     *
     * @param currentUserDto
     * @param model
     * @return
     */
    @GetMapping("/main")
    public String getMyFiles(@AuthenticationPrincipal UserDto currentUserDto, Model model) {
        fileService.getMyFiles(currentUserDto, model);
        return "main";
    }

    /**
     * Добавление файла.
     *
     * @param currentUserDto
     * @param usersFileDto
     * @param model
     * @param file
     * @return
     */
    @PostMapping("/main")
    public String addFile(
            @AuthenticationPrincipal UserDto currentUserDto,
            UsersFileDto usersFileDto,
            Model model,
            @RequestParam("file") MultipartFile file) {

        fileService.addFile(currentUserDto, usersFileDto, model, file);
        return "main";
    }

    /**
     * Отображение страницы со списком файлов пользователя с идентификатором id
     *
     * @param currentUserDto
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/user-files/{id}")
    public String userFiles(
            @AuthenticationPrincipal UserDto currentUserDto,
            @PathVariable Long id,
            Model model
    ) {
        fileService.userFiles(currentUserDto, id, model);
        return "userFiles";
    }

    /**
     * Отображение списка пользователей
     *
     * @param currentUserDto
     * @param model
     * @return
     */
    @GetMapping("/all-users")
    public String getAllUsers(
            @AuthenticationPrincipal UserDto currentUserDto,
            Model model
    ) {
        fileService.getAllUsers(currentUserDto, model);
        return "allUsers";
    }

    /**
     * Удалить файл по id
     *
     * @param currentUserDto
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/delete-file/{id}")
    public String deleteFileById(
            @AuthenticationPrincipal UserDto currentUserDto,
            @PathVariable Long id,
            Model model
    ) {
        return fileService.deleteFileById(currentUserDto, id, model);
    }

    /**
     * Скачать файл по id
     *
     * @param currentUserDto
     * @param id
     * @param model
     * @return
     */
    @ResponseBody
    @GetMapping("/download-file/{id}")
    public ResponseEntity downloadFileById(
            @AuthenticationPrincipal UserDto currentUserDto,
            @PathVariable Long id,
            Model model
    ) {
        return fileService.downloadFileById(id, currentUserDto, model);
    }

}