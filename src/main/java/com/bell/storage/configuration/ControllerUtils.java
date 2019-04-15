package com.bell.storage.configuration;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Утилитный класс для сбора ошибок валидации
 */
public class ControllerUtils {
    /**
     * Получить ошибки валидации
     * @param bindingResult объект BindingResult
     * @return Мапа с ошибками
     */
    public static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}