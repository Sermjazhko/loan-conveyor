package com.conveyor.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDTO {

    private Enum employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Enum position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
