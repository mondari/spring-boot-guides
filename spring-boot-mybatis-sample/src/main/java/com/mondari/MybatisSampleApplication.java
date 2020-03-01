package com.mondari;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.mondari.mapper")
@SpringBootApplication
public class MybatisSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisSampleApplication.class, args);
    }

}
