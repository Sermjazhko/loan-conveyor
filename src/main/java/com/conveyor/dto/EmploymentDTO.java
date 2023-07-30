package com.conveyor.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employee Information")
public class EmploymentDTO {

    @Schema(description = "Employee status: self-employed/unemployed/business")
    private Enum employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    @Schema(description = "Position in the company: manager, middle manager, top manager")
    private Enum position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

    @Override
    public String toString() {
        return "EmploymentDTO{" +
                "employmentStatus=" + employmentStatus +
                ", employerINN='" + employerINN + '\'' +
                ", salary=" + salary +
                ", position=" + position +
                ", workExperienceTotal=" + workExperienceTotal +
                ", workExperienceCurrent=" + workExperienceCurrent +
                '}';
    }
}
