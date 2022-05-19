-- liquibase formatted sql

-- changeset ASergeenkova:1
create table notification_task
(id serial not null primary key,
chatId int not null,
notificationDate timestamp not null,
notificationMessage text not null);

-- changeset ASergeenkova:2
alter table notification_task alter column chatId type bigint;

-- changeset ASergeenkova:3
alter table notification_task drop column chatId;
alter table notification_task add column chatId bigint;

-- changeset ASergeenkova:4
drop table notification_task;

create table notification_task
(id serial not null primary key,
 chatId bigint not null,
 notificationDate timestamp not null,
 notificationMessage text not null);
