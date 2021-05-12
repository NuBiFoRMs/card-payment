package com.nubiform.payment.config;

import com.nubiform.payment.security.AES256;
import com.nubiform.payment.security.Encryption;
import lombok.RequiredArgsConstructor;
import org.h2.tools.Server;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

@RequiredArgsConstructor
@Configuration
public class AppConfig {

    private final AppProperties appProperties;

    @Bean
    public Encryption encryption() {
        return new AES256(appProperties.getSecretKey());
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE)
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE);
        return modelMapper;
    }

    @Profile("dev")
    @Bean
    public Server h2TcpServer() throws SQLException {
        return Server.createTcpServer().start();
    }
}
