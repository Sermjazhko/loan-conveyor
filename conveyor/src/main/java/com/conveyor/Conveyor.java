package com.conveyor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Conveyor {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Conveyor.class, args);
    }
}