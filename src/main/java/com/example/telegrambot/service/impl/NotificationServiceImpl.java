package com.example.telegrambot.service.impl;

import com.example.telegrambot.model.NotificationTask;
import com.example.telegrambot.repository.NotificationTaskRepository;
import com.example.telegrambot.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private static final String REGEX_BOT_MESSAGE = "([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)";

    private static final DateTimeFormatter DATE_TAME_FORMATER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationServiceImpl(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @Override
    public NotificationTask schedule(NotificationTask task, Long chatId) {
        task.setChatId(chatId);
        NotificationTask storedTask = notificationTaskRepository.save(task);
        logger.info("Напоминание успешно сохранено " + storedTask);
        return storedTask;
    }

    public Optional<NotificationTask> parse(String notificationBotMessage) {
        Pattern pattern = Pattern.compile(REGEX_BOT_MESSAGE);
        Matcher matcher = pattern.matcher(notificationBotMessage);

        NotificationTask result = null;
        try {
            if (matcher.find()) {
                LocalDateTime notificationDateTime = LocalDateTime.parse(matcher.group(1), DATE_TAME_FORMATER);
                String notification = matcher.group(3);
                result = new NotificationTask(notification, notificationDateTime);
            }
        } catch (Exception e) {
            logger.info("parse провален: " + notificationBotMessage, e);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public void notifyAllScheduledTasks(Consumer<NotificationTask> notifier) {
        logger.info("notifyAllScheduledTasks is start");
        Collection<NotificationTask> notifications = notificationTaskRepository.getByNotificationDate();
        notifications.forEach(task -> {notifier.accept(task);
                                        task.markAsSent();
        });
        notificationTaskRepository.saveAll(notifications);
        logger.info("notifyAllScheduledTasks is finish");
    }



}
