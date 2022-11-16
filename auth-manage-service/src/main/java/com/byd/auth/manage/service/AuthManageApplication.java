package com.byd.auth.manage.service;

import com.google.common.eventbus.EventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author pk
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.byd.auth.manage.dao"})
public class AuthManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthManageApplication.class, args);
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }
}
