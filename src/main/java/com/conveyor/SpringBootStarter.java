package com.conveyor;

import com.conveyor.dto.*;

import com.conveyor.scoring.EmploymentStatus;
import com.conveyor.scoring.Gender;
import com.conveyor.scoring.MaritalStatus;
import com.conveyor.scoring.Position;
import com.conveyor.service.ConveyorServiceImpl;
import com.conveyor.service.ScoringService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class SpringBootStarter {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SpringBootStarter.class, args);
    }
}