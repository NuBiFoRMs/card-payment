package com.nubiform.payment.config;

import com.nubiform.payment.security.AES256;
import com.nubiform.payment.security.Encryption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    @Bean
    public Encryption encryption() {
        return new AES256();
    }
}
