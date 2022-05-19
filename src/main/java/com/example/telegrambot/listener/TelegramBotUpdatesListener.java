package com.example.telegrambot.listener;

import com.example.telegrambot.model.NotificationTask;
import com.example.telegrambot.service.NotificationService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private static final String START_CMD = "/start";

    private static final String GREETING_TEXT = "Hello!";

    private static final String INVALID_CMD_FORMAT = "Неверный формат запроса. Корректный формат запроса: ДД.ММ.ГГГГ ЧЧ:ММ Текст сообщения";

    private final NotificationService notificationService;

    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationService notificationService) {
        this.telegramBot = telegramBot;
        this.notificationService = notificationService;

    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Message message = update.message();
            if (message.text().startsWith(START_CMD)) {
                logger.info(START_CMD + " было запущено");
                sendMessage(extractChatId(message), GREETING_TEXT);
            } else {
                Optional<NotificationTask> parseResult = notificationService.parse(message.text());
                if (parseResult.isPresent()) {
                    scheduleNotification(extractChatId(message), parseResult.get());
                } else {
                    sendMessage(extractChatId(message), INVALID_CMD_FORMAT);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private Long extractChatId(Message message) {
        return message.chat().id();
    }

    private void scheduleNotification(Long chatId, NotificationTask task) {
        notificationService.schedule(task, chatId);
        sendMessage(chatId, "Напоминание успешно создано!");
    }

    private void sendMessage(Long chatId, String messageText) {
        SendMessage sendMessage = new SendMessage(chatId, messageText);
        telegramBot.execute(sendMessage);
    }

    private void sendMessage(NotificationTask task) {
        sendMessage(task.getChatId(), task.getNotificationMessage());
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void notifyScheduledTask() {
        notificationService.notifyAllScheduledTasks(this::sendMessage);
    }



}
