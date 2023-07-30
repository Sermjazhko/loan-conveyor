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
        //System.out.println("hello");
        /*ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
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
        ScoringService scoring = new ScoringService();
        ConveyorServiceImpl conveyorService = new ConveyorServiceImpl(scoring);
        CreditDTO creditDTO = conveyorService.getCalculation(scoringDataDTO);
        System.out.println(creditDTO.getPaymentSchedule());
        System.out.println(creditDTO.getPsk());*/
/*

        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();
        loanApplicationRequestDTO.setAmount(BigDecimal.valueOf(500));
        loanApplicationRequestDTO.setTerm(10);
        ScoringService scoringService = new ScoringService();
        ConveyorServiceImpl conveyorService = new ConveyorServiceImpl(scoringService);

        List<LoanOfferDTO> offers = conveyorService.getOffers(loanApplicationRequestDTO);
*/

    }
}