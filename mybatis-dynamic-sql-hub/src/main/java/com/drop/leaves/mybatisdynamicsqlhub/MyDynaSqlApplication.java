package com.drop.leaves.mybatisdynamicsqlhub;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author by mobingsen on 2021/6/10 20:51
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class MyDynaSqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyDynaSqlApplication.class);
    }
}
