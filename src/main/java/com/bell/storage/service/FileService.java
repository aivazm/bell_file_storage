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
     * @param userDto объект UserDto
     * @param model объект Model
     */
    void getMyFiles(UserDto userDto, Model model);

    /**
     * Добавить новый файл
     * @param userDto объект UserDto
     * @param usersFileDto объект UsersFileDto
     * @param model объект Model
     * @param file объект MultipartFile
     */
    void addFile(UserDto userDto, UsersFileDto usersFileDto, Model model, MultipartFile file);

    /**
     * Отображение файлов пользователя с id. Сервис определяет роль и права на доступ пользователя currentUserDto
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param id значение типа Long - id пользователя, чьи файлы отображаются
     * @param model объект Model
     */
    void userFiles(UserDto currentUserDto, Long id, Model model);

    /**
     * Получить всех пользователей. Получает количество пользователей в системе для пользователя с ролью Аналитик
     * @param currentUser объект UserDto - текущий пользователь
     * @param model объект Model
     */
    void getAllUsers(UserDto currentUser, Model model);

    /**
     * Удаление файла по id. Если собственником файла является currentUserDto, redirect на страницу currentUserDto,
     * иначе переход на страницу собственника файла
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param id значение типа Long - id удаляемого файла
     * @param model объект Model
     * @return редирект на /main либо на /user-files/
     */
    String deleteFileById(UserDto currentUserDto, Long id, Model model);

    /**
     * Скачивание файла
     * @param id значение типа Long - id скачиваемого файла
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param model объект Model
     * @return ResponseEntity с объектом ByteArrayResource в теле.
     */
    ResponseEntity downloadFileById(Long id, UserDto currentUserDto, Model model);
}
