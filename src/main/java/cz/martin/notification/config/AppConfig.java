package cz.martin.notification.config;

import cz.martin.notification.email.template.EmailMessage;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public EmailMessage emailMessage(){return new EmailMessage();}


}
