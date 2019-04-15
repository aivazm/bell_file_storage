package com.bell.storage.controller;

import com.bell.storage.dto.UserDto;
import com.bell.storage.service.AccessService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Контроллер доступа к просмотру и скачиванию файлов
 */
@Controller
public class AccessController {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    /**
     * Отображение открытого доступа и запросов на доступ к просмотру и скачиванию файлов
     * @param currentUserDto объект класса UserDto
     * @param model объект Model
     * @return String имя страницы accessAndRequestPage.ftl
     */
    @GetMapping("/accessAndRequestPage/")
    public String getAccessAndRequest(
            @AuthenticationPrincipal UserDto currentUserDto,
            Model model
    ) {
        accessService.getAccessAndRequest(currentUserDto, model);
        return "accessAndRequestPage";
    }

    /**
     * Запрос на доступ к просмотру файлов
     * @param currentUserDto объект класса UserDto - текущий пользователь
     * @param userId некоторое значение типа Long - id запрашиваемого пользователя
     * @return String редирект на url /all-users
     */
    @GetMapping("/request-visible-access/{userId}")
    public String requestToVisibleAccess(
            @AuthenticationPrincipal UserDto currentUserDto,
            @PathVariable Long userId
    ) {
        accessService.requestToVisibleAccess(currentUserDto, userId);
        return "redirect:/all-users";
    }

    /**
     * Запрос на доступ к скачиванию файлов
     * @param currentUserDto объект класса UserDto - текущий пользователь
     * @param userId некоторое значение типа Long - id запрашиваемого пользователя
     * @return String редирект на url /all-users
     */
    @GetMapping("/request-download-access/{userId}")
    public String requestToDownloadAccess(
            @AuthenticationPrincipal UserDto currentUserDto,
            @PathVariable Long userId
    ) {
        accessService.requestToDownloadAccess(currentUserDto, userId);
        return "redirect:/all-users";
    }

    /**
     * Подтверждение запроса на доступ к просмотру файлов
     * @param currentUserDto объект класса UserDto - текущий пользователь
     * @param userId некоторое значение типа Long - id запрашиваемого пользователя
     * @return String редирект на url /accessAndRequestPage/
     */
    @GetMapping("/confirm-visible-access/{userId}")
    public String confirmVisibleAccess(
            @AuthenticationPrincipal UserDto currentUserDto,
            @PathVariable Long userId
    ) {
        accessService.confirmVisibleAccess(currentUserDto, userId);
        return "redirect:/accessAndRequestPage/";
    }

    /**
     * Подтверждение запроса на доступ к скачиванию файлов
     * @param currentUserDto объект класса UserDto - текущий пользователь
     * @param userId некоторое значение типа Long - id запрашиваемого пользователя
     * @return String редирект на url /accessAndRequestPage/
     */
    @GetMapping("/confirm-download-access/{userId}")
    public String confirmDownloadAccess(
            @AuthenticationPrincipal UserDto currentUserDto,
            @PathVariable Long userId
    ) {
        accessService.confirmDownloadAccess(currentUserDto, userId);
        return "redirect:/accessAndRequestPage/";
    }

    /**
     * Отказ запроса  на доступ к просмотру файлов
     * @param currentUserDto объект класса UserDto - текущий пользователь
     * @param userId некоторое значение типа Long - id запрашиваемого пользователя
     * @return String редирект на url /accessAndRequestPage/
     */
    @GetMapping("/refuse-visible-access/{userId}")
    public String refuseVisibleAccess(
            @AuthenticationPrincipal UserDto currentUserDto,
            @PathVariable Long userId
    ) {
        accessService.refuseVisibleAccess(currentUserDto, userId);
        return "redirect:/accessAndRequestPage/";
    }

    /**
     * Отказ запроса на доступ к скачиванию файлов
     * @param currentUserDto объект класса UserDto - текущий пользователь
     * @param userId некоторое значение типа Long - id запрашиваемого пользователя
     * @return String редирект на url /accessAndRequestPage/
     */
    @GetMapping("/refuse-download-access/{userId}")
    public String refuseDownloadAccess(
            @AuthenticationPrincipal UserDto currentUserDto,
            @PathVariable Long userId
    ) {
        accessService.refuseDownloadAccess(currentUserDto, userId);
        return "redirect:/accessAndRequestPage/";
    }

    /**
     * Отмена доступа к просмотру файлов
     * @param currentUserDto объект класса UserDto - текущий пользователь
     * @param userId некоторое значение типа Long - id запрашиваемого пользователя
     * @return String редирект на url /accessAndRequestPage/
     */
    @GetMapping("/cancel-visible-access/{userId}")
    public String cancelVisibleAccess(
            @AuthenticationPrincipal UserDto currentUserDto,
            @PathVariable Long userId
    ) {
        accessService.cancelVisibleAccess(currentUserDto, userId);
        return "redirect:/accessAndRequestPage/";
    }

    /**
     * Отмена доступа к скачиванию файлов
     * @param currentUserDto объект класса UserDto - текущий пользователь
     * @param userId некоторое значение типа Long - id запрашиваемого пользователя
     * @return String редирект на url /accessAndRequestPage/
     */
    @GetMapping("/cancel-download-access/{userId}")
    public String cancelDownloadAccess(
            @AuthenticationPrincipal UserDto currentUserDto,
            @PathVariable Long userId
    ) {
        accessService.cancelDownloadAccess(currentUserDto, userId);
        return "redirect:/accessAndRequestPage/";
    }

}
