package com.enndfp.userrightsbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.enndfp.userrightsbackend.Mapper")
public class UserRightsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserRightsBackendApplication.class, args);
    }

}
