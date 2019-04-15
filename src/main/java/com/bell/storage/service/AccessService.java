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
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param model объект Model
     */
    void getAccessAndRequest(UserDto currentUserDto, Model model);

    /**
     * Добавление в множество запросов на доступк с просмоту файлов пользователя с id userId запрос от пользователя currentUserDto
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param userId значение типа Long - id запрашиваемого пользователя
     */
    void requestToVisibleAccess(UserDto currentUserDto, Long userId);

    /**
     * Добавление в множество запросов на доступк с скачиванию файлов пользователя с id userId запрос от пользователя currentUserDto
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param userId значение типа Long - id запрашиваемого пользователя
     */
    void requestToDownloadAccess(UserDto currentUserDto, Long userId);

    /**
     * Подтверждение пользователем currentUserDto запроса на доступ к просмотру файлов от пользователя с id userId
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param userId значение типа Long - id запрашиваемого пользователя
     */
    void confirmVisibleAccess(UserDto currentUserDto, Long userId);

    /**
     * Подтверждение пользователем currentUserDto запроса на доступ к скачиванию файлов от пользователя с id userId
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param userId значение типа Long - id запрашиваемого пользователя
     */
    void confirmDownloadAccess(UserDto currentUserDto, Long userId);

    /**
     * Отклонение пользователем currentUserDto запроса на доступ к просмотру файлов от пользователя с id userId
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param userId значение типа Long - id запрашиваемого пользователя
     */
    void refuseVisibleAccess(UserDto currentUserDto, Long userId);

    /**
     * Отклонение пользователем currentUserDto запроса на доступ к скачиванию файлов от пользователя с id userId
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param userId значение типа Long - id запрашиваемого пользователя
     */
    void refuseDownloadAccess(UserDto currentUserDto, Long userId);

    /**
     * Отмена пользователем currentUserDto доступа к просмотру файлов от пользователя с id userId
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param userId значение типа Long - id запрашиваемого пользователя
     */
    void cancelVisibleAccess(UserDto currentUserDto, Long userId);

    /**
     * Отмена пользователем currentUserDto доступа к скачиванию файлов от пользователя с id userId
     * @param currentUserDto объект UserDto - текущий пользователь
     * @param userId значение типа Long - id запрашиваемого пользователя
     */
    void cancelDownloadAccess(UserDto currentUserDto, Long userId);
}
