package com.alfonsoalmonte.ipinformation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IpInformationApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpInformationApplication.class, args);
    }

}
