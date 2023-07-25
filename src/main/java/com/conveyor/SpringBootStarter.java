package com.conveyor;

import com.conveyor.dto.LoanApplicationRequestDTO;

import com.conveyor.validation.DataValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class SpringBootStarter {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStarter.class, args);
        //LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();
        //System.out.println("hello");
    }
}