package com.example.telegrambot.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {

    @Bean
    public TelegramBot telegramBot(){
        TelegramBot bot = new TelegramBot("5357802270:AAEA6U-pp_SZYgpCjwkFoOxZ5oOO8kIZRsY");
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
