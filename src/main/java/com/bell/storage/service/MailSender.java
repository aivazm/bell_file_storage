package com.bell.storage.service;

/**
 * Сервис для отправки сообщений активации пользователя
 */
public interface MailSender {
    /**
     * Отправить письмо с текстом message на адрес emailTo
     * @param emailTo адресат
     * @param subject тема письма
     * @param message текст письма
     */
    void send(String emailTo, String subject, String message);
}
