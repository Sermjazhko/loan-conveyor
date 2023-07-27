package com.conveyor;

import com.conveyor.dto.CreditDTO;
import com.conveyor.dto.EmploymentDTO;
import com.conveyor.dto.LoanApplicationRequestDTO;

import com.conveyor.dto.ScoringDataDTO;
import com.conveyor.scoring.EmploymentStatus;
import com.conveyor.scoring.Gender;
import com.conveyor.scoring.MaritalStatus;
import com.conveyor.scoring.Position;
import com.conveyor.service.ConveyorServiceImpl;
import com.conveyor.validation.DataValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

//@SpringBootApplication
public class SpringBootStarter {

    public static void main(String[] args) throws IOException {
        //    SpringApplication.run(SpringBootStarter.class, args);
        //LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();
        //System.out.println("hello");
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        scoringDataDTO.setAccount("1234");
        scoringDataDTO.setBirthdate(LocalDate.of(2000, 12, 10));
        scoringDataDTO.setAmount(BigDecimal.valueOf(20000));
        scoringDataDTO.setTerm(12);
        scoringDataDTO.setFirstName("Al");
        scoringDataDTO.setLastName("Ll");
        scoringDataDTO.setMiddleName("DD");
        scoringDataDTO.setGender(Gender.MAN);
        scoringDataDTO.setPassportSeries("1212");
        scoringDataDTO.setPassportNumber("121212");
        scoringDataDTO.setPassportIssueDate(LocalDate.of(2020, 12, 12));
        scoringDataDTO.setPassportIssueBranch("я не знаю, кто-то просто выдал");
        scoringDataDTO.setMaritalStatus(MaritalStatus.MARRIED);
        scoringDataDTO.setDependentAmount(1);
        scoringDataDTO.setEmployment(new EmploymentDTO(EmploymentStatus.SELF_EMPLOYED, "123123134", BigDecimal.valueOf(50000),
                Position.MIDDLE_MANAGER, 20, 20));
        scoringDataDTO.setIsInsuranceEnabled(true);
        scoringDataDTO.setIsSalaryClient(true);

        System.out.println("hello");
        System.out.println(scoringDataDTO.getEmployment());
        ConveyorServiceImpl conveyorService = new ConveyorServiceImpl();
        CreditDTO creditDTO = conveyorService.getCalculation(scoringDataDTO);
        System.out.println(creditDTO.getPaymentSchedule());
        System.out.println(creditDTO.getPsk());
    }
}