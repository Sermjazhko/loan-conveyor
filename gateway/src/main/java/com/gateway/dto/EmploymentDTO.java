package com.gateway.dto;

import com.gateway.enums.EmploymentStatus;
import com.gateway.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@EqualsAndHashCode
@Schema(description = "Employee Information")
public class EmploymentDTO {

    @Schema(description = "Employee status", example = "UNEMPLOYED")
    private EmploymentStatus employmentStatus;
    @Schema(description = "employer INN", example = "1234567")
    private String employerINN;
    @Schema(description = "Salary", example = "40000.0")
    private BigDecimal salary;
    @Schema(description = "Position in the company", example = "WORKER")
    private Position position;
    @Schema(description = "Work Experience Total", example = "12")
    private Integer workExperienceTotal;
    @Schema(description = "Work Experience Current", example = "12")
    private Integer workExperienceCurrent;
}
