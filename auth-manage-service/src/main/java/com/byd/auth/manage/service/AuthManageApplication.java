package com.byd.auth.manage.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author pk
 */
@SpringBootApplication
@ComponentScan(value = {"com.byd.auth.manage"})
@MapperScan(basePackages = {"com.byd.auth.manage.dao"})
@EnableCaching
public class AuthManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthManageApplication.class, args);
    }
}
