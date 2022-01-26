package com.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: testWithMaven
 * @description:
 * @author: gwb
 * @create: 2021-12-30 09:32
 **/
@SpringBootApplication
@MapperScan({"com.test.mapper"})
public class Test {

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }
}
