package com.example.telegrambot.repository;

import com.example.telegrambot.model.NotificationTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    Collection<NotificationTask> getByNotificationDate();

}
