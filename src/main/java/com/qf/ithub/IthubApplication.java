package com.qf.ithub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.qf.ithub.mapper")
public class IthubApplication {

    public static void main(String[] args) {
        SpringApplication.run(IthubApplication.class, args);
    }

}
