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
     * @return String имя страницы greeting.ftl
     */
    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    /**
     * Домашняя страница пользователя. Представляет файлы пользователя
     *
     * @param currentUserDto объект класса UserDto
     * @param model объект Model
     * @return String имя страницы main.ftl
     */
    @GetMapping("/main")
    public String getMyFiles(@AuthenticationPrincipal UserDto currentUserDto, Model model) {
        fileService.getMyFiles(currentUserDto, model);
        return "main";
    }

    /**
     * Добавление файла.
     *
     * @param currentUserDto объект класса UserDto
     * @param usersFileDto объект класса UsersFileDto
     * @param model объект Model
     * @param file объект MultipartFile
     * @return String имя страницы main.ftl
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
     * @param currentUserDto объект класса UserDto
     * @param id значение типа Long. Идентификатор пользователя
     * @param model объект Model
     * @return String имя страницы userFiles.ftl
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
     * @param currentUserDto объект класса UserDto
     * @param model объект Model
     * @return String имя страницы allUsers.ftl
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
     * @param currentUserDto объект класса UserDto
     * @param id некоторое значение типа Long
     * @param model объект Model
     * @return String. результат работы метода deleteFileById(UserDto currentUserDto, Long id, Model model)
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
     * @param currentUserDto объект класса UserDto
     * @param id некоторое значение типа Long
     * @param model объект Model
     * @return ResponseEntity с объектом ByteArrayResource в теле
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