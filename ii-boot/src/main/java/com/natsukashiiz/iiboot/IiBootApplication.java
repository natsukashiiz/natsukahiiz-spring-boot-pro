package com.natsukashiiz.iiboot;

import com.natsukashiiz.iiboot.configuration.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
public class IiBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(IiBootApplication.class, args);
    }

}
