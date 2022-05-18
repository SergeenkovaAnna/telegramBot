-- liquibase formatted sql

-- changeset ASergeenkova:1
create table notification_task
(id serial not null primary key,
chatId int not null,
notificationDate timestamp not null,
notificationMessage text not null);

