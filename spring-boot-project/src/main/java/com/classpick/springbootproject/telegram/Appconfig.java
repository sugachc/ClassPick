package com.classpick.springbootproject.telegram;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class Appconfig {

    private String telegramKey = "6288354229:AAG_WmdY1Xnz-j_YkgTMUrI7-RZpT0y1bxw";
    //private final UserRepository userRepository;

//    @Bean
//    public UpdateUserChatId updateUserChatId() {
//        UpdateUserChatId updateUserChatId = new UpdateUserChatId(telegramBot());
//        updateUserChatId.updateUserChatId(userRepository);
//        return updateUserChatId;
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(telegramKey);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
