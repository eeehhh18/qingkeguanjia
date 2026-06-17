package com.qk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 轻客管家 - 启动类
 */
@SpringBootApplication
@MapperScan("com.qk.mapper")
public class QkApplication {

    public static void main(String[] args) {
        SpringApplication.run(QkApplication.class, args);
    }
}
