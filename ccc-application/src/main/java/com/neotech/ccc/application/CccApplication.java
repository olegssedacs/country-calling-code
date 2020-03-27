package com.neotech.ccc.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "com.neotech")
public class CccApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CccApplication.class).run(args);
    }

}
