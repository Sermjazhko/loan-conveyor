package com.conveyor.dto;

import java.math.BigDecimal;
import java.util.Objects;

import com.conveyor.scoring.EmploymentStatus;
import com.conveyor.scoring.Position;
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
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    @Schema(description = "Position in the company: manager, middle manager, top manager")
    private Position position;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmploymentDTO that = (EmploymentDTO) o;
        return Objects.equals(employmentStatus, that.employmentStatus) &&
                Objects.equals(employerINN, that.employerINN) &&
                Objects.equals(salary, that.salary) &&
                Objects.equals(position, that.position) &&
                Objects.equals(workExperienceTotal, that.workExperienceTotal) &&
                Objects.equals(workExperienceCurrent, that.workExperienceCurrent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employmentStatus, employerINN, salary,
                position, workExperienceTotal, workExperienceCurrent);
    }
}
