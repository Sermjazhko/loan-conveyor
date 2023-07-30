package com.conveyor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information about the user for the formation of a loan")
public class ScoringDataDTO {

    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private Enum gender;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private Enum maritalStatus;
    private Integer dependentAmount;
    private EmploymentDTO employment;
    private String account;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

    @Override
    public String toString() {
        return "\nScoringDataDTO{" +
                "amount= " + amount +
                ", term= " + term +
                ", firstName= '" + firstName + '\'' +
                ", lastName= '" + lastName + '\'' +
                ", middleName= '" + middleName + '\'' +
                ", gender= " + gender +
                ", birthdate= " + birthdate +
                ", passportSeries= '" + passportSeries + '\'' +
                ", passportNumber= '" + passportNumber + '\'' +
                ", passportIssueDate= " + passportIssueDate +
                ", passportIssueBranch= '" + passportIssueBranch + '\'' +
                ", maritalStatus= " + maritalStatus +
                ", dependentAmount= " + dependentAmount +
                ", employment= " + employment +
                ", account= '" + account + '\'' +
                ", isInsuranceEnabled= " + isInsuranceEnabled +
                ", isSalaryClient= " + isSalaryClient +
                '}';
    }
}
