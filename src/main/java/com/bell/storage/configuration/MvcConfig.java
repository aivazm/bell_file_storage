package com.bell.storage.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация MVC
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * Активизация системы авторизации
     * @param registry объект ViewControllerRegistry
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}
