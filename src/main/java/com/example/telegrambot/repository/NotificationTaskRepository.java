package com.example.telegrambot.repository;

import com.example.telegrambot.model.NotificationTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    @Query("from NotificationTask where notificationDate < current_timestamp")
    Collection<NotificationTask> getByNotificationDate();

}
