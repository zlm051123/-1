package com.energy.sign_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmSplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmSplusApplication.class, args);
        System.out.println("=====================");
        System.out.println("签到系统后端启动成功！");
        System.out.println("=====================");
    }
}
