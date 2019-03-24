package com.bell.storage.service;

import com.bell.storage.dto.UserDto;
import com.bell.storage.model.User;
import org.springframework.ui.Model;

/**
 * Сервис обработки запросов на доступ к просмотру и скачиванию файлов
 */
public interface AccessService {

    /**
     * Отображение открытого доступа и запросов на доступ к просмотру и скачиванию файлов
     * Передает в атрибуты параметра model множества запросов на доспут к просмотру и скачиванию,
     * множества открытого доступа к просмотру и скачиванию файлов.
     * @param currentUserDto
     * @param model
     */
    void getAccessAndRequest(UserDto currentUserDto, Model model);

    /**
     * Добавление в множество запросов на доступк с просмоту файлов пользователя с id userId запрос от пользователя currentUserDto
     * @param currentUserDto
     * @param userId
     */
    void requestToVisibleAccess(UserDto currentUserDto, Long userId);

    /**
     * Добавление в множество запросов на доступк с скачиванию файлов пользователя с id userId запрос от пользователя currentUserDto
     * @param currentUserDto
     * @param userId
     */
    void requestToDownloadAccess(UserDto currentUserDto, Long userId);

    /**
     * Подтверждение пользователем currentUserDto запроса на доступ к просмотру файлов от пользователя с id userId
     * @param currentUserDto
     * @param userId
     */
    void confirmVisibleAccess(UserDto currentUserDto, Long userId);

    /**
     * Подтверждение пользователем currentUserDto запроса на доступ к скачиванию файлов от пользователя с id userId
     * @param currentUserDto
     * @param userId
     */
    void confirmDownloadAccess(UserDto currentUserDto, Long userId);

    /**
     * Отклонение пользователем currentUserDto запроса на доступ к просмотру файлов от пользователя с id userId
     * @param currentUserDto
     * @param userId
     */
    void refuseVisibleAccess(UserDto currentUserDto, Long userId);

    /**
     * Отклонение пользователем currentUserDto запроса на доступ к скачиванию файлов от пользователя с id userId
     * @param currentUserDto
     * @param userId
     */
    void refuseDownloadAccess(UserDto currentUserDto, Long userId);

    /**
     * Отмена пользователем currentUserDto доступа к просмотру файлов от пользователя с id userId
     * @param currentUserDto
     * @param userId
     */
    void cancelVisibleAccess(UserDto currentUserDto, Long userId);

    /**
     * Отмена пользователем currentUserDto доступа к скачиванию файлов от пользователя с id userId
     * @param currentUserDto
     * @param userId
     */
    void cancelDownloadAccess(UserDto currentUserDto, Long userId);
}
