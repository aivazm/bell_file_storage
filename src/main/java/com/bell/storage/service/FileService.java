package com.bell.storage.service;

import com.bell.storage.dto.UserDto;
import com.bell.storage.dto.UsersFileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

/**
 * Сервис операций с файлами
 */
public interface FileService {

    /**
     * Получить файлы пользователя userDto
     * @param userDto
     * @param model
     */
    void getMyFiles(UserDto userDto, Model model);

    /**
     * Добавить новый файл
     * @param userDto
     * @param usersFileDto
     * @param model
     * @param file
     */
    void addFile(UserDto userDto, UsersFileDto usersFileDto, Model model, MultipartFile file);

    /**
     * Отображение файлов пользователя с id. Сервис определяет роль и права на доступ пользователя currentUserDto
     * @param currentUserDto
     * @param id
     * @param model
     */
    void userFiles(UserDto currentUserDto, Long id, Model model);

    /**
     * Получить всех пользователей. Получает количество пользователей в системе для пользователя с ролью Аналитик
     * @param currentUser
     * @param model
     */
    void getAllUsers(UserDto currentUser, Model model);

    /**
     * Удаление файла по id. Если собственником файла является currentUserDto, redirect на страницу currentUserDto,
     * иначе переход на страницу собственника файла
     * @param currentUserDto
     * @param id
     * @param model
     * @return
     */
    String deleteFileById(UserDto currentUserDto, Long id, Model model);

    /**
     * Скачивание файла
     * @param id
     * @param userDto
     * @param model
     * @return
     */
    ResponseEntity downloadFileById(Long id, UserDto userDto, Model model);
}
