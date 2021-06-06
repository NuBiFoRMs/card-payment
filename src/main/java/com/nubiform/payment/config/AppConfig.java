package com.nubiform.payment.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nubiform.payment.security.AES256;
import com.nubiform.payment.security.Encryption;
import com.nubiform.payment.vo.payload.PayloadSerializable;
import com.nubiform.payment.vo.payload.PayloadSerializer;
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

    @Bean
    public Module payloadModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(PayloadSerializable.class, new PayloadSerializer());
        return simpleModule;
    }

    @Profile("dev")
    @Bean
    public Server h2TcpServer() throws SQLException {
        return Server.createTcpServer().start();
    }
}
